package game.logic;

import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Highscore implements Serializable {
    private static final long serialVersionUID = -5815020676293054824L;
    private final static int START_SCORE = 2000;
    private final static String FILE_HIGHSCORE_PATH = "res/highscore.txt";
    private int currentHighscore;
    private int deathCounter = 0;
    private ArrayList<Integer> highscores;
    private ArrayList<Integer> bestHighscores;
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

    public void increaseHighscoreForEnemies() {
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
        findBestHighscores();
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

    public void deleteHighscoreFile() {
        Path filePath = Path.of(FILE_HIGHSCORE_PATH);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAllHighscores() {
        Path filePath = Path.of("res/highscores.txt");
        int startScore = 1;
        try {
            if (Files.exists(filePath)) {
                try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                    String lastLine = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lastLine = line;
                    }
                    if (!lastLine.isEmpty()) {
                        String[] parts = lastLine.split(":");
                        String lastScore = parts[0].trim();
                        startScore = Integer.parseInt(lastScore.substring(10)) + 1;
                    }
                }
            } else {
                Files.createFile(filePath);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
                for (int i = 0; i < highscores.size(); i += 5) {
                    writer.write(STR."highscore_\{startScore + i / 5}: ");
                    for (int j = i; j < Math.min(i + 5, highscores.size()); j++) {
                        writer.write(highscores.get(j).toString());
                        if (j != Math.min(i + 5, highscores.size()) - 1) {
                            writer.write(", ");
                        }
                    }
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findBestHighscores() {
        Path filePath = Path.of("res/highscores.txt");
        bestHighscores = new ArrayList<>();
        int bestSum = Integer.MIN_VALUE;
        try {
            if (Files.exists(filePath)) {
                try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        String[] scores = parts[1].trim().split(",");
                        ArrayList<Integer> currentScores = new ArrayList<>();
                        int lastScore = 0;
                        for (String score : scores) {
                            int currentScore = Integer.parseInt(score.trim());
                            currentScores.add(currentScore);
                            lastScore = currentScore; // Keep updating the lastScore with the currentScore
                        }
                        if (lastScore > bestSum) { // Check if the lastScore is greater than the bestSum
                            bestSum = lastScore;
                            bestHighscores = currentScores;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDeathCounter() {
        return deathCounter;
    }

    public ArrayList<Integer> getBestHighscores() {
        return bestHighscores;
    }
}
