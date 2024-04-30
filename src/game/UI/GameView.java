package game.UI;

import entities.logic.Player;
import entities.ui.PlayerUI;
import game.controller.GameController;
import keyboardinputs.logic.KeyboardInputsIngame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameView extends JPanel {

    private GameController gameController;

    private PlayerUI playerUI;

    private final static int TILES_DEFAULT_SIZE = 32;
    private final static float TILE_SCALE = 2f;
    private final static int TILES_IN_WIDTH = 26;
    private final static int TILES_IN_HEIGHT = 14;
    private final static int GAME_WIDTH = (int) (TILES_DEFAULT_SIZE * TILE_SCALE) * TILES_IN_WIDTH;
    private final static int GAME_HEIGHT = (int) (TILES_DEFAULT_SIZE * TILE_SCALE) * TILES_IN_HEIGHT;


    public GameView(GameController gameController, PlayerUI playerUI) {
        this.gameController = gameController;
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyboardInputsIngame(this));
        this.playerUI = playerUI;
    }

    public void gameWindow() {
        JFrame frame = new JFrame("Kenshins Journey");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        calculateScreenCenter(frame);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameController != null) render(g);
    }

    public void render(Graphics g) {
        //TODO: Implement rendering
        playerUI.drawAnimations(g);
    }

    public void calculateScreenCenter(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        frame.setLocation(x, y);
    }

    public void showFPS_UPS(int frames, int updates) {
        String fpsUpsText = "Kenshins Journey | FPS: " + frames + " | UPS: " + updates;
        setFrameTitle(fpsUpsText);
    }

    public void setFrameTitle(String fpsUpsText) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setTitle(fpsUpsText);
    }

    public void handleUserInput(KeyEvent e) {
        playerUI.handleUserInput(e);
    }
}
