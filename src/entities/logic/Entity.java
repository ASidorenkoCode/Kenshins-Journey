package entities.logic;

import maps.controller.MapController;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected Rectangle2D.Float hitbox;
    protected boolean isDead;

    public Entity(float x, float y, Rectangle2D.Float hitbox, boolean isDead) {
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
        this.isDead = isDead;
    }


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public boolean isDead() {
        return isDead;
    }
}
