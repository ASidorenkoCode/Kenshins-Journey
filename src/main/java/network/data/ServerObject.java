package network.data;

import entities.logic.Player;
import game.logic.Highscore;

public class ServerObject {

    private int currentLevel;
    private int highScore;
    private int deathCounter;
    private float horizontalPlayerPosition;
    private String playerId;


    public ServerObject(Highscore currentHighScore, Player player, String playerId) {
        this.currentLevel = currentHighScore.getAllHighscores().size() + 1;
        this.highScore = currentHighScore.getCurrentHighscore();
        this.deathCounter = currentHighScore.getDeathCounter();
        this.horizontalPlayerPosition = player.getX();
        this.playerId = playerId;
    }

    public void updateObject(ServerObject object) {
        this.highScore = object.highScore;
        this.deathCounter = object.deathCounter;
        this.currentLevel = object.currentLevel;
        this.horizontalPlayerPosition = object.horizontalPlayerPosition;
        this.playerId = object.playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public float getHorizontalPlayerPosition() {
        return horizontalPlayerPosition;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getDeathCounter() {
        return deathCounter;
    }

}
