package entities.controller;
import entities.logic.Player;
import entities.ui.PlayerUI;
import maps.logic.Map;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    public EntityController(boolean showHitBox) {
        player = new Player(100,500);
        playerUI = new PlayerUI(player, showHitBox);
    }

    public void update(Map map) {
        player.update(map);
    }

    public void handleUserInputKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.jump();
        }
    }

    public void handleUserInputKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
        }
    }

    public void drawEntities(Graphics g) {
        playerUI.drawAnimations(g);
    }
}
