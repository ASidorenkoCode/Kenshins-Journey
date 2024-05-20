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


    public InterfaceGame(Player player, ItemController itemController) {
        try {
            BufferedImage healthbarPlayer = ImageIO.read(new File("res/healthPlayer/healthbarPlayer.png"));
            fullHeart = healthbarPlayer.getSubimage(0, 0, 64, 64);
            halfHeart = healthbarPlayer.getSubimage(64, 0, 64, 64);
            emptyHeart = healthbarPlayer.getSubimage(128, 0, 64, 64);
            playerPortrait = ImageIO.read(new File("res/kenshin/kenshin_portrait.png"));
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

        int borderSize = 20;
        int x = 10;
        int y = GameView.GAME_HEIGHT - playerPortrait.getHeight() * 2 - borderSize;
        int width = playerPortrait.getWidth() * 2;
        int height = playerPortrait.getHeight() * 2;

        g.drawImage(playerPortrait, x, y, width, height, null);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(borderSize));
        g2d.drawRect(0, y - borderSize / 2, width + borderSize, height + borderSize);

        g.drawImage(playerPortrait, x, y, width, height, null);

        int statsX = x + width + 20;
        int statsY = y + 20;
        int statsWidth = 200;
        int statsHeight = height + borderSize * 2;
        g.setColor(Color.BLACK);
        g.fillRect(statsX, statsY - 40, statsWidth, statsHeight);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 20));


        heightJump = (int) player.getAirMovement();
        String damage = "Damage: " + player.getMaximumDamagePerAttack();
        String movementSpeed = "Movement Speed: " + heightJump;
        String jumpHeight = "Jump Height: " + player.getAirMovement();

        g.drawString(damage, statsX, statsY);
        g.drawString(movementSpeed, statsX, statsY + 30);
        g.drawString(jumpHeight, statsX, statsY + 60);

        drawScore(g, score);

        int imageX = 500;
        int imageY = GameView.GAME_HEIGHT - playerPortrait.getHeight() * 2 - borderSize;
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
