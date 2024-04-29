package game.controller;

import game.UI.GameView;
import game.logic.GameEngine;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;

    public GameController(boolean showFPS_UPS) {
        gameEngine = new GameEngine(showFPS_UPS, this);
        gameView = new GameView(this);
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
