package screens;

import entities.logic.Player;
import game.UI.GameView;
import items.controller.ItemController;
import items.logic.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InterfaceGame {
    private BufferedImage fullHeart;
    private BufferedImage halfHeart;
    private BufferedImage emptyHeart;
    private BufferedImage playerPortrait;
    private int playerHealth;
    private int score;
    private long lastTime;
    private int totalHearts;
    private int heightJump;
    private ItemController itemController;
    private BufferedImage characterPortraitAndStats;


    public InterfaceGame(Player player, ItemController itemController) {
        try {
            BufferedImage healthbarPlayer = ImageIO.read(new File("res/healthPlayer/healthbarPlayer.png"));
            fullHeart = healthbarPlayer.getSubimage(0, 0, 64, 64);
            halfHeart = healthbarPlayer.getSubimage(64, 0, 64, 64);
            emptyHeart = healthbarPlayer.getSubimage(128, 0, 64, 64);
            characterPortraitAndStats = ImageIO.read(new File("res/interfacePlayer/character_portrait_and_stats.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerHealth = player.getPlayerHealth();
        totalHearts = player.getTotalHearts();
        score = 5000;
        lastTime = System.currentTimeMillis();
        this.itemController = itemController;
    }

    public void draw(Graphics g, Player player) {
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
        int squareX = -1;
        int squareY = -1;

        for (int i = 0; i < characterPortraitAndStats.getWidth(); i++) {
            for (int j = 0; j < characterPortraitAndStats.getHeight(); j++) {
                if (characterPortraitAndStats.getRGB(i, j) == Color.decode("#37946e").getRGB()) {
                    characterPortraitAndStats.setRGB(i, j, Color.decode("#544535").getRGB());

                    squareX = i;
                    squareY = j;
                }
            }
        }

        int x = 10;
        int y = GameView.GAME_HEIGHT - (int) (characterPortraitAndStats.getHeight() * 1.5) - 20;
        g.drawImage(characterPortraitAndStats, x, y, (int) (characterPortraitAndStats.getWidth() * 1.5), (int) (characterPortraitAndStats.getHeight() * 1.5), null);

        int totalX = x + (int) (squareX * 1.5);
        int totalY = y + (int) (squareY * 1.5);

        String health = String.format("%d", player.getPlayerHealth());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(health);
        int textHeight = fm.getHeight();

        int textX = totalX + 90 + (int) (characterPortraitAndStats.getWidth() * 1.5) / 2 - textWidth / 2;
        int textY = totalY - 45 + (int) (characterPortraitAndStats.getHeight() * 1.5) / 2 + textHeight / 2;
        g.drawString(health, textX, textY);

        drawScore(g, score);

        int imageX = 500;
        int imageY = GameView.GAME_HEIGHT - characterPortraitAndStats.getHeight() * 2 - 20;
        for (Item i : itemController.getMenu()) {
            //TODO: Improve Menu style
            if (i != null)
                //TODO: use draw function() in item controller instead of getting itemUI -> classes should be replaceable
                itemController.getItemUI().drawStaticItemImage(g, i, imageX, imageY, itemController.getAnimations());
            imageX += 100;
        }
    }

    public void updatePlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void updateHighscore() {
        if (System.currentTimeMillis() - lastTime >= 1000 && score > 0) {
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

    public void increaseScore(int increment) {
        score += increment;
    }


}
