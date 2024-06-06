package screens.ui;

import game.UI.GameView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class StartScreen {
    private Image backgroundImage;

    public StartScreen() {
        try {
            backgroundImage = ImageIO.read(new File("res/screens/startScreen/StartScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT, null);
    }
}