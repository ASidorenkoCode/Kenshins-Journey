package items.logic;

import entities.logic.Player;
import items.animations.ItemAnimations;

import java.awt.geom.Rectangle2D;

public class Heart extends Item {
    public Heart(float x, float y) {
        super(x, y , new Rectangle2D.Float(x, y , 64, 64), ItemAnimations.HEART);
    }

    @Override
    public boolean handleItem(Player player) {
        if (player.getHealth() >= player.getTotalHearts() * 2) return false;
        if (player.getHealth() < player.getTotalHearts() * 2) {
            player.setPlayerHealth(player.getHealth() + 2);
            return true;
        }
        return false;
    }
}
