package screens;

import entities.logic.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InterfaceGame {
    private BufferedImage fullHeart;
    private BufferedImage halfHeart;
    private BufferedImage emptyHeart;
    private int playerHealth;

    public InterfaceGame(Player player) {
        try {
            BufferedImage healthbarPlayer = ImageIO.read(new File("res/healthPlayer/healthbarPlayer.png"));
            fullHeart = healthbarPlayer.getSubimage(0, 0, 64, 64);
            halfHeart = healthbarPlayer.getSubimage(64, 0, 64, 64);
            emptyHeart = healthbarPlayer.getSubimage(128, 0, 64, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerHealth = player.getPlayerHealth();
    }

    public void draw(Graphics g) {
        for (int i = 0; i < 3; i++) {
            Image heart;
            if (playerHealth > i * 2 + 1) {
                heart = fullHeart;
            } else if (playerHealth > i * 2) {
                heart = halfHeart;
            } else {
                heart = emptyHeart;
            }
            g.drawImage(heart, i * 32 - 32, -32, 150, 150, null);
        }
    }

    public void updatePlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }
}
