package game.logic;

import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Highscore implements Serializable {
    private static final long serialVersionUID = -5815020676293054824L;
    private final static int START_SCORE = 2000;
    private final static String FILE_HIGHSCORE_PATH = "res/highscore.txt";
    private int currentHighscore;
    private int deathCounter = 0;
    private ArrayList<Integer> highscores;
    private long comparingTime;

    public Highscore() {
        resetHighscore();
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

    public static Highscore readHighscore() {
        Highscore highscore = new Highscore();
        Path filePath = Path.of(FILE_HIGHSCORE_PATH);

        try {
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                return highscore;
            }
            try (ObjectInputStream fis = new ObjectInputStream(Files.newInputStream(filePath))) {
                highscore = (Highscore) fis.readObject();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return highscore;
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

    public void resetHighscore() {
        currentHighscore = START_SCORE;
        highscores = new ArrayList<>();
        comparingTime = System.currentTimeMillis();
        deathCounter = 0;
    }

    public void increaseHighscoreForItems(Item[] items) {
        for (Item item : items) {
            if (item != null) {
                if (item instanceof Heart) currentHighscore += 50;
                else if (item instanceof PowerRing) currentHighscore += 100;
            }
        }
    }

    public void writeHighscore() {
        Path filePath = Path.of(FILE_HIGHSCORE_PATH);
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            try (ObjectOutputStream fos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                fos.writeObject(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDeathCounter() {
        return deathCounter;
    }
}
