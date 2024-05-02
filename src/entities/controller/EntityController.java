package entities.controller;
import constants.Constants;
import entities.logic.Finish;
import entities.logic.Player;
import entities.ui.FinishUI;
import entities.ui.PlayerUI;
import maps.controller.MapController;
import maps.logic.Map;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    private FinishUI finishUI;
    private Finish finish;

    public EntityController(boolean showHitBox, Point point, float mapWidth) {
        player = new Player(point.x, point.y);
        playerUI = new PlayerUI(player, showHitBox);
        finish = new Finish(mapWidth * 32 * Constants.TILE_SCALE - (32 * Constants.TILE_SCALE * 2), 576);
        finishUI = new FinishUI(finish, showHitBox);
    }

    public void update(MapController mapController) {
        player.update(mapController.getCurrentMap());
        finish.checkIfPlayerIsInFinish(player, mapController.getMapOffset());
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

    public void drawEntities(Graphics g, int offset) {
        playerUI.drawAnimations(g, offset);
        finishUI.drawHitBox(g, offset);
    }

    public Player getPlayer() {
        return player;
    }
}
