package screens;

import game.UI.GameView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DeathScreen {
    private JFrame frame;
    private Dimension originalSize;
    private boolean playerContinuesGame = false;
    private JPanel centerPanel;
    private boolean displayDeathScreenOnlyOnce = false;

    public DeathScreen(JFrame frame) {
        this.frame = frame;
        this.originalSize = frame.getSize();
    }

    public void displayDeathScreen() {
        displayDeathScreenOnlyOnce = true;
        frame.setPreferredSize(new Dimension(GameView.GAME_WIDTH, GameView.GAME_HEIGHT));

        JLabel deathLabel = createDeathLabel();
        centerPanel = setupCenterPanel(deathLabel);

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(centerPanel, JLayeredPane.POPUP_LAYER);

        frame.setVisible(true);
    }

    public void removeDeathScreen() {
        setPlayerContinuesGame(true);
        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.remove(centerPanel);
        frame.repaint();
    }

    private JPanel setupCenterPanel(JLabel deathLabel) {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(true);
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setSize(GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(deathLabel);
        centerPanel.add(Box.createVerticalGlue());

        return centerPanel;
    }

    private JLabel createDeathLabel() {
        JLabel deathLabel = new JLabel("<html>YOU DIED<br><center>Press ENTER to continue</center></html>");
        deathLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        deathLabel.setForeground(Color.WHITE);
        deathLabel.setHorizontalAlignment(JLabel.CENTER);
        deathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Border border = BorderFactory.createEmptyBorder(20, 0, 0, 0);
        deathLabel.setBorder(border);
        return deathLabel;
    }

    public boolean isPlayerContinuesGame() {
        return playerContinuesGame;
    }

    public void setPlayerContinuesGame(boolean playerContinuesGame) {
        this.playerContinuesGame = playerContinuesGame;
    }

    public void setDisplayDeathScreenOnlyOnce(boolean displayDeathScreenOnlyOnce) {
        this.setPlayerContinuesGame(false);
        this.displayDeathScreenOnlyOnce = displayDeathScreenOnlyOnce;
    }

    public boolean isDisplayDeathScreenOnlyOnce() {
        return displayDeathScreenOnlyOnce;
    }
}
