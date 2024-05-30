package game.logic;

import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Highscore {
    private final static int START_SCORE = 2000;
    private int currentHighscore;
    private ArrayList<Integer> highscores;

    private Instant comparingTime;

    public Highscore() {
        currentHighscore = START_SCORE;
        highscores = new ArrayList<>();
        comparingTime = Instant.now();
    }
    public void decreaseHighScoreAfterOneSecond() {
        Instant currentTime = Instant.now();

        //check if one second has passed
        if (Duration.between(comparingTime, currentTime).getSeconds() >= 1) {
            comparingTime = currentTime;
            currentHighscore--;
            System.out.println(currentHighscore);
        }

        //decrease cpu usage
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public int getCurrentHighscore() {
        return currentHighscore;
    }

}
