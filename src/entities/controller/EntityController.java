package entities.controller;

import entities.logic.Player;
import entities.ui.PlayerUI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    public EntityController(boolean showHitBox) {
        player = new Player(200,200);
        //TODO: Improve TileScale
        playerUI = new PlayerUI(player, 2f, showHitBox);
    }

    public void update() {
        player.update();
    }

    public void handleUserInputKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
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
