package items.logic;

import constants.Constants;
import entities.logic.Player;
import items.animations.ItemAnimations;

import java.awt.geom.Rectangle2D;

public class Heart extends Item {
    public Heart(float x, float y) {
        super(x, y , new Rectangle2D.Float(x, y , 32 * Constants.TILE_SCALE, 32 * Constants.TILE_SCALE), ItemAnimations.HEART);
    }

    @Override
    public void handleItem(Player player) {
        if(hitbox.intersects(player.getHitbox()) && isActive) {
            //TODO: Do something with the player
            if(player.getPlayerHealth() < player.getTotalHearts() * 2) player.setPlayerHealth(player.getPlayerHealth()+2);
            setActive(false);
        }
    }
}
