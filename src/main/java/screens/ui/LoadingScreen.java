package screens.ui;

import game.UI.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LoadingScreen {
    private int progress = 0;
    private Random random = new Random();
    private String loadingText = "Loading...";
    private Timer progressTimer;
    private String currentFact;

    private boolean isLoadingStarted = false;

    List<String> facts = Arrays.asList(
            "Did you know? Game development with Java Swing is not fun.",
            "Did you know? Bauer has a first name.",
            "Did you know? This loading screen is useless, because the map is already loaded."
    );

    public LoadingScreen() {
    }

    public void startLoading() {
        if (isLoadingStarted) {
            return;
        }
        isLoadingStarted = true;
        progressTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 15 + random.nextInt(10);
                if (progress >= 100) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        currentFact = facts.get(random.nextInt(facts.size()));
        progressTimer.start();
    }

    public boolean isLoadingFinished() {
        return progress >= 100;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int cornerRadiuse = 30;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        int textWidth = g2d.getFontMetrics().stringWidth(loadingText);
        int textX = (GameView.GAME_WIDTH - textWidth) / 2;
        int textY = GameView.GAME_HEIGHT / 2;
        g2d.drawString(loadingText, textX, textY);

        int progressBarWidth = GameView.GAME_WIDTH / 2;
        int progressBarHeight = 50;
        int progressBarX = (GameView.GAME_WIDTH - progressBarWidth) / 2;
        int progressBarY = textY + 60;
        g2d.drawRoundRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight, cornerRadiuse, cornerRadiuse);
        int progressWidth = (int) (progressBarWidth * ((double) progress / 100));
        g2d.fillRoundRect(progressBarX, progressBarY, progressWidth, progressBarHeight, cornerRadiuse, cornerRadiuse);

        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        int factWidth = g2d.getFontMetrics().stringWidth(currentFact);
        int factX = (GameView.GAME_WIDTH - factWidth) / 2;
        int factY = progressBarY + 80;
        g2d.drawString(currentFact, factX, factY);
    }

    public void resetProgress() {
        this.progress = 0;
        this.isLoadingStarted = false;
    }
}