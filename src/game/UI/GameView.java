package game.UI;
import entities.controller.EntityController;
import game.controller.GameController;
import gameObjects.controller.GameObjectController;
import items.controller.ItemController;
import keyboardinputs.logic.KeyboardInputsIngame;
import maps.controller.MapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class GameView extends JPanel {

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int GAME_WIDTH = (int) (TILES_DEFAULT_SIZE * 2) * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = (int) (TILES_DEFAULT_SIZE * 2) * TILES_IN_HEIGHT;
    private double scaleX;
    private double scaleY;
    private double scaleFactor;
    private GameController gameController;
    private MapController mapController;
    private EntityController entityController;
    private ItemController itemController;
    private GameObjectController gameObjectController;
    private JFrame frame = new JFrame("Kenshins Journey");


    public GameView(GameController gameController, EntityController entityController, MapController mapController, ItemController itemController, GameObjectController gameObjectController) {
        this.gameController = gameController;
        this.mapController = mapController;
        this.gameObjectController = gameObjectController;
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
        frame.setUndecorated(false);

        calculateScaleFactors();
        int scaledWidth = (int) (GAME_WIDTH * scaleFactor);
        int scaledHeight = (int) (GAME_HEIGHT * scaleFactor);
        this.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

        JPanel container = new JPanel();
        configureContainerForGameView(container);

        frame.add(container);
        frame.pack();
        setFrameToFullScreen();
        frame.setVisible(true);
    }

    public void calculateScaleFactors() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.scaleX = screenSize.getWidth() / GAME_WIDTH;
        this.scaleY = screenSize.getHeight() / GAME_HEIGHT;
        this.scaleFactor = Math.min(scaleX, scaleY);
    }

    public void configureContainerForGameView(JPanel container) {
        container.setBackground(Color.BLACK);
        container.setLayout(new GridBagLayout());
        container.add(this);
    }

    public void setFrameToFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        Rectangle screenBounds = device.getDefaultConfiguration().getBounds();

        frame.setSize(screenBounds.width, screenBounds.height);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameController != null) {
            Graphics2D g2d = (Graphics2D) g.create(); // Create a new Graphics2D object
            String osName = System.getProperty("os.name");
            AffineTransform at;
            at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            g2d.setTransform(at);
            render(g2d);
            gameController.getInterfaceGame().updatePlayerHealth(entityController.getPlayer().getHealth());
            gameController.getInterfaceGame().updateHighscore();
            gameController.getInterfaceGame().draw(g2d, entityController.getPlayer());
            g2d.dispose(); // Dispose the Graphics2D object when done
        }
    }

    public void render(Graphics g) {
        //TODO: Class calling inside of gameController
        //TODO: Implement rendering for more stuff
        int mapOffset = mapController.getMapOffset();
        mapController.draw(g);
        entityController.drawEntities(g, mapOffset);
        gameController.getInterfaceGame().draw(g, entityController.getPlayer());
        itemController.getItemUI().drawMapItems(g, mapOffset, itemController.getItemsOnMap(), itemController.isShowHitBox(), itemController.getAnimations());
        gameObjectController.drawObjects(g, mapOffset);
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

            default:
                entityController.handleUserInputKeyPressed(e, gameController.getDeathScreen());
        }
    }

    public void handleUserInputKeyReleased(KeyEvent e) {
        entityController.handleUserInputKeyReleased(e);
    }

    public JFrame getFrame() {
        return frame;
    }
}
