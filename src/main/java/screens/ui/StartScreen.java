package screens.ui;

import game.UI.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StartScreen {
    private Image backgroundImage;
    private Image[] buttonImages = new Image[4];
    private Image[] buttonSelectedImages = new Image[4];
    private boolean[] buttonVisible = new boolean[4];
    private int selectedButton = 0;

    public StartScreen() {
        try {
            backgroundImage = ImageIO.read(new File("res/screens/startScreen/MainStartScreen.png"));
            for (int i = 0; i < 4; i++) {
                buttonImages[i] = ImageIO.read(new File("res/screens/startScreen/Button" + (i + 1) + ".png"));
                buttonSelectedImages[i] = ImageIO.read(new File("res/screens/startScreen/Button" + (i + 1) + "a.png"));
                buttonVisible[i] = true;
            }
            if (new File("res/highscore.txt").exists()) {
                buttonVisible[0] = true;
                selectedButton = 0;
            } else {
                buttonVisible[0] = false;
                selectedButton = 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT, null);
        double scaleFactor = 3.5;
        for (int i = 0; i < 4; i++) {
            if (buttonVisible[i]) {
                Image buttonImage = (i == selectedButton) ? buttonSelectedImages[i] : buttonImages[i];
                int newWidth = (int) (buttonImage.getWidth(null) / scaleFactor);
                int newHeight = (int) (buttonImage.getHeight(null) / scaleFactor);
                g.drawImage(buttonImage, 50, 350 + i * 100, newWidth, newHeight, null);
            }
        }
    }


    public void handleDownKey() {
        changeButtonImageToNonSelected();
        selectNextButton();
        changeButtonImageToSelected();
    }

    public void handleUpKey() {
        changeButtonImageToNonSelected();
        selectPreviousButton();
        changeButtonImageToSelected();
    }

    private void changeButtonImageToNonSelected() {
        try {
            buttonImages[selectedButton] = ImageIO.read(new File("res/screens/startScreen/Button" + (selectedButton + 1) + ".png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void selectNextButton() {
        do {
            selectedButton = (selectedButton + 1) % 5;
        } while (!buttonVisible[selectedButton]);
    }

    private void selectPreviousButton() {
        do {
            selectedButton = (selectedButton + 4) % 5;
        } while (!buttonVisible[selectedButton]);
    }

    private void changeButtonImageToSelected() {
        try {
            buttonSelectedImages[selectedButton] = ImageIO.read(new File("res/screens/startScreen/Button" + (selectedButton + 1) + "a.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getSelectedButton() {
        return selectedButton;
    }
}