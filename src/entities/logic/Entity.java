package entities.logic;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;

    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, Rectangle2D.Float hitbox) {
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
    }

    abstract void updatePushback();

    abstract void update();


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

}
