package game.logic;
import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;
import java.util.ArrayList;

public class Highscore {
    private final static int START_SCORE = 2000;
    private int currentHighscore;
    private int deathCounter;
    private ArrayList<Integer> highscores;

    private long comparingTime;

    public Highscore() {
        currentHighscore = START_SCORE;
        highscores = new ArrayList<>();
        comparingTime = System.currentTimeMillis();
        deathCounter = 0;
    }
    public void decreaseHighScoreAfterOneSecond() {
        long currentTime = System.currentTimeMillis();

        //check if one second has passed
        if (System.currentTimeMillis() - comparingTime >= 1000) {
            comparingTime = currentTime;
            currentHighscore--;
        }
    }
    public void increaseHighscoreForKappa() {
        currentHighscore += 200;
    }

    public void increaseHighscoreForBoss() {
        currentHighscore += 1000;
    }

    public void decreaseHighscoreForDeath() {
        currentHighscore -= 100;
    }

    public void increaseHighscoreForItems(Item[] items) {
        for(Item item: items) {
            if(item != null) {
                if(item instanceof Heart) currentHighscore += 50;
                else if(item instanceof PowerRing) currentHighscore += 100;
            }
        }
    }
    public void increaseDeathCounter() {
        deathCounter++;
    }

    public void addCurrentHighscoreToList() {
        highscores.add(currentHighscore);
    }

    public int getCurrentHighscore() {
        return currentHighscore;
    }

    public ArrayList<Integer> getAllHighscores() {
        return highscores;
    }
}
