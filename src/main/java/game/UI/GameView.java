package game.UI;

import entities.controller.EntityController;
import game.controller.GameController;
import gameObjects.controller.GameObjectController;
import items.controller.ItemController;
import keyboardinputs.logic.KeyboardInputsIngame;
import maps.controller.MapController;
import screens.controller.ScreenController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

public class GameView extends JPanel {

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int GAME_WIDTH = TILES_DEFAULT_SIZE * 2 * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_DEFAULT_SIZE * 2 * TILES_IN_HEIGHT;
    private double scaleX;
    private double scaleY;
    private double scaleFactor;
    private final GameController gameController;
    private final MapController mapController;
    private final EntityController entityController;
    private final ItemController itemController;
    private final GameObjectController gameObjectController;
    private ScreenController screenController;

    private final JFrame frame = new JFrame("Kenshins Journey");


    public GameView(GameController gameController, EntityController entityController, MapController mapController, ItemController itemController, GameObjectController gameObjectController, ScreenController screenController) {
        this.gameController = gameController;
        this.mapController = mapController;
        this.gameObjectController = gameObjectController;
        this.screenController = screenController;
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyboardInputsIngame(this, gameController));
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

//        drawStartScreen();
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
            Graphics2D g2d = (Graphics2D) g.create();
            String osName = System.getProperty("os.name");
            AffineTransform at;
            at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            g2d.setTransform(at);
            try {
                render(g2d);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            g2d.dispose();
        }
    }

    public void render(Graphics g) throws IOException {
        int mapOffsetX = mapController.getMapOffsetX();
        int mapOffsetY = mapController.getMapOffsetY();
        mapController.draw(g, false);
        entityController.drawEntities(g, mapOffsetX, mapOffsetY);
        mapController.draw(g, true);
        entityController.drawHealthBarBoss(g);
        itemController.draw(g, mapOffsetX, mapOffsetY);
        gameObjectController.drawObjects(g, mapOffsetX, mapOffsetY);
        screenController.draw(g, gameController.getCurrentGameState(), gameController.getHighscore().getCurrentHighscore(), gameController.getHighscore().getDeathCounter(), gameController.getHighscore(), mapController.getMaps().size(), gameController.getPlayerId(), gameController.getHighscore().getAllHighscores().size() + 1, gameController.getIsPlayingMultiplayer());

    }

    public void showFPS_UPS(int frames, int updates) {
        String fpsUpsText = "Kenshins Journey | FPS: " + frames + " | UPS: " + updates;
        setFrameTitle(fpsUpsText);
    }

    public void setFrameTitle(String fpsUpsText) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setTitle(fpsUpsText);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
