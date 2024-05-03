package game.logic;

import game.controller.GameController;

public class GameEngine implements Runnable {

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Thread gameThread;
    private GameController gameController;
    private volatile boolean SHOW_FPS_UPS;
    private long lastCheck;
    private int frames = 0;
    private int updates = 0;
    private final Object lock = new Object();
    private boolean isPaused = false;


    public GameEngine(boolean showFPS_UPS, GameController gameController) {
        this.SHOW_FPS_UPS = showFPS_UPS;
        this.gameController = gameController;
    }

    private void initClasses() {
        // TODO: init all necessary Classes for the game (ex. AudioOptions, AudioPlayer, Menu, Playing, GameOptions)
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void resume() {
        synchronized (lock) {
            isPaused = false;
            lock.notify();
        }
    }

    public void pause() {
        synchronized (lock) {
            isPaused = true;
        }
    }

    @Override
    public void run() {

        final double timePerUpdate = 1e9 / UPS_SET;
        final double timePerFrame = 1e9 / FPS_SET;
        long prevTime = System.nanoTime();
        double updateAccumulator = 0;
        double frameAccumulator = 0;

        while (true) {
            synchronized (lock) {
                while (isPaused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread was interrupted, Failed to complete operation");
                    }
                }
            }
            long currTime = System.nanoTime();
            double elapsed = currTime - prevTime;
            prevTime = currTime;
            updateAccumulator += elapsed;
            frameAccumulator += elapsed;

            while (updateAccumulator >= timePerUpdate) {
                update();
                setUpdates(getUpdates() + 1);
                updateAccumulator -= timePerUpdate;
            }

            if (frameAccumulator >= timePerFrame) {
                gameController.callRepaint();
                setFrames(getFrames() + 1);
                frameAccumulator -= timePerFrame;
            }

            if (getSHOW_FPS_UPS() && System.currentTimeMillis() - getLastCheck() >= 1000) {
                gameController.showFPS_UPS();
                resetFPSandUPS();
            }
        }
    }

    private void resetFPSandUPS() {
        setLastCheck(System.currentTimeMillis());
        setFrames(0);
        setUpdates(0);
    }

    private void update() {
        gameController.update();
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
