package entities.logic;

public abstract class Entity {
    protected float x, y;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    abstract void updatePushback();

    abstract void initHitbox();
}
