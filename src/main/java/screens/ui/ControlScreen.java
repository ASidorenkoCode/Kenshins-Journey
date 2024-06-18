package screens.ui;

import game.UI.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControlScreen implements KeyListener {
    private Map<String, Integer> controls = new HashMap<>();
    private Map<String, BufferedImage> controlImages = new HashMap<>();
    private String selectedControl;

    public ControlScreen() {
        // Initialize controls with default keys
        controls.put("Arrow Up", KeyEvent.VK_UP);
        controls.put("Arrow Down", KeyEvent.VK_DOWN);
        controls.put("Arrow Left", KeyEvent.VK_LEFT);
        controls.put("Arrow Right", KeyEvent.VK_RIGHT);

        // Load control images
        for (String control : controls.keySet()) {
            try {
                BufferedImage img = ImageIO.read(new File("res/controlButtons/" + control.toUpperCase().replace(" ", "") + ".png"));
                controlImages.put(control, img); // Store the image in the map
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void drawControls(Graphics g) {
        int y = 50;
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        for (String control : controls.keySet()) {
            BufferedImage img = controlImages.get(control);
            if (img != null) {
                g.drawImage(img, 50, y, null);
                g.setColor(Color.WHITE); // Set the color for the text
                g.drawString(control, 50 + img.getWidth() + 10, y + img.getHeight() / 2); // Draw the control name next to the image
            }
            y += img.getHeight() + 10; // Adjust y coordinate for next image
        }
    }

    public void handleClick(int x, int y) {
        // Determine which control was clicked based on the y coordinate
        // This is a simple example and might need to be adjusted based on your actual UI layout
        int controlIndex = (y - 50) / 20;
        if (controlIndex >= 0 && controlIndex < controls.size()) {
            selectedControl = (String) controls.keySet().toArray()[controlIndex];
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedControl != null) {
            controls.put(selectedControl, e.getKeyCode());
            selectedControl = null; // Reset selected control after changing the key
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}