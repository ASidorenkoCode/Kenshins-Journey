package entities.logic;

import constants.Constants;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class Finish extends Entity {

    public Finish(float x, float y) {
        super(x, y, new Rectangle2D.Float(x, y, 32 * Constants.TILE_SCALE, 32 * Constants.TILE_SCALE));
    }

    @Override
    void updatePushback() {
        //Not needed for finish
    }

    @Override
    void update(Map map) {

    }

    public void updateFinishPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.hitbox.x = x;
        this.hitbox.y = y;
    }

    public boolean checkIfPlayerIsInFinish(Player player) {
        return hitbox.intersects(player.getHitbox());
    }
}
