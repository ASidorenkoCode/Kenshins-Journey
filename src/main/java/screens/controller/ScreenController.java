package screens.controller;

import entities.logic.Player;
import game.controller.GameState;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import screens.ui.DeathScreen;
import screens.ui.InterfaceGame;
import screens.ui.LoadingScreen;
import screens.ui.StartScreen;

import javax.swing.*;
import java.awt.*;

public class ScreenController {
    private JFrame frame;
    private InterfaceGame interfaceGame;
    private DeathScreen deathScreen;
    private LoadingScreen loadingScreen;
    private StartScreen startScreen;

    public ScreenController(ItemController itemController) {
        this.interfaceGame = new InterfaceGame(itemController);
        this.startScreen = new StartScreen();
        this.loadingScreen = new LoadingScreen();
        this.deathScreen = new DeathScreen();
    }

    public void update(Highscore highscore, Player player, Item[] menu) {
        interfaceGame.update(highscore, player, menu);
    }

    public void draw(Graphics g, GameState currentGameState, int highscore, int deathCounter) {
        switch (currentGameState) {
            //TODO implement start and death screen
            case START -> startScreen.draw(g);
            case LOADING, END -> {
                loadingScreen.setFrame(frame);
                loadingScreen.displayLoadingScreen();
            }
            case DEAD -> deathScreen.draw(g, highscore, deathCounter);
            case PLAYING -> interfaceGame.draw(g);
        }

    }

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }


}
