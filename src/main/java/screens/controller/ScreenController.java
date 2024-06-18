package screens.controller;

import entities.logic.Player;
import game.controller.GameState;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import network.data.ServerObject;
import screens.ui.*;

import java.awt.*;
import java.util.ArrayList;

public class ScreenController {
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
            case PLAYING -> {
                interfaceGame.draw(g, playerId, currentLevel, isPlayingMultiplayer);
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

    public InterfaceGame getInterfaceGame() {
        return interfaceGame;
    }
}
