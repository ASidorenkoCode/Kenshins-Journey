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

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private GameObjectController gameObjectController;
    private MapController mapController;
    private ScreenController screenController;
    private ItemController itemController;

    private Highscore highscore;

    private GameState currentGameState;

    private boolean showHitbox;

    public GameController(boolean showHitBox) {
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

    public void update() {
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

    public void loadNewMap() {
        screenController.setFrame(gameView.getFrame());
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
        player.setTotalHearts(player.getTotalHearts() + 1);// AMOUNT OF hearts collected
        mapController.loadCurrentMapIndex(highscore);
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

    public void startGame() {
        if(currentGameState != GameState.START) return;
        currentGameState = GameState.PLAYING;
    }

    public void resetGame() {
        highscore.resetHighscore();
        highscore.writeHighscore();
        Player player = entityController.getPlayer();
        player.setTotalHearts(player.getTotalHearts() + 1);// AMOUNT OF hearts collected
        mapController.loadCurrentMapIndex(highscore);
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        entityController.initBoss(mapController, showHitbox);
        itemController.initItems(mapController);
        itemController.deleteAllItemsFromMenu();
        currentGameState = GameState.START;
    }

    public Highscore getHighscore() {
        return highscore;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }
}
