package screens;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class StartScreen extends JPanel {
    private Image backgroundImage;

    public StartScreen() {
        try {
            backgroundImage = ImageIO.read(new File("res/screens/startScreen/StartScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}