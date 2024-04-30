package entities.logic;

import entities.animations.PlayerAnimations;

public class Player extends Entity {

    private boolean left, right;

    private PlayerAnimations currentAnimation;

    public Player(float x, float y) {
        super(x, y);
        left = false;
        currentAnimation = PlayerAnimations.IDLE;
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
        if(right) x++;
        if(left) x--;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if(left) currentAnimation = PlayerAnimations.RUN;
        else currentAnimation = PlayerAnimations.IDLE;
    }

    public void setRight(boolean right) {
        this.right = right;
        if(right) currentAnimation = PlayerAnimations.RUN;
        else currentAnimation = PlayerAnimations.IDLE;
    }

    public PlayerAnimations currentPlayerAnimation() {
        return currentAnimation;
    }

    public boolean getLeft() {
        return left;
    }
    public boolean getRight() {
        return right;
    }
}
