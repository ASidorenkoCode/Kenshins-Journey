package items.logic;

import entities.logic.Player;
import java.awt.geom.Rectangle2D;


//TODO: Maybe another parent class for entity and item
public abstract class Item {

    protected float x, y;
    protected Rectangle2D.Float hitbox;

    public Item(float x, float y, Rectangle2D.Float hitbox) {
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    abstract void handleItem(Player player);
}
