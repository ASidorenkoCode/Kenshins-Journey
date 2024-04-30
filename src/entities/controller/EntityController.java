package entities.controller;

import entities.logic.Player;
import entities.ui.PlayerUI;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    public EntityController() {
        player = new Player(200,200);
        //TODO: Improve TileScale
        playerUI = new PlayerUI(player, 2f);
    }

    public void update() {
        player.update();
    }

    public void handleUserInput(KeyEvent e) {
        player.setLeft(true);
    }

    public void drawEntities(Graphics g) {
        playerUI.drawAnimations(g);
    }
}
