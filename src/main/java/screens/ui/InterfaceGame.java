package screens.ui;

import entities.logic.Player;
import game.UI.GameView;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InterfaceGame {
    private int playerHealth;
    private int playerCurrentDamagePerAttack;
    private int score;
    private Item[] menu;
    private ItemController itemController;
    private BufferedImage characterPortraitAndStats;


    public InterfaceGame(ItemController itemController) {
        try {
            characterPortraitAndStats = ImageIO.read(new File("res/interfacePlayer/character_portrait_and_stats.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: remove itemController
        this.itemController = itemController;
    }

    public void draw(Graphics g) {
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

        int x = 0;
        int y = 0;
        g.drawImage(characterPortraitAndStats, x, y, (int) (characterPortraitAndStats.getWidth() * 1.5), (int) (characterPortraitAndStats.getHeight() * 1.5), null);

        int totalX = x + (int) (squareX * 1.5);
        int totalY = y + (int) (squareY * 1.5);

        String health = String.format("%d", playerHealth);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(health);
        int textHeight = fm.getHeight();

        int textX = totalX + 90 + (int) (characterPortraitAndStats.getWidth() * 1.5) / 2 - textWidth / 2;
        int textY = totalY - 45 + (int) (characterPortraitAndStats.getHeight() * 1.5) / 2 + textHeight / 2;
        g.drawString(health, textX, textY);


        for (int i = 0; i < characterPortraitAndStats.getWidth(); i++) {
            for (int j = 0; j < characterPortraitAndStats.getHeight(); j++) {
                if (characterPortraitAndStats.getRGB(i, j) == Color.decode("#ac3232").getRGB()) {
                    characterPortraitAndStats.setRGB(i, j, Color.decode("#544535").getRGB());

                    squareX = i;
                    squareY = j;
                }
            }
        }

        String maxDamage = String.valueOf(playerCurrentDamagePerAttack);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(maxDamage);
        textHeight = fm.getHeight();

        textX = totalX + 90 + (int) (characterPortraitAndStats.getWidth() * 1.5) / 2 - textWidth / 2;
        textY = totalY - 12 + (int) (characterPortraitAndStats.getHeight() * 1.5) / 2 + textHeight / 2;
        g.drawString(maxDamage, textX, textY);

        drawScore(g, score);

        //TODO: remove itemController
        int itemHeight = itemController.getItemUI().getSPRITE_PX_HEIGHT() + 50;

        int imageY = GameView.GAME_HEIGHT - itemHeight;

        int totalWidth = menu.length * 55;
        int startX = (GameView.GAME_WIDTH - totalWidth) / 2;

        if (menu.length > 0) {
            int index = 1;
            for (Item i : menu) {
                if (i != null) {
                    g.setColor(Color.BLACK);
                    g.fillOval(startX, imageY + 10, itemHeight - 20, itemHeight + 20);
                    //TODO: remove itemController
                    itemController.getItemUI().drawStaticItemImage(g, i, startX, imageY - 20, itemController.getAnimations());

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.drawString(String.valueOf(index), startX + 27, imageY + itemHeight - 10);
                }

                startX += 55;
                index++;
            }
        }
    }

    public void update(Highscore highscore, Player player, Item[] menu) {
        score = highscore.getCurrentHighscore();
        playerCurrentDamagePerAttack = player.getCurrentDamagePerAttack();
        playerHealth = player.getHealth();
        this.menu = menu;
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
}
