package keyboardinputs.logic;

import game.UI.GameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyboardInputsIngame implements KeyListener {
    private GameView gameView;

    public KeyboardInputsIngame(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            gameView.handleUserInputKeyPressed(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameView.handleUserInputKeyReleased(e);
    }
}
