package entities.logic;

public class Player extends Entity {
    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    void updatePushback() {

    }

    @Override
    void initHitbox() {

    }
}
