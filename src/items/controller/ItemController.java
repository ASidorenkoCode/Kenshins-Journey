package items.controller;

import entities.logic.Player;
import items.logic.Heart;
import items.ui.HeartUI;

import java.awt.*;

public class ItemController {

    private HeartUI heartUI;
    private Heart heart;

    public ItemController(boolean showHitBox) {
        heart = new Heart(700, 500);
        heartUI = new HeartUI(heart, showHitBox);
    }

    public void update(Player player) {
        heart.handleItem(player);
    }

    public void drawEntities(Graphics g, int offset) {
        heartUI.drawAnimations(g, offset);
    }
}
