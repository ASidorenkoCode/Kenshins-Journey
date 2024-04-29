package entities.logic;

public class Player extends Entity {
    public Player(float x, float y) {
        super(x, y);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    void updatePushback() {

    }

    @Override
    void initHitbox() {

    }
}
