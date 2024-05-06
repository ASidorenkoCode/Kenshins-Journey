package entities.logic;

import constants.Constants;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class Finish extends Entity {

    private final static float HORIZONTAL_OFFSET = 25 * Constants.TILE_SCALE; //needed because

    public Finish(float x, float y) {
        super(x, y - HORIZONTAL_OFFSET, new Rectangle2D.Float(x, y - HORIZONTAL_OFFSET, 32 * Constants.TILE_SCALE, (32 * Constants.TILE_SCALE) * 2));
    }

    @Override
    void updatePushback() {
        //Not needed for finish
    }

    @Override
    boolean isDead() {
        return false;
    }

    public void updateFinishPoint(int x, int y) {
        this.x = x;
        this.y = y - HORIZONTAL_OFFSET;
        this.hitbox.x = x;
        this.hitbox.y = y - HORIZONTAL_OFFSET;
    }

    public boolean checkIfPlayerIsInFinish(Player player) {
        return hitbox.intersects(player.getHitbox());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
