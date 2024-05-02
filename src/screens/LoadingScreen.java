package screens;

import game.UI.GameView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
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
        URL url = this.getClass().getResource("/loadingScreen/backgroundloading.png");
        if (url == null) {
            System.out.println("Image not found");
        } else {
            Image image = Toolkit.getDefaultToolkit().getImage(url);

            // Use a MediaTracker to wait for the image to load
            MediaTracker tracker = new MediaTracker(new Component() {
            });
            tracker.addImage(image, 0);
            try {
                tracker.waitForID(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create the ImageIcon once the image is loaded
            ImageIcon loadingImage = new ImageIcon(image);
            frame.setPreferredSize(new Dimension(GameView.GAME_WIDTH, GameView.GAME_HEIGHT));
            loadingLabel = new JLabel(loadingImage);

            // Set the size and position of the JLabel to match the size of the JFrame
            loadingLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

            // Create a semi-transparent JPanel for the blur effect
            JPanel blurPanel = new JPanel();
            blurPanel.setBackground(new Color(0, 0, 0, 120)); // Semi-transparent black
            blurPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

            // Add the blur panel and the loading screen to the JLayeredPane
            JLayeredPane layeredPane = frame.getLayeredPane();
            layeredPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight())); // Set the size of the JLayeredPane to match the size of the JFrame
            layeredPane.add(loadingLabel, Integer.valueOf(1)); // Add the loading screen at depth 1
            layeredPane.add(blurPanel, Integer.valueOf(2)); // Add the blur panel at depth 2

            // Display the frame
            frame.setVisible(true);

            // Set a timer to remove the image and the blur panel from the frame after 3 seconds
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    layeredPane.remove(loadingLabel);
                    layeredPane.remove(blurPanel);
                    frame.revalidate();
                    frame.repaint();
                }
            }, 2000);
        }
    }

    public void hideLoadingScreen() {
        // Remove the loadingLabel from the layeredPane
        frame.getLayeredPane().remove(loadingLabel);
        // Revalidate and repaint the frame to reflect the changes
        frame.revalidate();
        frame.repaint();
    }
}