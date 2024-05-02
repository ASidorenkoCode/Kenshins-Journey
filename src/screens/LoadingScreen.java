package screens;

import game.UI.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicProgressBarUI;
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

        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(Color.BLACK);
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10); // 10 pixels of spacing
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 5); // 5 pixels of border width
        progressBar.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));

        progressBar.setUI(new BasicProgressBarUI() {
            protected void paintDeterminate(Graphics g, JComponent c) {
                // Always paint the progress bar black
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
                super.paintDeterminate(g, c);
            }
        });

        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.X_AXIS));

        progressBarPanel.setBackground(Color.BLACK);

        progressBarPanel.add(Box.createHorizontalGlue());
        progressBarPanel.add(progressBar);
        progressBarPanel.add(Box.createHorizontalGlue());

        progressBar.setMaximumSize(new Dimension(GameView.GAME_WIDTH, 200)); // Adjust the height as needed
        progressBar.setPreferredSize(new Dimension(GameView.GAME_WIDTH/2, 50)); // Adjust the height as needed

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(progressBarPanel);
        centerPanel.add(Box.createVerticalGlue());

        centerPanel.setBackground(Color.BLACK);

        centerPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(centerPanel, JLayeredPane.POPUP_LAYER);

        frame.setVisible(true);

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