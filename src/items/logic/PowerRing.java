package items.logic;

import constants.Constants;
import entities.logic.Player;
import items.animations.ItemAnimations;

import java.awt.geom.Rectangle2D;

public class PowerRing extends Item {
    public PowerRing(float x, float y) {
        super(x, y , new Rectangle2D.Float(x, y , 32 * Constants.TILE_SCALE, 32 * Constants.TILE_SCALE), ItemAnimations.POWER_RING);
    }

    @Override
    public void handleItem(Player player) {
        //TODO: increase player attack
    }
}
