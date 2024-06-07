package screens.ui;

import game.UI.GameView;
import game.logic.Highscore;

import java.awt.*;

public class EndScreen {
    private int mapCount;

    public void draw(Graphics g, Highscore highscore) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        Font gameOverFont = new Font("Arial", Font.BOLD, 40);
        Font statsFont = new Font("Arial", Font.PLAIN, 20);

        String gameOverText = "YOU FINISHED THE GAME!";
        textSettings(g2d, gameOverFont, gameOverText);

        int gameOverY = GameView.GAME_HEIGHT / 4;
        int statsY = gameOverY + 100;
        int columnWidth = 200;
        FontMetrics fm = g2d.getFontMetrics(gameOverFont);
        int gameOverTextWidth = fm.stringWidth(gameOverText);
        int gameOverTextX = (GameView.GAME_WIDTH - gameOverTextWidth) / 2;

// Use gameOverTextX as the start position for the stats
        int statsX = gameOverTextX;

        g2d.setColor(Color.WHITE);
        g2d.setFont(statsFont);
        g2d.drawString("Map", statsX, statsY - 30);
        g2d.drawString("Current Highscore", statsX + columnWidth, statsY - 30);
        g2d.drawString("Difference", statsX + columnWidth * 2, statsY - 30);

        for (int i = 0; i < highscore.getAllHighscores().size(); i++) {
            String mapCountText = String.valueOf((i + 1));
            String currentGameText = String.valueOf(highscore.getAllHighscores().get(i));
            String differenceText = String.valueOf(highscore.getAllHighscores().get(i));

            g2d.setColor(Color.WHITE);
            g2d.setFont(statsFont);
            g2d.drawString(mapCountText, statsX, statsY);

            g2d.drawString(currentGameText, statsX + columnWidth, statsY);

            if (highscore.getAllHighscores().get(i) > 0) {
                g2d.setColor(Color.GREEN);
                differenceText = "+ " + differenceText;
            }

            g2d.drawString(differenceText, statsX + columnWidth * 2, statsY);

            statsY += 30;
        }
    }

    private void textSettings(Graphics2D g2d, Font gameOverFont, String gameOverText) {
        int gameOverX = (GameView.GAME_WIDTH - g2d.getFontMetrics(gameOverFont).stringWidth(gameOverText)) / 2;
        int gameOverY = GameView.GAME_HEIGHT / 4;

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameOverFont);
        g2d.drawString(gameOverText, gameOverX, gameOverY);
    }

    public void setMapCount(int mapCount) {
        this.mapCount = mapCount;
    }


}