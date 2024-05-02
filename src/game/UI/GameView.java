package game.UI;

import entities.controller.EntityController;
import game.controller.GameController;
import keyboardinputs.logic.KeyboardInputsIngame;
import maps.UI.MapUI;
import constants.Constants;
import maps.controller.MapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameView extends JPanel {

    private GameController gameController;
    private MapController mapController;
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int GAME_WIDTH = (int) (TILES_DEFAULT_SIZE * Constants.TILE_SCALE) * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = (int) (TILES_DEFAULT_SIZE * Constants.TILE_SCALE) * TILES_IN_HEIGHT;
    private EntityController entityController;

    private JFrame frame = new JFrame("Kenshins Journey");


    public GameView(GameController gameController, EntityController entityController, MapController mapController) {
        this.gameController = gameController;
        this.mapController = mapController;
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyboardInputsIngame(this));
        this.entityController = entityController;
    }

    public void gameWindow() {
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
        //TODO: Implement rendering for more stuff
        int mapOffset = mapController.getMapOffset();
        mapController.draw(g);
        entityController.drawEntities(g, mapOffset);
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

    public void handleUserInputKeyPressed(KeyEvent e) {
        entityController.handleUserInputKeyPressed(e);
    }

    public void handleUserInputKeyReleased(KeyEvent e) {
        entityController.handleUserInputKeyReleased(e);
    }

    public JFrame getFrame() {
        return frame;
    }
}
