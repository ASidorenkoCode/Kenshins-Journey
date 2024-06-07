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
        if (player.getHealth() >= player.getTotalHealth()) return false;
        if (player.getHealth() < player.getTotalHealth()) {
            player.setPlayerHealth(player.getHealth() + 2);
            return true;
        }
        return false;
    }
}
