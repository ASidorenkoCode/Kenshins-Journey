package keyboardinputs.logic;

import entities.controller.EntityController;
import game.UI.GameView;
import game.controller.GameController;
import game.controller.GameControls;
import items.controller.ItemController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyboardInputsIngame implements KeyListener {
    private GameView gameView;
    private Map<GameControls, Runnable> controlActions = new HashMap<>();


    public KeyboardInputsIngame(GameView gameView, GameController gameController) {
        this.gameView = gameView;
        initializeControlActionsOnPress(gameController.getEntityController(), gameController.getItemController(), gameController);
        initializeControlActionsOnRelease(gameController.getEntityController());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleUserInputKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleUserInputKeyRelease(e);
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

    public void handleUserInputKeyRelease(KeyEvent e) {
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

    private void initializeControlActionsOnPress(EntityController entityController, ItemController itemController, GameController gameController) {
        controlActions.put(GameControls.MOVE_RIGHT, () -> entityController.getPlayer().setRight(true));
        controlActions.put(GameControls.MOVE_LEFT, () -> entityController.getPlayer().setLeft(true));
        controlActions.put(GameControls.JUMP, () -> entityController.getPlayer().jump());
        controlActions.put(GameControls.ATTACK, () -> entityController.getPlayer().attack());
        controlActions.put(GameControls.DASH, () -> entityController.getPlayer().setIsDashing(true));
        controlActions.put(GameControls.REST, () -> entityController.getPlayer().setIsRestingIfNotInAir(true));
        controlActions.put(GameControls.ITEM_1, () -> itemController.selectItem(entityController.getPlayer(), 1));
        controlActions.put(GameControls.ITEM_2, () -> itemController.selectItem(entityController.getPlayer(), 2));
        controlActions.put(GameControls.ITEM_3, () -> itemController.selectItem(entityController.getPlayer(), 3));
        controlActions.put(GameControls.ITEM_4, () -> itemController.selectItem(entityController.getPlayer(), 4));
        controlActions.put(GameControls.ITEM_5, () -> itemController.selectItem(entityController.getPlayer(), 5));
        controlActions.put(GameControls.RESTART_AFTER_DEATH, gameController::restartLevelAfterDeath);

    }

    private void initializeControlActionsOnRelease(EntityController entityController) {
        controlActions.put(GameControls.MOVE_RIGHT, () -> entityController.getPlayer().setRight(false));
        controlActions.put(GameControls.MOVE_LEFT, () -> entityController.getPlayer().setLeft(false));
        controlActions.put(GameControls.REST, () -> entityController.getPlayer().setIsRestingIfNotInAir(false));
    }
}
