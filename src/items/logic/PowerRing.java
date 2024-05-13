package items.logic;

import entities.logic.Player;
import items.animations.ItemAnimations;

import java.awt.geom.Rectangle2D;

public class PowerRing extends Item {
    public PowerRing(float x, float y) {
        super(x, y , new Rectangle2D.Float(x, y , 64, 64), ItemAnimations.POWER_RING);
    }

    @Override
    public void handleItem(Player player) {
        player.increaseMaximumDamagePerAttack(20);
    }
}
