package game.controller;

import entities.logic.Player;
import entities.ui.PlayerUI;
import game.UI.GameView;
import game.logic.GameEngine;

import java.awt.event.KeyEvent;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;

    private PlayerUI playerUI;
    private Player player;

    public GameController(boolean showFPS_UPS) {
        player = new Player(200,200);
        //TODO: Improve TileScale
        playerUI = new PlayerUI(player, 2f);
        gameEngine = new GameEngine(showFPS_UPS, this, player);
        gameView = new GameView(this, playerUI);
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
