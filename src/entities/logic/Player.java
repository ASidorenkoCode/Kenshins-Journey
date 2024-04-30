package entities.logic;

import entities.animations.PlayerAnimations;
import entities.ui.PlayerUI;

import java.awt.*;

public class Player extends Entity {

    private boolean left, right;
    private Rectangle hitbox;
    private PlayerAnimations currentAnimation;

    private PlayerAnimations lastAnimation;

    public Player(float x, float y) {
        super(x, y);
        left = false;
        currentAnimation = PlayerAnimations.IDLE;
        lastAnimation = PlayerAnimations.IDLE;
        //TODO: variables for values
        hitbox = new Rectangle((int) x +64 , (int) (y + 16 * 2f),96 -64,96 - 48);
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
        if(right && !left) {
            x++;
            hitbox.x++;
        }
        else if(left && !right) {
            x--;
            hitbox.x--;
        }
        else updateAnimation(PlayerAnimations.IDLE);
    }

    public void updateAnimation(PlayerAnimations animation) {
        lastAnimation = currentAnimation;
        currentAnimation = animation;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if(left && !right) updateAnimation(PlayerAnimations.RUN);
        else if(right) updateAnimation(PlayerAnimations.RUN);
        else updateAnimation(PlayerAnimations.IDLE);
    }

    public void setRight(boolean right) {
        this.right = right;
        if(right && !left) updateAnimation(PlayerAnimations.RUN);
        else if (left) updateAnimation(PlayerAnimations.RUN);
        else updateAnimation(PlayerAnimations.IDLE);
    }

    public PlayerAnimations getCurrentPlayerAnimation() {
        return currentAnimation;
    }
    public PlayerAnimations getLastPlayerAnimation() {
        return lastAnimation;
    }

    public boolean getLeft() {
        return left;
    }
    public boolean getRight() {
        return right;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }


}
