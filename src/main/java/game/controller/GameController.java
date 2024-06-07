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
import screens.controller.ScreenController;
import screens.ui.DeathScreen;

import java.io.IOException;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private GameObjectController gameObjectController;
    private MapController mapController;
    private ScreenController screenController;
    private ItemController itemController;

    private Highscore highscore;
    private Player.PlayerSerializer playerSerializer;

    private GameState currentGameState;

    private boolean showHitbox;

    public GameController(boolean showHitBox) throws IOException {
        //controller
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
            screenController.update(highscore, entityController.getPlayer(), itemController.getMenu());

        }
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
        if(mapController.getMaps().size() == highscore.getAllHighscores().size()) {
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

    public void resetGame() throws IOException{
        if (currentGameState != GameState.END) return;
        highscore.writeAllHighscores();
        highscore.resetHighscore();
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
}
