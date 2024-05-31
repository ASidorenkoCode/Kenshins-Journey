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
import screens.ui.InterfaceGame;
import screens.ui.LoadingScreen;
import screens.StartScreen;

public class GameController implements ReloadGame {

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
        mapController = new MapController(null);
        entityController = new EntityController(mapController, showHitBox);
        mapController.setEntityController(entityController);
        itemController = new ItemController(mapController, showHitBox);
        gameEngine = new GameEngine( this);
        gameObjectController = new GameObjectController(mapController, showHitBox);
        screenController = new ScreenController(itemController);

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
        entityController.update(this, mapController, gameObjectController, highscore, loadingScreen, deathScreen);
        itemController.update(entityController);
        highscore.decreaseHighScoreAfterOneSecond();
        screenController.update(highscore, entityController.getPlayer(), itemController.getMenu());
    }

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    @Override
    public void loadNewMap() {
        Player player = entityController.getPlayer();
        player.setTotalHearts(player.getTotalHearts() + 1);  // AMOUNT OF hearts collected
        loadingScreen.displayLoadingScreen();
        loadingScreen.updateScore(highscore.getCurrentHighscore());
        deathScreen.updateScore(highscore.getCurrentHighscore());
        mapController.loadNextMap();
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        itemController.initItems(mapController);
        highscore.increaseHighscoreForItems(itemController.getMenu());
        itemController.deleteAllItemsFromMenu();
        highscore.addCurrentHighscoreToList();

    }
}
