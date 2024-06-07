package network;

public class ServerObject {

    private int currentLevel;
    private int highScore;
    private int deathCounter;
    private float horizontalPlayerPosition;
    private String playerId;

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