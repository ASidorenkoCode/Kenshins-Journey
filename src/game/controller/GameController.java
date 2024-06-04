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
import screens.ui.LoadingScreen;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private GameObjectController gameObjectController;
    private MapController mapController;
    private ScreenController screenController;
    private LoadingScreen loadingScreen;
    private ItemController itemController;
    private DeathScreen deathScreen;

    private Highscore highscore;

    private boolean showHitbox;

    public GameController(boolean showHitBox) {
        //controller
        this.highscore = Highscore.readHighscore();
        mapController = new MapController(null, highscore);
        entityController = new EntityController(mapController, showHitBox);
        mapController.setEntityController(entityController);
        itemController = new ItemController(mapController, showHitBox);
        gameEngine = new GameEngine( this);
        gameObjectController = new GameObjectController(mapController, showHitBox);
        screenController = new ScreenController(itemController);


        //ui
        gameView = new GameView(this, entityController, mapController, itemController, gameObjectController, screenController);
        this.deathScreen = new DeathScreen(gameView.getFrame());
        this.loadingScreen = new LoadingScreen(gameView.getFrame());
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
        Player player = entityController.getPlayer();
        if (player.isDead() && player.getDeathAnimationFinished()) {

            if(deathScreen.getTotalScore() > highscore.getCurrentHighscore()) deathScreen.updateScore(highscore.getCurrentHighscore());
            if (!deathScreen.isPlayerContinuesGame() && !deathScreen.isDisplayDeathScreenOnlyOnce()) {
                deathScreen.displayDeathScreen();
            }
            if (deathScreen.isPlayerContinuesGame() && deathScreen.isDisplayDeathScreenOnlyOnce()) {
                loadingScreen.displayLoadingScreen();
                player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
                gameObjectController.updatePoints(mapController);
                player.resetHealth();
                player.resetDeath();
                deathScreen.setDisplayDeathScreenOnlyOnce(false);
                highscore.decreaseHighscoreForDeath();
                highscore.increaseDeathCounter();
            }
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

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    public void loadNewMap() {
        //highscore updates
        highscore.addCurrentHighscoreToList();
        highscore.writeHighscore();
        highscore.increaseHighscoreForItems(itemController.getMenu());

        Player player = entityController.getPlayer();
        player.setTotalHearts(player.getTotalHearts() + 1);// AMOUNT OF hearts collected
        loadingScreen.displayLoadingScreen();
        loadingScreen.updateScore(highscore.getCurrentHighscore());
        deathScreen.updateScore(highscore.getCurrentHighscore());
        mapController.loadCurrentMapIndex(highscore);
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        itemController.initItems(mapController);
        itemController.deleteAllItemsFromMenu();


    }
}
