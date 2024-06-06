package gameObjects.logic;

import gameObjects.animations.ObjectAnimations;

import java.awt.geom.Rectangle2D;

public class GameObject {
    protected float x, y;
    protected ObjectAnimations objectAnimation;

    protected Rectangle2D.Float hitbox;

    public GameObject(float x, float y, Rectangle2D.Float hitbox, ObjectAnimations objectAnimation) {
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
        this.objectAnimation = objectAnimation;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public ObjectAnimations getObjectAnimation() {
        return objectAnimation;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
