package screens.controller;

import entities.logic.Player;
import game.controller.GameState;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import network.data.ServerObject;
import screens.ui.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ScreenController {
    private final InterfaceGame interfaceGame;
    private final DeathScreen deathScreen;
    private final LoadingScreen loadingScreen;
    private final StartScreen startScreen;
    private final EndScreen endScreen;
    private final HighScoreScreen highScoreScreen;
    private final ControlScreen controlScreen;


    public ScreenController(ItemController itemController) throws IOException {
        this.interfaceGame = new InterfaceGame(itemController);
        this.startScreen = new StartScreen();
        this.loadingScreen = new LoadingScreen();
        this.deathScreen = new DeathScreen();
        this.endScreen = new EndScreen();
        this.highScoreScreen = new HighScoreScreen();
        this.controlScreen = new ControlScreen();
    }

    public void update(Highscore highscore, Player player, Item[] menu, ArrayList<ServerObject> serverObjects) {
        interfaceGame.update(highscore, player, menu, serverObjects);
    }

    public void draw(Graphics g, GameState currentGameState, int highscore, int deathCounter, Highscore highscores, int mapCount, String playerId, int currentLevel, boolean isPlayingMultiplayer) throws IOException {
        switch (currentGameState) {
            case START -> {
                startScreen.draw(g);
            }
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
            case CONTROLS -> {
                controlScreen.drawControls(g);
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

    public ControlScreen getControlScreen() {
        return controlScreen;
    }
}
