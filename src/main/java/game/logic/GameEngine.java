package game.logic;

import game.controller.GameController;

import java.io.IOException;

public class GameEngine implements Runnable {

    private Thread gameThread;
    private GameController gameController;
    private final int UPS_SET = 200;
    private long lastCheck;
    private int frames = 0;
    private int updates = 0;

    public GameEngine(GameController gameController) {
        this.gameController = gameController;
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        final double timePerUpdate = 1e9 / UPS_SET;
        long prevTime = System.nanoTime();
        double updateAccumulator = 0;

        while (true) {

            long currTime = System.nanoTime();
            double elapsed = currTime - prevTime;
            prevTime = currTime;
            updateAccumulator += elapsed;

            while (updateAccumulator >= timePerUpdate) {
                updateAccumulator -= timePerUpdate;
                try {
                    gameController.update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                updates++;
            }

            gameController.callRepaint();
            frames++;
        }
    }

    public int getFrames() {
        return frames;
    }

    public int getUpdates() {
        return updates;
    }
}