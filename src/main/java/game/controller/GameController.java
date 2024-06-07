package game.controller;

import entities.controller.EntityController;
import entities.logic.Player;
import game.UI.GameView;
import game.logic.GameEngine;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import gameObjects.logic.Finish;
import items.controller.ItemController;
import maps.controller.MapController;
import network.ServerObject;
import network.SharedData;
import screens.controller.ScreenController;
import screens.ui.DeathScreen;

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

    private Highscore highscore;
    private String playerId;
    private Player.PlayerSerializer playerSerializer;

    private GameState currentGameState;
    private long comparingTime;
    private ArrayList<ServerObject> serverObjects;

    private boolean showHitbox;

    public GameController(boolean showHitBox) throws IOException {
        comparingTime = System.currentTimeMillis();
        serverObjects = new ArrayList<>();
        //controller
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            playerId = inetAddress.getHostName();
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

    public void update() throws IOException {
        if(currentGameState == GameState.PLAYING) {

            if (!SharedData.networkToGameQueue.isEmpty()) {
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
                return;
            }
            entityController.update(mapController, gameObjectController, highscore);
            itemController.update(entityController);
            highscore.decreaseHighScoreAfterOneSecond();
            screenController.update(highscore, entityController.getPlayer(), itemController.getMenu(), serverObjects);

            if (SharedData.gameToNetworkQueue.isEmpty()) {
                try {
                    SharedData.gameToNetworkQueue.put(new ServerObject(highscore, entityController.getPlayer(), playerId));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public DeathScreen getDeathScreen() {
        //TODO: do not handle this here
        return screenController.getDeathScreen();
    }

    public void loadNewMap() throws IOException {
        currentGameState = GameState.LOADING;
        //highscore update
        highscore.addCurrentHighscoreToList();
        highscore.increaseHighscoreForItems(itemController.getMenu());
        highscore.writeHighscore();


        //handle option of game is finished
        if(mapController.getMaps().size() == highscore.getAllHighscores().size()) {
            //Game is finished
            resetGame();
        }

        Player player = entityController.getPlayer();
        playerSerializer.writePlayer(player);
        initOrUpdateGame();
        currentGameState = GameState.PLAYING;
    }

    private void initOrUpdateGame() throws IOException {
        mapController.loadCurrentMapIndex(highscore);
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        entityController.initBoss(mapController, showHitbox);
        itemController.initItems(mapController);
        itemController.deleteAllItemsFromMenu();
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
        highscore.resetHighscore();
        highscore.writeHighscore();
        Player player = entityController.getPlayer();
        Player.PlayerSerializer.writePlayer(player);
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
}
