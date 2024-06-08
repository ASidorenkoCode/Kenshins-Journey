package screens.controller;

import entities.logic.Player;
import game.UI.GameView;
import game.controller.GameState;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import items.controller.ItemController;
import items.logic.Item;
import maps.controller.MapController;
import network.ServerObject;
import screens.ui.DeathScreen;
import screens.ui.InterfaceGame;
import screens.ui.LoadingScreen;
import screens.ui.StartScreen;

import java.awt.*;
import java.util.ArrayList;

public class ScreenController {
    private JFrame frame;
    private InterfaceGame interfaceGame;
    private DeathScreen deathScreen;
    private LoadingScreen loadingScreen;
    private StartScreen startScreen;
    private EndScreen endScreen;
    private HighScoreScreen highScoreScreen;

    public ScreenController(ItemController itemController) {
        this.interfaceGame = new InterfaceGame(itemController);
        this.startScreen = new StartScreen();
        this.loadingScreen = new LoadingScreen();
        this.deathScreen = new DeathScreen();
        this.endScreen = new EndScreen();
        this.highScoreScreen = new HighScoreScreen();
    }

    public void update(Highscore highscore, Player player, Item[] menu) {
        interfaceGame.update(highscore, player, menu);
    }

    public void draw(Graphics g, GameState currentGameState, int highscore, int deathCounter, Highscore highscores, int mapCount) {
        switch (currentGameState) {
            //TODO implement start and death screen
            case START -> startScreen.draw(g);
            case LOADING -> {
                loadingScreen.setFrame(frame);
                loadingScreen.displayLoadingScreen();
            }
            case END -> {
                setMapCountEndScreen(mapCount);
                endScreen.draw(g, highscores);
                highscores.deleteHighscoreFile();
            }
            case DEAD -> deathScreen.draw(g, highscore, deathCounter);
            case PLAYING -> interfaceGame.draw(g);
            case HIGHSCORE -> {
                setMapCountHighScoreScreen(mapCount);
                highscores.findBestHighscores();
                highScoreScreen.draw(g, highscores);
            }
        }
    }
    private void drawLoadingScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        // Progress bar background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(100, 150, 400, 50);

        // Progress bar foreground
        g.setColor(Color.GREEN);
        g.fillRect(100, 150, 1 * 4, 50);

        // Game Information
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Loading Game...", 250, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Tip: Collect coins to increase your score!", 150, 250);
        g.drawString("Tip: Avoid enemies to stay alive!", 150, 280);
    }

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    public void displayDeathScreen() {
        deathScreen.displayDeathScreen();
    }

    public void displayLoadingScreen() {
        loadingScreen.displayLoadingScreen();
    }
}
