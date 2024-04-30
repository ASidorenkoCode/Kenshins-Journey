package entities.logic;

import entities.animations.PlayerAnimations;
import entities.ui.PlayerUI;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Player extends Entity {

    private boolean left, right, jump, inAir;

    private float airMovement = -5f;
    private Rectangle2D.Float hitbox;
    private PlayerAnimations currentAnimation;

    public Player(float x, float y) {
        super(x, y);
        left = false;
        currentAnimation = PlayerAnimations.IDLE;
        //TODO: variables for values
        hitbox = new Rectangle2D.Float( x + 32 * 2f,  y + 16 * 2f,96 -64,96 - 48);
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
            updateXPos(1);
            updateAnimation(PlayerAnimations.RUN);
        }
        else if(left && !right) {
            updateXPos(-1);
            updateAnimation(PlayerAnimations.RUN);
        }
        if (inAir) {
            updateYPos(airMovement);
            //TODO Check if hit bottom
            airMovement += 0.1f;


            if(airMovement >= 0) updateAnimation(PlayerAnimations.FALL);


            //Placeholder if for stopping animation
            if (y > 200) {
                inAir = false;
                updateAnimation(PlayerAnimations.IDLE);
            }
        }
    }

    private void updateXPos(float by_value) {
        x += by_value;
        hitbox.x += by_value;
    }

    private void updateYPos(float by_value) {
        y += by_value;
        hitbox.y += by_value;
    }

    public void updateAnimation(PlayerAnimations animation) {
        currentAnimation = animation;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if(inAir) return;
        if(left && !right) updateAnimation(PlayerAnimations.RUN);
        else if(right) updateAnimation(PlayerAnimations.RUN);
        else updateAnimation(PlayerAnimations.IDLE);
        if(left && right) updateAnimation(PlayerAnimations.IDLE);
    }

    public void setRight(boolean right) {
        this.right = right;
        if(inAir) return;
        if(right && !left) updateAnimation(PlayerAnimations.RUN);
        else if (left) updateAnimation(PlayerAnimations.RUN);
        else updateAnimation(PlayerAnimations.IDLE);
        if(left && right) updateAnimation(PlayerAnimations.IDLE);
    }

    public void jump() {
        if (!inAir) {
            inAir = true;
            airMovement = -5f;
            updateAnimation(PlayerAnimations.JUMP);
        }
    }
    public PlayerAnimations getCurrentPlayerAnimation() {
        return currentAnimation;
    }
    public boolean getLeft() {
        return left;
    }
    public boolean getRight() {
        return right;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }


}
