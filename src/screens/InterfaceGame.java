package screens;

import entities.logic.Player;
import game.UI.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class InterfaceGame {
    private BufferedImage fullHeart;
    private BufferedImage halfHeart;
    private BufferedImage emptyHeart;
    private BufferedImage swordAttackIcon;
    private int playerHealth;
    private int score;
    private long lastTime;
    private int totalHearts;
    private int attackCooldown;
    private boolean attackCooldownActive;


    public InterfaceGame(Player player) {
        try {
            BufferedImage healthbarPlayer = ImageIO.read(new File("res/healthPlayer/healthbarPlayer.png"));
            fullHeart = healthbarPlayer.getSubimage(0, 0, 64, 64);
            halfHeart = healthbarPlayer.getSubimage(64, 0, 64, 64);
            emptyHeart = healthbarPlayer.getSubimage(128, 0, 64, 64);
            swordAttackIcon = ImageIO.read(new File("res/interfacePlayer/swordAttack.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerHealth = player.getPlayerHealth();
        totalHearts = player.getTotalHearts();
        score = 5000;
        lastTime = System.currentTimeMillis();
    }

    public void draw(Graphics g) {
        int maximumHeartsPerRow = 10;
        for (int i = 0; i < totalHearts; i++) {
            Image heart;
            if (playerHealth > i * 2 + 1) {
                heart = fullHeart;
            } else if (playerHealth > i * 2) {
                heart = halfHeart;
            } else {
                heart = emptyHeart;
            }

            int x = (i % maximumHeartsPerRow) * 32 - 32;
            int y = (i / maximumHeartsPerRow) * 32 - 32;

            g.drawImage(heart, x, y, 150, 150, null);
        }
        drawSwordAttackIcon(g);
        drawScore(g, score);
    }

    private void drawSwordAttackIcon(Graphics g) {
        int x = 10; // adjust as needed
        int y = GameView.GAME_HEIGHT - swordAttackIcon.getHeight() - 10; // adjust as needed
        int diameter = Math.max(swordAttackIcon.getWidth() + 30, swordAttackIcon.getHeight() + 30);

        int imageX = x + diameter / 2 - swordAttackIcon.getWidth() / 2;
        int imageY = y + diameter / 2 - swordAttackIcon.getHeight() / 2;

        Graphics2D g2d = (Graphics2D) g;
        Composite originalComposite = g2d.getComposite(); // save original composite

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% transparency
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x, y - 30, diameter, diameter);

        g2d.setComposite(originalComposite); // reset to original composite
        if (attackCooldown == 0) {
            g2d.drawImage(swordAttackIcon, imageX, imageY - 30, null);
            attackCooldownActive = false;
        }

        if (!attackCooldownActive && attackCooldown > 0) startCooldownTimer();
        if (attackCooldownActive) {
            g2d.setFont(new Font("Calibri", Font.BOLD, 32));

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(x, y - 30, diameter, diameter);
            g2d.setColor(Color.WHITE);

            String cooldownText = String.valueOf(attackCooldown);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(cooldownText);
            int textHeight = fm.getHeight();

            int textX = x + (diameter - textWidth) / 2;
            int textY = y + (diameter - textHeight) / 2 + fm.getAscent();

            g2d.drawString(cooldownText, textX, textY);
            g2d.setFont(new Font("Calibri", Font.BOLD, 12)); // reset font size
        }

        g2d.setColor(Color.BLACK); // reset color
        g2d.drawOval(x, y - 30, diameter, diameter);
    }

    public void updatePlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void updateHighscore() {
        if (System.currentTimeMillis() - lastTime >= 1000 && score > 0) {
            score--;
            lastTime = System.currentTimeMillis();
        }
    }

    private void drawScore(Graphics g, int score) {
        String scoreText = "Score: " + score;
        int x = GameView.GAME_WIDTH / 2;
        int y = 50;

        g.setColor(Color.WHITE); // Set the color to white
        g.setFont(new Font("Calibri", Font.BOLD, 32)); // Set the font to Calibri and size to 32px
        g.drawString(scoreText, x, y); // Draw the string in the center
        g.setColor(Color.BLACK); // Reset the color to black
        g.setFont(new Font("Calibri", Font.BOLD, 12)); // Reset the font size to default
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalHearts(int totalHearts) {
        this.totalHearts = totalHearts;
    }

    public void increaseScore(int increment) {
        score += increment;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    private void startCooldownTimer() {
        Timer attackCooldownTimer = new Timer();
        attackCooldownActive = true;
        attackCooldownTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (attackCooldown > 0) {
                    attackCooldown--;
                } else {
                    attackCooldownTimer.cancel();
                }
            }
        }, 0, 1000); // schedule to run every second
    }
}
