package gameObjects.logic;

import entities.logic.Player;
import gameObjects.animations.ObjectAnimations;

import java.awt.geom.Rectangle2D;

public class Finish extends GameObject {

    private final static float HORIZONTAL_OFFSET = 50;

    public Finish(float x, float y) {
        super(x, y - HORIZONTAL_OFFSET, new Rectangle2D.Float(x, y - HORIZONTAL_OFFSET, 64, 64 * 2), ObjectAnimations.FINISH);
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
}
