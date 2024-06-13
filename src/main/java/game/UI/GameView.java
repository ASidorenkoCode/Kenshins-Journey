package game.UI;

import entities.controller.EntityController;
import game.controller.GameController;
import game.controller.GameState;
import gameObjects.controller.GameObjectController;
import items.controller.ItemController;
import keyboardinputs.logic.KeyboardInputsIngame;
import maps.controller.MapController;
import screens.controller.ScreenController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;

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
    private ScreenController screenController;
    private JFrame frame = new JFrame("Kenshins Journey");


    public GameView(GameController gameController, EntityController entityController, MapController mapController, ItemController itemController, GameObjectController gameObjectController, ScreenController screenController) {
        this.gameController = gameController;
        this.mapController = mapController;
        this.gameObjectController = gameObjectController;
        this.screenController = screenController;
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
            Graphics2D g2d = (Graphics2D) g.create(); // Create a new Graphics2D object
            String osName = System.getProperty("os.name");
            AffineTransform at;
            at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            g2d.setTransform(at);
            render(g2d);
            g2d.dispose(); // Dispose the Graphics2D object when done
        }
    }

    public void render(Graphics g) {
        //TODO: Class calling inside of gameController
        //TODO: Implement rendering for more stuff
        int mapOffsetX = mapController.getMapOffsetX();
        int mapOffsetY = mapController.getMapOffsetY();
        mapController.draw(g, false);
        entityController.drawEntities(g, mapOffsetX, mapOffsetY);
        itemController.draw(g, mapOffsetX, mapOffsetY);
        gameObjectController.drawObjects(g, mapOffsetX, mapOffsetY);
        mapController.draw(g, true);

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

    public void handleUserInputKeyPressed(KeyEvent e) throws IOException {
        switch (e.getKeyCode()) {
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
            case KeyEvent.VK_L:
                if (gameController.getCurrentGameState() == GameState.DEAD) gameController.restartLevelAfterDeath();
                break;
            case KeyEvent.VK_W:
                if (gameController.getCurrentGameState() == GameState.START) gameController.startGame();
                break;
            case KeyEvent.VK_P:
                if (gameController.getCurrentGameState() == GameState.PLAYING) gameController.loadNewMap();
                break;
            case KeyEvent.VK_ENTER:
                if (gameController.getCurrentGameState() == GameState.END) gameController.resetGame();
                break;
            case KeyEvent.VK_H:
                if (gameController.getCurrentGameState() == GameState.START) {
                    gameController.setCurrentGameState(GameState.HIGHSCORE);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (gameController.getCurrentGameState() == GameState.END) {
                    gameController.getHighscore().writeAllHighscores();
                    System.exit(0);
                }
                if (gameController.getCurrentGameState() == GameState.HIGHSCORE) {
                    gameController.setCurrentGameState(GameState.START);
                }
                break;
            case KeyEvent.VK_O:
                gameController.setIsDrawingListOfCurrentPlayersForInterfaceGame(true);
                break;
            case KeyEvent.VK_M:
                gameController.useMultiplayer();
                this.requestFocusInWindow();
                break;
            default:
                entityController.handleUserInputKeyPressed(e, gameController.getDeathScreen());

        }
    }

    public void handleUserInputKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_O:
                gameController.setIsDrawingListOfCurrentPlayersForInterfaceGame(false);
                break;
            default:
                entityController.handleUserInputKeyReleased(e);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
