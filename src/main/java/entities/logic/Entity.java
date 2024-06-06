package entities.logic;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected Rectangle2D.Float hitbox;
    protected boolean isDead;
    protected int health;

    public Entity(float x, float y, Rectangle2D.Float hitbox, boolean isDead, int health) {
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
        this.isDead = isDead;
        this.health = health;
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public int getHealth() {
        return health;
    }
}
