package game.logic;

import game.UI.GameView;


public class GameEngine implements Runnable {

    private Thread gameThread;
    private GameView gameView;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private volatile boolean SHOW_FPS_UPS;
    private long lastCheck;
    private int frames = 0;
    private int updates = 0;


    public GameEngine(boolean showFPS_UPS) {
        gameView = new GameView(this);
        gameView.gameWindow();
        this.SHOW_FPS_UPS = showFPS_UPS;
        startGameLoop();
    }

    private void initClasses() {
        // TODO: init all necessary Classes for the game (ex. AudioOptions, AudioPlayer, Menu, Playing, GameOptions)
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1e9 / FPS_SET;
        double timePerUpdate = 1e9 / UPS_SET;
        long prevTime = System.nanoTime();
        setLastCheck(System.currentTimeMillis());
        double UpdateDelta = 0;
        double FrameDelta = 0;

        while (true) {
            long currTime = System.nanoTime();
            UpdateDelta += (currTime - prevTime) / timePerUpdate;
            FrameDelta += (currTime - prevTime) / timePerFrame;
            prevTime = currTime;

            if (UpdateDelta >= 1) {
                update();
                setUpdates(getUpdates() + 1);
                UpdateDelta--;
            }

            if (FrameDelta >= 1) {
                gameView.repaint();
                setFrames(getFrames() + 1);
                FrameDelta--;
            }

            if (getSHOW_FPS_UPS() && System.currentTimeMillis() - getLastCheck() >= 1000) {
                showFPS();
            }
        }
    }

    private void showFPS() {
        setLastCheck(System.currentTimeMillis());
        System.out.println("FPS: " + getFrames() + " | UPS: " + getUpdates());
        setFrames(0);
        setUpdates(0);
    }

    private void update() {
        // TODO: Update game logic
    }

    public Boolean getSHOW_FPS_UPS() {
        return SHOW_FPS_UPS;
    }

    public long getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheck = lastCheck;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }
}
