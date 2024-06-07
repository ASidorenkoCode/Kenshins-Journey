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
    private InterfaceGame interfaceGame;
    private DeathScreen deathScreen;
    private LoadingScreen loadingScreen;
    private StartScreen startScreen;

    public ScreenController(ItemController itemController) {
        this.interfaceGame = new InterfaceGame(itemController);
        this.startScreen = new StartScreen();
    }

    public void update(Highscore highscore, Player player, Item[] menu, ArrayList<ServerObject> serverObjects) {
        interfaceGame.update(highscore, player, menu, serverObjects);
    }


    public void displayDeathScreenIfPlayerIsDead(Player player, Highscore highscore, MapController mapController, GameObjectController gameObjectController) {
        //TODO: Should be deprecated
        if (player.isDead() && player.getDeathAnimationFinished()) {

            if (!deathScreen.isPlayerContinuesGame() && !deathScreen.isDisplayDeathScreenOnlyOnce()) {
                deathScreen.displayDeathScreen();
            }
            if (deathScreen.isPlayerContinuesGame() && deathScreen.isDisplayDeathScreenOnlyOnce()) {
                //TODO: no game logic in screen controller
                loadingScreen.displayLoadingScreen();
                player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
                gameObjectController.updatePoints(mapController, true);
                player.resetHealth();
                player.resetDeath();
                deathScreen.setDisplayDeathScreenOnlyOnce(false);
                highscore.decreaseHighscoreForDeath();
                highscore.increaseDeathCounter();
            }
        }
    }

    public void draw(Graphics g, GameState currentGameState, int highscore, int deathCounter, int mapOffsetX, String playerId, int currentLevel) {
        switch (currentGameState) {
            //TODO implement start and death screen
            case START -> startScreen.draw(g);
            case LOADING, END -> drawLoadingScreen(g);
            case DEAD -> drawDeathScreen(g, highscore, deathCounter);
            case PLAYING -> interfaceGame.draw(g, mapOffsetX, playerId, currentLevel);
        }

    }

    private void drawDeathScreen(Graphics g, int highscore, int deathCounter) {
        Graphics2D g2d = (Graphics2D) g;

        // Background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        // Font settings
        Font gameOverFont = new Font("Arial", Font.BOLD, 40);
        Font statsFont = new Font("Arial", Font.PLAIN, 20);
        Font restartFont = new Font("Arial", Font.BOLD, 25);

        // Game Over Text
        String gameOverText = "YOU DIED!";
        int gameOverX = (GameView.GAME_WIDTH - g2d.getFontMetrics(gameOverFont).stringWidth(gameOverText)) / 2;
        int gameOverY = GameView.GAME_HEIGHT / 3;

        g2d.setColor(Color.RED);
        g2d.setFont(gameOverFont);
        g2d.drawString(gameOverText, gameOverX, gameOverY);

        // Game Statistics
        String scoreText = STR."Your current Score is \{highscore} points";
        String deaths = STR."You currently died \{deathCounter + 1} times!";

        int statsX = (GameView.GAME_WIDTH - g2d.getFontMetrics(statsFont).stringWidth(scoreText)) / 2;
        int scoreY = gameOverY + 100;
        int enemiesY = scoreY + 30;

        g2d.setColor(Color.WHITE);
        g2d.setFont(statsFont);
        g2d.drawString(scoreText, statsX, scoreY);
        g2d.drawString(deaths, statsX, enemiesY);

        String restartTextLine1 = "Press L to Respawn!";
        String restartTextLine2 = "You will lose 100 score points on respawn!";
        int restartYLine1 = enemiesY + 100;
        int restartYLine2 = restartYLine1 + 30;

        int restartXLine1 = (GameView.GAME_WIDTH - g2d.getFontMetrics(restartFont).stringWidth(restartTextLine1)) / 2;
        int restartXLine2 = (GameView.GAME_WIDTH - g2d.getFontMetrics(restartFont).stringWidth(restartTextLine2)) / 2;

        g2d.setFont(restartFont);
        g2d.drawString(restartTextLine1, restartXLine1, restartYLine1);
        g2d.drawString(restartTextLine2, restartXLine2, restartYLine2);
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

    public InterfaceGame getInterfaceGame() {
        return interfaceGame;
    }
}
