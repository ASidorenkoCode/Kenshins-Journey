package game.controller;

import entities.controller.EntityController;
import entities.logic.Player;
import entities.ui.PlayerUI;
import game.UI.GameView;
import game.logic.GameEngine;

import java.awt.event.KeyEvent;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;

    private EntityController entityController;

    public GameController(boolean showFPS_UPS, boolean showHitBox) {
        entityController = new EntityController(showHitBox);
        gameEngine = new GameEngine(showFPS_UPS, this, entityController);
        gameView = new GameView(this, entityController);
        gameView.gameWindow();
        gameEngine.startGameLoop();

    }

    public void showFPS_UPS() {
        gameView.showFPS_UPS(gameEngine.getFrames(), gameEngine.getUpdates());
    }

    public void callRepaint() {
        gameView.repaint();
    }

}
