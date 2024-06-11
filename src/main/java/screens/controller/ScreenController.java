package screens.controller;

import entities.logic.Player;
import game.controller.GameState;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import screens.ui.*;

import javax.swing.*;
import java.awt.*;

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
                loadingScreen.startLoading();
                loadingScreen.draw(g);
            }
            case END -> {
                setMapCountEndScreen(mapCount);
                endScreen.draw(g, highscores);
                highscores.deleteHighscoreFile();
            }
            case DEAD -> deathScreen.draw(g, highscore, deathCounter);
            case PLAYING -> interfaceGame.draw(g, playerId, currentLevel, isPlayingMultiplayer);
            case PLAYING -> {
                loadingScreen.resetProgress();
                interfaceGame.draw(g);
            }
            case HIGHSCORE -> {
                setMapCountHighScoreScreen(mapCount);
                highscores.findBestHighscores();
                highScoreScreen.draw(g, highscores);
            }
        }
    }

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setMapCountEndScreen(int mapCount) {
        endScreen.setMapCount(mapCount);
    }

    public void setMapCountHighScoreScreen(int mapCount) {
        highScoreScreen.setMapCount(mapCount);
    }

    public StartScreen getStartScreen() {
        return startScreen;
    }

    public HighScoreScreen getHighScoreScreen() {
        return highScoreScreen;
    }

    public LoadingScreen getLoadingScreen() {
        return loadingScreen;
    }
}
