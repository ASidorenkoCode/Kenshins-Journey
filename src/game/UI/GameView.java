package game.UI;

import constants.Constants;
import entities.controller.EntityController;
import game.controller.GameController;
import items.controller.ItemController;
import keyboardinputs.logic.KeyboardInputsIngame;
import maps.controller.MapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameView extends JPanel {

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int GAME_WIDTH = (int) (TILES_DEFAULT_SIZE * Constants.TILE_SCALE) * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = (int) (TILES_DEFAULT_SIZE * Constants.TILE_SCALE) * TILES_IN_HEIGHT;
    private GameController gameController;
    private MapController mapController;
    private EntityController entityController;
    private ItemController itemController;
    private JFrame frame = new JFrame("Kenshins Journey");


    public GameView(GameController gameController, EntityController entityController, MapController mapController, ItemController itemController) {
        this.gameController = gameController;
        this.mapController = mapController;
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyboardInputsIngame(this));
        this.entityController = entityController;
        this.itemController = itemController;
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
        if (gameController != null) {
            render(g);
            gameController.getInterfaceGame().updatePlayerHealth(entityController.getPlayer().getPlayerHealth());
            gameController.getInterfaceGame().updateHighscore();
            gameController.getInterfaceGame().draw(g, entityController.getPlayer());
        }
    }

    public void render(Graphics g) {
        //TODO: Class calling inside of gameController
        //TODO: Implement rendering for more stuff
        int mapOffset = mapController.getMapOffset();
        mapController.draw(g);
        entityController.drawEntities(g, mapOffset);
        gameController.getInterfaceGame().draw(g, entityController.getPlayer());
        itemController.drawMapItems(g, mapOffset);    }

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_0:
                itemController.selectItem(entityController.getPlayer(), 0);
                break;
            case KeyEvent.VK_1:
                itemController.selectItem(entityController.getPlayer(), 1);
                break;
            case KeyEvent.VK_2:
                itemController.selectItem(entityController.getPlayer(), 2);
                break;
            case KeyEvent.VK_3:
                itemController.selectItem(entityController.getPlayer(), 3);
                break;
            case KeyEvent.VK_4:
                itemController.selectItem(entityController.getPlayer(), 4);
                break;
            case KeyEvent.VK_5:
                itemController.selectItem(entityController.getPlayer(), 5);
                break;
            case KeyEvent.VK_6:
                itemController.selectItem(entityController.getPlayer(), 6);
                break;
            case KeyEvent.VK_7:
                itemController.selectItem(entityController.getPlayer(), 7);
                break;
            case KeyEvent.VK_8:
                itemController.selectItem(entityController.getPlayer(), 8);
                break;
            case KeyEvent.VK_9:
                itemController.selectItem(entityController.getPlayer(), 9);
                break;

            default: entityController.handleUserInputKeyPressed(e, gameController.getDeathScreen());
        }
    }

    public void handleUserInputKeyReleased(KeyEvent e) {
        entityController.handleUserInputKeyReleased(e);
    }

    public JFrame getFrame() {
        return frame;
    }
}
