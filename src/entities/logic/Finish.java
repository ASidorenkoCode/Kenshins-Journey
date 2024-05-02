package entities.logic;

import constants.Constants;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class Finish extends Entity {

    public Finish(float x, float y) {
        super(x, y, new Rectangle2D.Float(x,  y,32 * Constants.TILE_SCALE,32 * Constants.TILE_SCALE));
    }

    @Override
    void updatePushback() {
        //Not needed for finish
    }

    @Override
    void update(Map map) {

    }

    public void checkIfPlayerIsInFinish(Player player, int offset) {
        Rectangle2D.Float playerHitbox = new Rectangle2D.Float(player.getHitbox().x - offset, player.getHitbox().y, player.getHitbox().width, player.getHitbox().height);
        if(hitbox.intersects(playerHitbox)) System.out.println("player hits finish");
    }
}
