package items.logic;

import entities.logic.Player;
import items.animations.ItemAnimations;

import java.awt.geom.Rectangle2D;

public class Heart extends Item {
    public Heart(float x, float y) {
        super(x, y , new Rectangle2D.Float(x, y , 64, 64), ItemAnimations.HEART);
    }

    @Override
    public void handleItem(Player player) {
        if(player.getPlayerHealth() < player.getTotalHearts() * 2) player.setPlayerHealth(player.getPlayerHealth()+2);
    }
}
