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

        String gameOverText = mapCount == highscore.getAllHighscores().size() ? "YOU FINISHED THE GAME" : "GAME OVER";
        int gameOverY = GameView.GAME_HEIGHT / 4;
        textSettings(g2d, gameOverFont, gameOverText, gameOverY);

        int statsY = gameOverY + 100;
        int columnWidth = 200;
        int totalTableWidth = columnWidth * 4;

        int statsX = (GameView.GAME_WIDTH - totalTableWidth) / 2;

        g2d.setColor(Color.WHITE);
        g2d.setFont(statsFont);

        String mapTitle = "Map";
        String currentHighscoreTitle = "Current Highscore";
        String bestHighscoreTitle = "Best Highscore";
        String differenceTitle = "Difference";

        int mapTitleWidth = g2d.getFontMetrics().stringWidth(mapTitle);
        int currentHighscoreTitleWidth = g2d.getFontMetrics().stringWidth(currentHighscoreTitle);
        int bestHighscoreTitleWidth = g2d.getFontMetrics().stringWidth(bestHighscoreTitle);
        int differenceTitleWidth = g2d.getFontMetrics().stringWidth(differenceTitle);

        g2d.drawString(mapTitle, statsX + columnWidth / 2 - mapTitleWidth / 2, statsY - 30);
        g2d.drawString(currentHighscoreTitle, statsX + columnWidth + columnWidth / 2 - currentHighscoreTitleWidth / 2, statsY - 30);
        g2d.drawString(bestHighscoreTitle, statsX + columnWidth * 2 + columnWidth / 2 - bestHighscoreTitleWidth / 2, statsY - 30);
        g2d.drawString(differenceTitle, statsX + columnWidth * 3 + columnWidth / 2 - differenceTitleWidth / 2, statsY - 30);


        for (int i = 0; i < mapCount; i++) {
            String mapCountText = String.valueOf((i + 1));
            String currentGameText = i < highscore.getAllHighscores().size() ? String.valueOf(highscore.getAllHighscores().get(i)) : "-";
            String bestHighscoreText = "-";
            if (!highscore.getBestHighscores().isEmpty() && highscore.getBestHighscores().size() > i) {
                bestHighscoreText = String.valueOf(highscore.getBestHighscores().get(i));
            }
            String differenceText = "-";
            if (!bestHighscoreText.equals("-")) {
                differenceText = i < highscore.getAllHighscores().size() ? String.valueOf(Math.abs(highscore.getAllHighscores().get(i) - Integer.parseInt(bestHighscoreText))) : String.valueOf(Math.abs(0 - Integer.parseInt(bestHighscoreText)));
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(statsFont);
            int mapCountTextWidth = g2d.getFontMetrics().stringWidth(mapCountText);
            int currentGameTextWidth = g2d.getFontMetrics().stringWidth(currentGameText);
            int bestHighscoreTextWidth = g2d.getFontMetrics().stringWidth(bestHighscoreText);
            int differenceTextWidth = g2d.getFontMetrics().stringWidth(differenceText);

            g2d.drawString(mapCountText, statsX + columnWidth / 2 - mapCountTextWidth / 2, statsY);
            g2d.drawString(currentGameText, statsX + columnWidth + columnWidth / 2 - currentGameTextWidth / 2, statsY);
            g2d.drawString(bestHighscoreText, statsX + columnWidth * 2 + columnWidth / 2 - bestHighscoreTextWidth / 2, statsY);


            if (!bestHighscoreText.equals("-") && highscore.getAllHighscores().size() > i) {
                if (Integer.parseInt(bestHighscoreText) < highscore.getAllHighscores().get(i)) {
                    g2d.setColor(Color.GREEN);
                    differenceText = STR."+\{differenceText}";
                } else if (Integer.parseInt(bestHighscoreText) > highscore.getAllHighscores().get(i)) {
                    g2d.setColor(Color.RED);
                    differenceText = STR."-\{differenceText}";
                } else {
                    differenceText = "-";
                }
            }

            g2d.drawString(differenceText, statsX + columnWidth * 3 + columnWidth / 2 - differenceTextWidth / 2, statsY);

            statsY += 30;
        }

        if (!highscore.getBestHighscores().isEmpty() && highscore.getAllHighscores().get(highscore.getAllHighscores().size() - 1) > highscore.getBestHighscores().get(highscore.getBestHighscores().size() - 1)) {
            String message = "You have beaten your best high score in the game!";
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            int messageWidth = g2d.getFontMetrics().stringWidth(message);
            int messageX = (GameView.GAME_WIDTH - messageWidth) / 2;
            g2d.drawString(message, messageX, statsY + 50);
        }

        String gameOverRestart = "Press ENTER to restart or ESCAPE to quit the game";
        int gameOverRestartY = statsY + 100;
        textSettings(g2d, statsFont, gameOverRestart, gameOverRestartY);


    }

    private void textSettings(Graphics2D g2d, Font gameOverFont, String gameOverText, int gameOverY) {
        int gameOverX = (GameView.GAME_WIDTH - g2d.getFontMetrics(gameOverFont).stringWidth(gameOverText)) / 2;


        g2d.setColor(Color.WHITE);
        g2d.setFont(gameOverFont);
        g2d.drawString(gameOverText, gameOverX, gameOverY);
    }

    public void setMapCount(int mapCount) {
        this.mapCount = mapCount;
    }


}