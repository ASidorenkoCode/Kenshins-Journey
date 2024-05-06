package items.logic;

import entities.logic.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


//TODO: Maybe another parent class for entity and item
public abstract class Item {

    protected float x, y;
    protected Rectangle2D.Float hitbox;
    protected boolean isActive;

    public Item(float x, float y, Rectangle2D.Float hitbox) {
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
        this.isActive = true;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public boolean getIsActive() {
        return isActive;
    }
    abstract void handleItem(Player player);
}
