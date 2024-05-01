package entities.logic;

import constants.Constants;
import entities.animations.PlayerAnimations;
import entities.ui.PlayerUI;
import maps.logic.Map;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class Player extends Entity {

    private boolean left, right, jump, inAir;
    private float airMovement = -5f;
    private PlayerAnimations currentAnimation;

    public Player(float x, float y) {
        super(x, y, new Rectangle2D.Float( x + 32 * Constants.TILE_SCALE,  y + 16 * Constants.TILE_SCALE,(96 - 64) * Constants.TILE_SCALE,(96 - 48) * Constants.TILE_SCALE));
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
    public void update(Map map) {

        if(right && !left) {
            updateXPos(map, 1);
            updateAnimation(PlayerAnimations.RUN);
        }
        else if(left && !right) {
            updateXPos(map, -1);
            updateAnimation(PlayerAnimations.RUN);
        }
        if (inAir) {
            updateYPos(airMovement);
            //TODO Check if hit bottom
            airMovement += 0.1f;


            if(airMovement >= 0) updateAnimation(PlayerAnimations.FALL);

        }
    }

    private void updateXPos(Map map, float by_value) {
        if(!checkIfPlayerCanMoveToPosition(map, hitbox.x + by_value, hitbox.y, hitbox.width, hitbox.height)) return;
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

    public boolean checkCollisionForPosition(Map map, float x, float y) {
        if(x < 0) return true;
        if(y < 0) return true;

        //TODO: Constants for Tile width and height
        int[][] mapData = map.getMapData();
        int tile_x = (int) (x / 64);
        int tile_y = (int) (y / 64);
        return mapData[tile_y][tile_x] != 11;
    }

    private boolean checkIfPlayerCanMoveToPosition(Map map, float x, float y, float width, float height) {
        if (!checkCollisionForPosition(map, x,y))
            if (!checkCollisionForPosition(map, x+width, y))
                if (!checkCollisionForPosition(map, x,y + height))
                    if (!checkCollisionForPosition(map, x+width,y+height))
                        return true;
        return false;
    }


}
