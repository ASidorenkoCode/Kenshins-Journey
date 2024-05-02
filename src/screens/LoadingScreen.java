package screens;

import game.UI.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingScreen {
    private JFrame frame;
    private JLabel loadingLabel;
    private Dimension originalSize;

    public LoadingScreen(JFrame frame) {
        this.frame = frame;
        this.originalSize = frame.getSize();
    }

    public void displayLoadingScreen() {
        System.out.println("displayLoadingScreen method called");

        frame.setPreferredSize(new Dimension(GameView.GAME_WIDTH, GameView.GAME_HEIGHT));

        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.BLUE); // Change the color of the progress bar
        progressBar.setBackground(Color.WHITE); // Change the background color of the progress bar
        progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add a border to the progress bar

        // Create a panel to hold the progress bar
        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.X_AXIS));

        // Set the background color of the progressBarPanel to black
        progressBarPanel.setBackground(Color.BLACK);

        // Add padding panels on the left and right
        progressBarPanel.add(Box.createHorizontalGlue());
        progressBarPanel.add(progressBar);
        progressBarPanel.add(Box.createHorizontalGlue());

        // Set the maximum size of the progress bar to be 50% of the screen width and increase the height
        progressBar.setMaximumSize(new Dimension(frame.getWidth() / 2, 100)); // Adjust the height as needed

        // Create a panel to center the progress bar vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(progressBarPanel);
        centerPanel.add(Box.createVerticalGlue());

        // Set the background color of the centerPanel to black
        centerPanel.setBackground(Color.BLACK);

        centerPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(centerPanel, JLayeredPane.POPUP_LAYER); // Add to the popup layer, which is above all others

        frame.setVisible(true);

        // Update the progress bar over time
        new Timer().schedule(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                progress += 20;
                progressBar.setValue(progress);

                if (progress >= 100) {
                    System.out.println("Removing centerPanel");
                    layeredPane.remove(centerPanel);
                    frame.revalidate();
                    frame.repaint();
                    this.cancel(); // Stop the timer
                }
            }
        }, 0, 500); // Update every 0.5 seconds
    }
}