package game.controller;

import entities.controller.EntityController;
import game.UI.GameView;
import game.logic.GameEngine;
import maps.controller.MapController;
import screens.LoadingScreen;
import screens.OptionScreen;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private MapController mapController;
    private LoadingScreen loadingScreen;
    private OptionScreen optionScreen;

    public GameController(boolean showFPS_UPS, boolean showHitBox) {
        mapController = new MapController(null);
        entityController = new EntityController(showHitBox,
                mapController.getCurrentPlayerSpawn(),
                mapController.getCurrentFinishSpawn(),
                mapController.getCurrentBigOrcSpawn(),
                mapController.getCurrentBigOrcRouteFinish());
        mapController.setEntityController(entityController);
        gameEngine = new GameEngine(showFPS_UPS, this);
        gameView = new GameView(this, entityController, mapController);
        this.loadingScreen = new LoadingScreen(gameView.getFrame());
        this.optionScreen = new OptionScreen(gameView, gameEngine);
        gameView.gameWindow();
        gameEngine.startGameLoop();
    }


    public void showFPS_UPS() {
        gameView.showFPS_UPS(gameEngine.getFrames(), gameEngine.getUpdates());
    }

    public void callRepaint() {
        gameView.repaint();
    }

    public void update() {
        entityController.update(mapController, loadingScreen);
        entityController.handleBigOrc(mapController, true, mapController.getCurrentBigOrcSpawn(), mapController.getCurrentBigOrcRouteFinish());
    }

}
