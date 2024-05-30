package game.logic;

import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;

import java.util.ArrayList;

public class Highscore {
    private final static int START_SCORE = 2000;

    private int currentHighscore;

    private ArrayList<Integer> highscores;

    public Highscore() {
        currentHighscore = START_SCORE;
        highscores = new ArrayList<>();
    }
    public void decreaseHighScoreAfterOneSecond() {
        //TODO: implement timer
        currentHighscore--;
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
