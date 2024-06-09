package screens.controller;

import entities.logic.Player;
import game.UI.GameView;
import game.controller.GameState;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import network.data.ServerObject;
import screens.ui.*;

import javax.swing.*;
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

    public void update(Highscore highscore, Player player, Item[] menu, ArrayList<ServerObject> serverObjects) {
        interfaceGame.update(highscore, player, menu, serverObjects);
    }

    public void draw(Graphics g, GameState currentGameState, int highscore, int deathCounter, Highscore highscores, int mapCount, String playerId, int currentLevel, boolean isPlayingMultiplayer) {
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
            case PLAYING -> interfaceGame.draw(g, playerId, currentLevel, isPlayingMultiplayer);
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

    public void displayLoadingScreen() {
        loadingScreen.displayLoadingScreen();
    }

    public void setMapCountEndScreen(int mapCount) {
        endScreen.setMapCount(mapCount);
    }

    public void setMapCountHighScoreScreen(int mapCount) {
        highScoreScreen.setMapCount(mapCount);
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public InterfaceGame getInterfaceGame() {
        return interfaceGame;
    }
}
