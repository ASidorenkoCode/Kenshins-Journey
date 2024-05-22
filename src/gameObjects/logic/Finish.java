package gameObjects.logic;

import entities.logic.Player;
import gameObjects.animations.ObjectAnimations;

import java.awt.geom.Rectangle2D;

public class Finish extends GameObject {

    private final static float HORIZONTAL_OFFSET = 50;

    private boolean isActive;

    public Finish(float x, float y, boolean isActive) {
        super(x, y - HORIZONTAL_OFFSET, new Rectangle2D.Float(x, y - HORIZONTAL_OFFSET, 64, 64 * 2), ObjectAnimations.FINISH);
        this.isActive = isActive;
    }
    public void updateFinishPoint(int x, int y, boolean isActive) {
        this.x = x;
        this.y = y - HORIZONTAL_OFFSET;
        this.hitbox.x = x;
        this.hitbox.y = y - HORIZONTAL_OFFSET;
        this.isActive = isActive;
    }

    public boolean checkIfPlayerIsInFinish(Player player) {
        if(!isActive) return false;
        return hitbox.intersects(player.getHitbox());
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
