package screens.controller;

import entities.logic.Player;
import game.UI.GameView;
import game.controller.GameState;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import items.controller.ItemController;
import items.logic.Item;
import maps.controller.MapController;
import screens.ui.DeathScreen;
import screens.ui.InterfaceGame;
import screens.ui.LoadingScreen;

import javax.swing.*;
import java.awt.*;

public class ScreenController {
    private InterfaceGame interfaceGame;
    private DeathScreen deathScreen;
    private LoadingScreen loadingScreen;

    public ScreenController(ItemController itemController) {
        this.interfaceGame = new InterfaceGame(itemController);
    }

    public void update(Highscore highscore, Player player, Item[] menu) {
        interfaceGame.update(highscore, player, menu);
    }

    public void displayDeathScreenIfPlayerIsDead(Player player, Highscore highscore, MapController mapController, GameObjectController gameObjectController) {
        if (player.isDead() && player.getDeathAnimationFinished()) {

            if (!deathScreen.isPlayerContinuesGame() && !deathScreen.isDisplayDeathScreenOnlyOnce()) {
                deathScreen.displayDeathScreen();
            }
            if (deathScreen.isPlayerContinuesGame() && deathScreen.isDisplayDeathScreenOnlyOnce()) {
                //TODO: no game logic in screen controller
                loadingScreen.displayLoadingScreen();
                player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
                gameObjectController.updatePoints(mapController);
                player.resetHealth();
                player.resetDeath();
                deathScreen.setDisplayDeathScreenOnlyOnce(false);
                highscore.decreaseHighscoreForDeath();
                highscore.increaseDeathCounter();
            }
        }
    }

    public void draw(Graphics g, GameState currentGameState) {
        switch (currentGameState) {
            //TODO implement start and death screen
            case START, LOADING, END -> drawLoadingScreen(g);
            case DEAD -> drawDeathScreen(g);
            case PLAYING -> interfaceGame.draw(g);
        }

    }
    private void drawDeathScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        // Font settings
        Font gameOverFont = new Font("Arial", Font.BOLD, 40);
        Font statsFont = new Font("Arial", Font.PLAIN, 20);
        Font restartFont = new Font("Arial", Font.BOLD, 25);

        FontMetrics gameOverMetrics = g2d.getFontMetrics(gameOverFont);
        FontMetrics statsMetrics = g2d.getFontMetrics(statsFont);
        FontMetrics restartMetrics = g2d.getFontMetrics(restartFont);

        // Game Over Text
        String gameOverText = "GAME OVER";
        int gameOverX = (GameView.GAME_WIDTH - gameOverMetrics.stringWidth(gameOverText)) / 2;
        int gameOverY = GameView.GAME_HEIGHT / 4;

        g2d.setColor(Color.RED);
        g2d.setFont(gameOverFont);
        g2d.drawString(gameOverText, gameOverX, gameOverY);

        // Game Statistics
        String scoreText = "Score: 12345";
        String highScoreText = "High Score: 67890";
        String enemiesText = "Enemies Defeated: 50";

        int statsX = (GameView.GAME_WIDTH - statsMetrics.stringWidth(scoreText)) / 2;
        int scoreY = gameOverY + 50;
        int highScoreY = scoreY + 30;
        int enemiesY = highScoreY + 30;

        g2d.setColor(Color.WHITE);
        g2d.setFont(statsFont);
        g2d.drawString(scoreText, statsX, scoreY);
        g2d.drawString(highScoreText, statsX, highScoreY);
        g2d.drawString(enemiesText, statsX, enemiesY);

        String restartText = "Press L to Restart";
        int restartX = (GameView.GAME_WIDTH - restartMetrics.stringWidth(restartText)) / 2;
        int restartY = GameView.GAME_HEIGHT * 3 / 4;

        g2d.setFont(restartFont);
        g2d.drawString(restartText, restartX, restartY);
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
}
