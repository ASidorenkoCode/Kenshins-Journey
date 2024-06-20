package game.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.animations.PlayerAnimations;
import entities.controller.EntityController;
import entities.logic.Player;
import game.UI.GameView;
import game.logic.GameEngine;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import gameObjects.logic.Finish;
import items.controller.ItemController;
import javazoom.jl.decoder.JavaLayerException;
import keyboardinputs.logic.Control;
import maps.controller.MapController;
import network.client.Client;
import network.client.GitHubClient;
import network.data.ServerObject;
import network.data.SharedData;
import network.host.Host;
import network.host.HostChecker;
import screens.controller.ScreenController;
import screens.ui.ControlScreen;
import screens.ui.DeathScreen;
import sound.SoundController;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private GameObjectController gameObjectController;
    private MapController mapController;
    private ScreenController screenController;
    private ItemController itemController;
    private SoundController soundController;
    private Highscore highscore;
    private String playerId;
    private String ipAddress;
    private Player.PlayerSerializer playerSerializer;

    private GameState currentGameState;
    private int getAmountOfCurrentItemsRegistered;
    private static final Gson gson = new Gson();
    private final Type type = new TypeToken<GameControls[]>() {
    }.getType();

    //network vars
    private long comparingTime;
    private ArrayList<ServerObject> serverObjects;
    private boolean isPlayingMultiplayer;
    private Client client;
    private boolean showHitbox;

    public GameController(boolean showHitBox) throws IOException {
        comparingTime = System.currentTimeMillis();
        serverObjects = new ArrayList<>();
        isPlayingMultiplayer = false;
        //controller
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            playerId = inetAddress.getHostName();
            ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        currentGameState = GameState.START;
        this.highscore = Highscore.readHighscore();

        mapController = new MapController(null, highscore);
        entityController = new EntityController(mapController, showHitBox);
        mapController.setEntityController(entityController);
        itemController = new ItemController(mapController, showHitBox);
        gameEngine = new GameEngine(this);
        gameObjectController = new GameObjectController(mapController, showHitBox);
        soundController = new SoundController();
        screenController = new ScreenController(itemController);
        gameView = new GameView(this, entityController, mapController, itemController, gameObjectController, screenController);

        //ui
        gameView.gameWindow();
        gameEngine.startGameLoop();
        this.showHitbox = showHitBox;
        loadAndInitializeControls();
    }


    public void showFPS_UPS() {
        gameView.showFPS_UPS(gameEngine.getFrames(), gameEngine.getUpdates());
    }

    public void callRepaint() {
        gameView.repaint();
    }

    public void update() throws IOException, JavaLayerException {
        if(currentGameState == GameState.PLAYING) {

            if (isPlayingMultiplayer && !SharedData.networkToGameQueue.isEmpty()) {
                try {
                    serverObjects = SharedData.networkToGameQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Player player = entityController.getPlayer();
            if (player.isDead() && player.getDeathAnimationFinished()) {
                currentGameState = GameState.DEAD;
                return;
            }
            if (gameObjectController.checkIfPlayerIsInFinish(entityController.getPlayer()) && !entityController.getPlayer().getDeathAnimationFinished()) {
                loadNewMap();
            }

            if (highscore.getCurrentHighscore() <= 0) {
                currentGameState = GameState.END;
                return;
            }
            entityController.update(mapController, gameObjectController, highscore);
            itemController.update(entityController);
            highscore.decreaseHighScoreAfterOneSecond();
            screenController.update(highscore, entityController.getPlayer(), itemController.getMenu(), serverObjects);

            if (isPlayingMultiplayer && SharedData.gameToNetworkQueue.isEmpty()) {
                try {
                    SharedData.gameToNetworkQueue.put(new ServerObject(highscore, entityController.getPlayer(), playerId));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (!isPlayingMultiplayer) {
                serverObjects.clear();
            }

        }

        if (currentGameState == GameState.LOADING && screenController.getLoadingScreen().isLoadingFinished()) {
            currentGameState = GameState.PLAYING;
            screenController.getLoadingScreen().resetProgress();
        }

        switchSound();
    }

    public DeathScreen getDeathScreen() {
        //TODO: do not handle this here
        return screenController.getDeathScreen();
    }

    public void loadNewMap() throws IOException {
        currentGameState = GameState.LOADING;
        //highscore update
        highscore.increaseHighscoreForItems(itemController.getMenu());
        highscore.addCurrentHighscoreToList();
        highscore.writeHighscore();


        //handle option of game is finished
        if (mapController.getMaps().size() == highscore.getAllHighscores().size()) {
            currentGameState = GameState.END;
            return;
        }

        Player player = entityController.getPlayer();
        playerSerializer.writePlayer(player);
        initOrUpdateGame();
    }

    private void initOrUpdateGame() throws IOException {
        mapController.loadCurrentMapIndex(highscore.getAllHighscores().size());
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initEnemies(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        entityController.initBoss(mapController, showHitbox);
        itemController.initItems(mapController);
        itemController.deleteAllItemsFromMenu();
        getAmountOfCurrentItemsRegistered = 0;
    }

    public void restartLevelAfterDeath() {
        if (currentGameState != GameState.DEAD) return;
        Player player = entityController.getPlayer();
        player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
        gameObjectController.updatePoints(mapController, entityController.getCurrentBoss() == null || entityController.getCurrentBoss().getIsDead());
        player.resetHealth();
        player.resetDeath();
        highscore.decreaseHighscoreForDeath();
        highscore.increaseDeathCounter();
        currentGameState = GameState.PLAYING;
    }

    public void startGame() throws IOException {
        if (currentGameState != GameState.START) return;
        Player player = entityController.getPlayer();
        if (!highscore.getAllHighscores().isEmpty()) {
            int loadedHealth = playerSerializer.readPlayerHealth();
            if (loadedHealth != -1) {
                player.setHealth(loadedHealth);
            }
        }
        currentGameState = GameState.LOADING;
    }

    public void resetGame() throws IOException {
        if (currentGameState != GameState.END) return;
        highscore.writeAllHighscores();
        highscore.resetHighscore();
        Player player = entityController.getPlayer();
        player.resetHealth();
        initOrUpdateGame();
        currentGameState = GameState.START;
    }

    public void newGame() throws IOException {
        highscore.deleteHighscoreFile();
        highscore.resetHighscore();
        Player player = entityController.getPlayer();
        player.resetHealth();
        initOrUpdateGame();
        startGame();
    }

    public Highscore getHighscore() {
        return highscore;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setIsDrawingListOfCurrentPlayersForInterfaceGame(boolean isDrawingListOfCurrentPlayers) {
        screenController.getInterfaceGame().setIsDrawingCurrentListOfPlayers(isDrawingListOfCurrentPlayers);
    }

    public void switchSound() throws JavaLayerException {
        if (currentGameState != soundController.getCurrentGameState()) {
            soundController.setCurrentGameState(currentGameState);
            soundController.soundControl();
        }
        if (currentGameState == GameState.PLAYING) {
            if (!soundController.isSoundEffectPlaying()) {
                if (entityController.getPlayer().isDead()) {
                    soundController.playSoundEffect("res/sounds/soundeffects/death.mp3", 1200);
                    return;
                }
                if (getAmountOfCurrentItemsRegistered < itemController.getActualItemCount()) {
                    soundController.playSoundEffect("res/sounds/soundeffects/pickup_item.mp3", 10);
                    getAmountOfCurrentItemsRegistered++;
                    return;
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.RUN) {
                    soundController.playSoundEffect("res/sounds/soundeffects/run.mp3", 350);
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.IDLE_SLASH ||
                        entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.RUN_SLASH ||
                        entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.JUMP_SLASH ||
                        entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.FALL_SLASH) {
                    soundController.playSoundEffect("res/sounds/soundeffects/attack.mp3", 650);
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.DASH) {
                    soundController.playSoundEffect("res/sounds/soundeffects/dash.mp3", 350);
                    return;
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.JUMP) {
                    soundController.playSoundEffect("res/sounds/soundeffects/jump.mp3", 350);
                    return;
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.FALL && entityController.getPlayer().getAirMovement() > 5.5f && entityController.getPlayer().getAirMovement() < 6.0f) {
                    soundController.playSoundEffect("res/sounds/soundeffects/land.mp3", 350);
                }
            }
        }
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }


    //TODO: Maybe remove from gameController to a network class, but not quite sure
    public void useMultiplayer() {
        if (isPlayingMultiplayer) return;
        try {
            String fileContent = GitHubClient.readFile();
            if (fileContent.isEmpty()) {
                new Host().start();
                GitHubClient.writeFile(ipAddress);
            } else {
                if (!HostChecker.isServerRunning(fileContent)) {
                    new Host().start();
                    GitHubClient.writeFile(ipAddress);
                }
            }
            client = new Client(GitHubClient.readFile(), this);
            client.start();


        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        isPlayingMultiplayer = true;
    }

    public void saveControlsToJson(String filePath) {
        ControlScreen controlScreen = this.screenController.getControlScreen();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Control>>() {
        }.getType();
        List<Control> controlsList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : controlScreen.getControls().entrySet()) {
            controlsList.add(new Control(entry.getKey(), entry.getValue()));
        }
        String json = gson.toJson(controlsList, listType);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Map<String, Object>> loadControlsFromJson() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        FileReader reader = new FileReader("res/configsAndSaves/controls.json");
        List<Map<String, Object>> controls = gson.fromJson(reader, type);
        reader.close();
        return controls;
    }

    public void loadAndInitializeControls() {
        try {
            // Load the controls from the JSON file
            List<Map<String, Object>> controls = loadControlsFromJson();

            // Initialize the controls
            for (Map<String, Object> control : controls) {
                String name = (String) control.get("name");
                int keyCode = ((Double) control.get("keyCode")).intValue();

                // Check if the control name is null
                if (name == null) {
                    System.err.println("A control name is null. Skipping this control.");
                    continue;
                }

                // Convert the control name to a GameControls enum value
                GameControls gameControl = GameControls.valueOf(name);

                // Check if the gameControl is null
                if (gameControl == null) {
                    System.err.println("The control " + name + " does not exist in the GameControls enum. Skipping this control.");
                    continue;
                }

                // Set the key code of the game control
                gameControl.setKeyCode(keyCode);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while loading the controls from the JSON file: " + e.getMessage());
        }
    }

    public boolean getIsPlayingMultiplayer() {
        return isPlayingMultiplayer;
    }

    public void setIsPlayingMultiplayer(boolean isPlayingMultiplayer) {
        this.isPlayingMultiplayer = isPlayingMultiplayer;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public EntityController getEntityController() {
        return entityController;
    }

    public ItemController getItemController() {
        return itemController;
    }


}
