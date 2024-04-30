package entities.logic;

public class Player extends Entity {

    private boolean left;
    public Player(float x, float y) {
        super(x, y);
        left = false;
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
    @Override
    public void update() {
        if(left) x++;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}
