package game.controller;

import java.awt.event.KeyEvent;

public enum GameControls {
    MOVE_RIGHT("Move right", KeyEvent.VK_D, true),
    MOVE_LEFT("Move left", KeyEvent.VK_A, true),
    JUMP("Jump", KeyEvent.VK_SPACE, true),
    ATTACK("Attack", KeyEvent.VK_SHIFT, true),
    DASH("Dash", KeyEvent.VK_S, true),
    REST("Rest", KeyEvent.VK_R, true),
    ITEM_1("Item 1", KeyEvent.VK_1, true),
    ITEM_2("Item 2", KeyEvent.VK_2, true),
    ITEM_3("Item 3", KeyEvent.VK_3, true),
    ITEM_4("Item 4", KeyEvent.VK_4, true),
    ITEM_5("Item 5", KeyEvent.VK_5, true),
    RESTART_AFTER_DEATH("Restart after death", KeyEvent.VK_L, true),
    OPEN_HIGHSCORE_TABLE("Open highscore table", KeyEvent.VK_O, true),
    ACTIVATE_MULTIPLAYER("Activate multiplayer", KeyEvent.VK_M, true);

    private String controlName;
    private int keyCode;
    private boolean isChangeable;

    GameControls(String controlName, int keyCode, boolean isChangeable) {
        this.controlName = controlName;
        this.keyCode = keyCode;
        this.isChangeable = isChangeable;

    }

    public static GameControls getControlByCode(int keyCode) {
        for (GameControls control : values()) {
            if (control.getKeyCode() == keyCode) {
                return control;
            }
        }
        return null;
    }

    public boolean isChangeable() {
        return isChangeable;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
