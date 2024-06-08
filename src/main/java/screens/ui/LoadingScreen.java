package screens.ui;

import game.UI.GameView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class LoadingScreen {
    private JFrame frame;
    private JLabel loadingLabel;

    List<String> facts = Arrays.asList(
            "Did you know? Game development with Java Swing is not fun.",
            "Did you know? Bauer has a first name.",
            "Did you know? This loading screen is useless, because the map is already loaded."
    );

    public LoadingScreen() {
    }

    public void displayLoadingScreen() {
        frame.setPreferredSize(new Dimension(GameView.GAME_WIDTH, GameView.GAME_HEIGHT));

        JProgressBar progressBar = setupProgressBar();
        JPanel progressBarPanel = setupProgressBarPanel(progressBar);
        JPanel centerPanel = setupCenterPanel(progressBarPanel);

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(centerPanel, JLayeredPane.POPUP_LAYER);

        frame.setVisible(true);

        startProgressUpdateTimer(progressBar, centerPanel, layeredPane);
    }

    private JProgressBar setupProgressBar() {
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
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
                super.paintDeterminate(g, c);
            }
        });

        progressBar.setMaximumSize(new Dimension(GameView.GAME_WIDTH, 200)); // Adjust the height as needed
        progressBar.setPreferredSize(new Dimension(GameView.GAME_WIDTH/2, 50)); // Adjust the height as needed

        return progressBar;
    }

    private JPanel setupProgressBarPanel(JProgressBar progressBar) {
        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.X_AXIS));
        progressBarPanel.setBackground(Color.BLACK);
        progressBarPanel.add(Box.createHorizontalGlue());
        progressBarPanel.add(progressBar);
        progressBarPanel.add(Box.createHorizontalGlue());

        return progressBarPanel;
    }

    private JPanel setupCenterPanel(JPanel progressBarPanel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        loadingLabel = new JLabel("Loading...") {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(centerPanel.getWidth(), super.getPreferredSize().height);
            }
        };
        loadingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setHorizontalAlignment(JLabel.CENTER);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setMaximumSize(new Dimension(centerPanel.getWidth(), loadingLabel.getPreferredSize().height));

        JLabel factLabel = getFactLabel(centerPanel);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(loadingLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some vertical space
        centerPanel.add(progressBarPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some vertical space
        centerPanel.add(factLabel);
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        return centerPanel;
    }

    private JLabel getFactLabel(JPanel centerPanel) {
        Random random = new Random();
        String randomFact = facts.get(random.nextInt(facts.size()));
        JLabel factLabel = new JLabel(randomFact) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(centerPanel.getWidth(), super.getPreferredSize().height);
            }
        };
        factLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        factLabel.setForeground(Color.WHITE);
        factLabel.setHorizontalAlignment(JLabel.CENTER);
        factLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        factLabel.setMaximumSize(new Dimension(centerPanel.getWidth(), factLabel.getPreferredSize().height));
        return factLabel;
    }

    private void startProgressUpdateTimer(JProgressBar progressBar, JPanel centerPanel, JLayeredPane layeredPane) {
        new Timer().schedule(new TimerTask() {
            int progress = 0;
            Random random = new Random();

            @Override
            public void run() {
                progress += 10 + random.nextInt(26);
                progressBar.setValue(progress);

                if (progress >= 100) {
                    layeredPane.remove(centerPanel);
                    frame.revalidate();
                    frame.repaint();
                    this.cancel(); // Stop the timer
                }
            }
        }, 0, 500); // Update every 0.5 seconds
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}