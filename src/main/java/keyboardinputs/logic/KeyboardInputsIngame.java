package keyboardinputs.logic;

import entities.controller.EntityController;
import game.UI.GameView;
import game.controller.GameController;
import game.controller.GameControls;
import items.controller.ItemController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyboardInputsIngame implements KeyListener {
    private GameView gameView;
    private Map<GameControls, Runnable> controlActions = new HashMap<>();


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

    public void handleUserInputKeyPressed(KeyEvent e) {
        GameControls control = GameControls.getControlByCode(e.getKeyCode());
        if (control != null) {
            Runnable action = controlActions.get(control);
            if (action != null) {
                action.run();
            }
        }
    }

    public void changeControl(GameControls control, int newKeyCode) {
        if (control.isChangeable()) {
            control.setKeyCode(newKeyCode);
        }
    }

    private void initializeControlActions(EntityController entityController, ItemController itemController, GameController gameController) {
        // Player Controls
        controlActions.put(GameControls.MOVE_RIGHT, () -> entityController.getPlayer().setRight(true));
        controlActions.put(GameControls.MOVE_LEFT, () -> entityController.getPlayer().setLeft(true));
        controlActions.put(GameControls.ITEM_1, () -> itemController.selectItem(entityController.getPlayer(), 1));
        controlActions.put(GameControls.ITEM_2, () -> itemController.selectItem(entityController.getPlayer(), 2));
        controlActions.put(GameControls.ITEM_3, () -> itemController.selectItem(entityController.getPlayer(), 3));
        controlActions.put(GameControls.ITEM_4, () -> itemController.selectItem(entityController.getPlayer(), 4));
        controlActions.put(GameControls.ITEM_5, () -> itemController.selectItem(entityController.getPlayer(), 5));


        // GUI Controls
        controlActions.put(GameControls.RESTART_AFTER_DEATH, () -> gameController.restartLevelAfterDeath());

    }
}
