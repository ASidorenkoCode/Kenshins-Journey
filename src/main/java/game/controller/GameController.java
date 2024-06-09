package game.controller;

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
import maps.controller.MapController;
import network.*;
import screens.controller.ScreenController;
import screens.ui.DeathScreen;
import sound.SoundController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
        gameEngine = new GameEngine( this);
        gameObjectController = new GameObjectController(mapController, showHitBox);
        screenController = new ScreenController(itemController);
        soundController = new SoundController();
        gameView = new GameView(this, entityController, mapController, itemController, gameObjectController, screenController);




        //ui
        gameView.gameWindow();
        gameEngine.startGameLoop();
        this.showHitbox = showHitBox;
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
            if(player.isDead()) {
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
            }

        }
        switchSound();
    }

    public DeathScreen getDeathScreen() {
        //TODO: do not handle this here
        return screenController.getDeathScreen();
    }

    public void loadNewMap() throws IOException {
        screenController.setFrame(gameView.getFrame());
        currentGameState = GameState.LOADING;
        //highscore update
        highscore.addCurrentHighscoreToList();
        highscore.increaseHighscoreForItems(itemController.getMenu());
        highscore.writeHighscore();


        //handle option of game is finished
        if (mapController.getMaps().size() == highscore.getAllHighscores().size()) {
            currentGameState = GameState.END;
            return;
        }

        Player player = entityController.getPlayer();
        playerSerializer.writePlayer(player);
        initOrUpdateGame();
        currentGameState = GameState.PLAYING;
    }

    private void initOrUpdateGame() throws IOException {
        mapController.loadCurrentMapIndex(highscore.getAllHighscores().size());
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        entityController.initBoss(mapController, showHitbox);
        itemController.initItems(mapController);
        itemController.deleteAllItemsFromMenu();
        currentGameState = GameState.PLAYING;
    }

    public void restartLevelAfterDeath() {
        if(currentGameState != GameState.DEAD) return;
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
        if(currentGameState != GameState.START) return;
        Player player = entityController.getPlayer();
        int loadedHealth = playerSerializer.readPlayerHealth();
        if (loadedHealth != -1) {
            player.setHealth(loadedHealth);
        }
        currentGameState = GameState.PLAYING;
    }

    public void resetGame() throws IOException {
        if (currentGameState != GameState.END) return;
        highscore.writeAllHighscores();
        highscore.resetHighscore();
        highscore.writeHighscore();
        Player player = entityController.getPlayer();
        player.resetHealth();
        initOrUpdateGame();
        currentGameState = GameState.START;
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
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.RUN) {
                    soundController.playSoundEffect("res/sounds/soundeffects/run.mp3");
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.IDLE_SLASH ||
                        entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.RUN_SLASH ||
                        entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.JUMP_SLASH ||
                        entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.FALL_SLASH) {
                    soundController.playSoundEffect("res/sounds/soundeffects/attack.mp3");
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.DASH) {
                    soundController.playSoundEffect("res/sounds/soundeffects/dash.mp3");
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.JUMP) {
                    soundController.playSoundEffect("res/sounds/soundeffects/jump.mp3");
                }
                if (entityController.getPlayerUI().getCurrentAnimation() == PlayerAnimations.FALL) {
                    soundController.playSoundEffect("res/sounds/soundeffects/land.mp3");
                }
            }
        }
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    public void quitGame() {
        if (isPlayingMultiplayer) return;
        client.playerQuitsGame();
    }

    public void useMultiplayer() {
        if (isPlayingMultiplayer) return;


        try {
            if (!GitHubClient.hostIsRunning()) {
                new Host().start();
                GitHubClient.writeFile(ipAddress);
            }
            client = new Client(GitHubClient.readFile());
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        isPlayingMultiplayer = true;
    }

    public boolean getIsPlayingMultiplayer() {
        return isPlayingMultiplayer;
    }
}
