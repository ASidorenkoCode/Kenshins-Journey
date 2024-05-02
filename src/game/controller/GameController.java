package game.controller;

import entities.controller.EntityController;
import entities.logic.Player;
import entities.ui.PlayerUI;
import game.UI.GameView;
import game.logic.GameEngine;
import maps.UI.MapUI;
import maps.controller.MapController;
import screens.LoadingScreen;

import java.awt.event.KeyEvent;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private MapController mapController;
    private LoadingScreen loadingScreen;

    public GameController(boolean showFPS_UPS, boolean showHitBox) {
        mapController = new MapController(null);
        entityController = new EntityController(showHitBox, mapController.getCurrentPlayerSpawn(), mapController.getCurrentFinishSpawn());
        mapController.setEntityController(entityController);
        gameEngine = new GameEngine(showFPS_UPS, this);
        gameView = new GameView(this, entityController, mapController);
        gameView.gameWindow();
        gameEngine.startGameLoop();
        this.loadingScreen = new LoadingScreen(gameView.getFrame());

    }

    public void showFPS_UPS() {
        gameView.showFPS_UPS(gameEngine.getFrames(), gameEngine.getUpdates());
    }

    public void callRepaint() {
        gameView.repaint();
    }

    public void update() {
        entityController.update(mapController, loadingScreen);
    }

}
