package screens.ui;

import entities.logic.Player;
import game.UI.GameView;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import network.data.ServerObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InterfaceGame {
    private int playerHealth;
    private int playerDeathCount;
    private int playerCurrentDamagePerAttack;
    private int score;
    private Item[] menu;
    private ItemController itemController;
    private ArrayList<ServerObject> serverObjects;
    private BufferedImage characterPortraitAndStats;

    private boolean isDrawingListOfCurrentPlayers;


    public InterfaceGame(ItemController itemController) {
        try {
            characterPortraitAndStats = ImageIO.read(new File("res/interfacePlayer/character_portrait_and_stats.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: remove itemController
        this.itemController = itemController;
        this.isDrawingListOfCurrentPlayers = false;
    }

    public void draw(Graphics g, String playerId, int currentLevel, boolean isPlayingMultiplayer) {

        int x = 0;
        int y = 0;
        g.drawImage(characterPortraitAndStats, x, y, (int) (characterPortraitAndStats.getWidth() * 1.5), (int) (characterPortraitAndStats.getHeight() * 1.5), null);

        drawTextAtColor(g, String.format("%d", playerHealth), "#37946e", "#544535", -45);
        drawTextAtColor(g, String.valueOf(playerCurrentDamagePerAttack), "#ac3232", "#544535", -12);
        drawTextAtColor(g, String.valueOf(playerDeathCount), "#fbf236", "#544535", 24);

        drawScore(g, score);

        //TODO: remove itemController
        int itemHeight = itemController.getItemUI().getSPRITE_PX_HEIGHT() + 50;

        int imageY = GameView.GAME_HEIGHT - itemHeight;

        if (menu.length > 0) {
            int index = 1;
            int totalWidth = menu.length * 55;
            int startX = (GameView.GAME_WIDTH - totalWidth) / 2;
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

        drawServerObjects(g, playerId, currentLevel);

        if (isDrawingListOfCurrentPlayers) drawListOfCurrentPlayers(g, isPlayingMultiplayer, playerId);
    }

    public void update(Highscore highscore, Player player, Item[] menu, ArrayList<ServerObject> serverObjects) {
        score = highscore.getCurrentHighscore();
        playerCurrentDamagePerAttack = player.getCurrentDamagePerAttack();
        playerHealth = player.getHealth();
        playerDeathCount = highscore.getDeathCounter();
        this.menu = menu;
        this.serverObjects = serverObjects;
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

    private void drawTextAtColor(Graphics g, String text, String colorToFind, String colorToReplace, int yOffset) {
        int squareX = -1;
        int squareY = -1;

        for (int i = 0; i < characterPortraitAndStats.getWidth(); i++) {
            for (int j = 0; j < characterPortraitAndStats.getHeight(); j++) {
                if (characterPortraitAndStats.getRGB(i, j) == Color.decode(colorToFind).getRGB()) {
                    characterPortraitAndStats.setRGB(i, j, Color.decode(colorToReplace).getRGB());

                    squareX = i;
                    squareY = j;
                }
            }
        }

        int totalX = (int) (squareX * 1.5);
        int totalY = (int) (squareY * 1.5) + yOffset;

        Color backGroundColor = Color.WHITE;
        g.setColor(backGroundColor);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int textX = totalX + 90 + (int) (characterPortraitAndStats.getWidth() * 1.5) / 2 - textWidth / 2;
        int textY = totalY + (int) (characterPortraitAndStats.getHeight() * 1.5) / 2 + textHeight / 2;
        g.drawString(text, textX, textY);
    }

    private void drawServerObjects(Graphics g, String playerId, int currentLevel) {
        for (ServerObject object : serverObjects) {
            if (object.getPlayerId().equals(playerId)) continue;
            if (currentLevel != object.getCurrentLevel()) continue;

            Graphics2D g2d = (Graphics2D) g;

            // Set anti-aliasing for smoother edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the triangle
            int[] xPoints = {(int) object.getHorizontalPlayerPosition(), (int) object.getHorizontalPlayerPosition() + 30, (int) object.getHorizontalPlayerPosition() + 15};
            int[] yPoints = {0, 0, 30};
            g2d.setColor(Color.BLUE);
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawListOfCurrentPlayers(Graphics g, boolean isPlayingMultiplayer, String hostname) {
        int padding = GameView.GAME_WIDTH / 8;
        int margin = GameView.GAME_HEIGHT / 15;
        int startX = padding + margin;
        int startY = padding + margin + GameView.GAME_HEIGHT / 20;
        int screenWidth = GameView.GAME_WIDTH - (2 * padding);
        int screenHeight = GameView.GAME_HEIGHT - (2 * padding);
        int columnLength = (screenWidth - margin) / 4;

        int lineHeight = GameView.HEIGHT / 28;
        int linePadding = GameView.GAME_HEIGHT / 30;

        g.setColor(new Color(255, 255, 255, 100));
        g.fillRoundRect(padding, padding, screenWidth, screenHeight, 30, 30);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, GameView.GAME_WIDTH / 30));

        if (!isPlayingMultiplayer) {
            g.drawString("No List available because you play offline", padding + margin, padding + margin);
            g.setFont(new Font("Arial", Font.PLAIN, GameView.GAME_WIDTH / 65));
            g.drawString("Press M to show data", padding + margin, startY);
            return;
        }


        g.drawString("Current Players", padding + margin, padding + margin);
        g.setFont(new Font("Arial", Font.PLAIN, GameView.GAME_WIDTH / 65));
        g.setColor(Color.BLACK);
        int columnPosition = margin;
        g.drawString("Player ID", padding + columnPosition, startY);
        columnPosition += columnLength;
        g.drawString("High Score", padding + columnPosition, startY);
        columnPosition += columnLength;
        g.drawString("Current Level", padding + columnPosition, startY);
        columnPosition += columnLength;
        g.drawString("Death Counter", padding + columnPosition, startY);

        startY += lineHeight + linePadding;


        for (int i = 0; i < serverObjects.size(); i++) {

            ServerObject object = serverObjects.get(i);
            columnPosition = margin;
            g.drawString((i + 1) + (object.getPlayerId().equals(hostname) ? "(You)" : ""), padding + columnPosition, startY);
            columnPosition += columnLength;
            g.drawString(String.valueOf(object.getHighScore()), padding + columnPosition, startY);
            columnPosition += columnLength;
            g.drawString(String.valueOf(object.getCurrentLevel()), padding + columnPosition, startY);
            columnPosition += columnLength;
            g.drawString(String.valueOf(object.getDeathCounter()), padding + columnPosition, startY);
            startY += lineHeight + linePadding;
        }
    }

    public void setIsDrawingCurrentListOfPlayers(boolean isDrawingListOfCurrentPlayers) {
        this.isDrawingListOfCurrentPlayers = isDrawingListOfCurrentPlayers;
    }
}
