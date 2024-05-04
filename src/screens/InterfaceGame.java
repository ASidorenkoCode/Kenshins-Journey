package screens;

import entities.logic.Player;
import game.UI.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InterfaceGame {
    private BufferedImage fullHeart;
    private BufferedImage halfHeart;
    private BufferedImage emptyHeart;
    private int playerHealth;
    private int score;
    private long lastTime;
    private int totalHearts;

    public InterfaceGame(Player player) {
        try {
            BufferedImage healthbarPlayer = ImageIO.read(new File("res/healthPlayer/healthbarPlayer.png"));
            fullHeart = healthbarPlayer.getSubimage(0, 0, 64, 64);
            halfHeart = healthbarPlayer.getSubimage(64, 0, 64, 64);
            emptyHeart = healthbarPlayer.getSubimage(128, 0, 64, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerHealth = player.getPlayerHealth();
        totalHearts = (int) (playerHealth * 0.5);
        score = 10;
        lastTime = System.currentTimeMillis();
    }

    public void draw(Graphics g) {
        int maximumHeartsPerRow = 10;
        for (int i = 0; i < totalHearts; i++) {
            Image heart;
            if (playerHealth > i * 2 + 1) {
                heart = fullHeart;
            } else if (playerHealth > i * 2) {
                heart = halfHeart;
            } else {
                heart = emptyHeart;
            }

            int x = (i % maximumHeartsPerRow) * 32 - 32;
            int y = (i / maximumHeartsPerRow) * 32 - 32;

            g.drawImage(heart, x, y, 150, 150, null);
        }
        drawScore(g, score);
    }

    public void updatePlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void updateHighscore() {
        if(System.currentTimeMillis() - lastTime >= 1000 && score > 0) {
            score--;
            lastTime = System.currentTimeMillis();
        }
    }

    private void drawScore(Graphics g, int score) {
        String scoreText = "Score: " + score;
        int x = GameView.GAME_WIDTH / 2;
        int y = 50;

        g.setColor(Color.WHITE); // Set the color to white
        g.setFont(new Font("Calibri", Font.BOLD, 32)); // Set the font to Calibri and size to 32px
        g.drawString(scoreText, x, y); // Draw the string in the center
        g.setColor(Color.BLACK); // Reset the color to black
        g.setFont(new Font("Calibri", Font.BOLD, 12)); // Reset the font size to default
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalHearts(int totalHearts) {
        this.totalHearts = totalHearts;
    }
}
