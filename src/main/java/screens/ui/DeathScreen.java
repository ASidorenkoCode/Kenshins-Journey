package screens.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import game.UI.GameView;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeathScreen {

    public void draw(Graphics g, int highscore, int deathCounter) throws IOException {
        Graphics2D g2d = (Graphics2D) g;

        String content = new String(Files.readAllBytes(Paths.get("res/configsAndSaves/controls.json")));
        JsonArray controls = JsonParser.parseString(content).getAsJsonArray();
        String restartDeath = "";
        for (int i = 0; i < controls.size(); i++) {
            JsonObject control = controls.get(i).getAsJsonObject();
            if (control.get("name").getAsString().equals("RESTART_AFTER_DEATH")) {
                restartDeath = String.valueOf((char) control.get("keyCode").getAsInt());
                break;
            }
        }

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        Font gameOverFont = new Font("Arial", Font.BOLD, 40);
        Font statsFont = new Font("Arial", Font.PLAIN, 20);
        Font restartFont = new Font("Arial", Font.BOLD, 25);

        String gameOverText = "YOU DIED!";
        textSettings(g2d, gameOverFont, gameOverText);

        String scoreText = STR."Your current Score is \{highscore} points";
        String deaths = STR."You currently died \{deathCounter + 1} times!";

        int statsX = (GameView.GAME_WIDTH - g2d.getFontMetrics(statsFont).stringWidth(scoreText)) / 2;
        int gameOverY = GameView.GAME_HEIGHT / 3;
        int scoreY = gameOverY + 100;
        int enemiesY = scoreY + 30;

        g2d.setColor(Color.WHITE);
        g2d.setFont(statsFont);
        g2d.drawString(scoreText, statsX, scoreY);
        g2d.drawString(deaths, statsX, enemiesY);

        String restartTextLine1 = "Press " + restartDeath + " to Respawn!";
        String restartTextLine2 = "You will lose 100 score points on respawn!";
        int restartYLine1 = enemiesY + 100;
        int restartYLine2 = restartYLine1 + 30;

        int restartXLine1 = (GameView.GAME_WIDTH - g2d.getFontMetrics(restartFont).stringWidth(restartTextLine1)) / 2;
        int restartXLine2 = (GameView.GAME_WIDTH - g2d.getFontMetrics(restartFont).stringWidth(restartTextLine2)) / 2;

        g2d.setFont(restartFont);
        g2d.drawString(restartTextLine1, restartXLine1, restartYLine1);
        g2d.drawString(restartTextLine2, restartXLine2, restartYLine2);
    }

    private void textSettings(Graphics2D g2d, Font gameOverFont, String gameOverText) {
        int gameOverX = (GameView.GAME_WIDTH - g2d.getFontMetrics(gameOverFont).stringWidth(gameOverText)) / 2;
        int gameOverY = GameView.GAME_HEIGHT / 3;

        g2d.setColor(Color.RED);
        g2d.setFont(gameOverFont);
        g2d.drawString(gameOverText, gameOverX, gameOverY);
    }
}
