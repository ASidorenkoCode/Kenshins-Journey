package entities.logic;

import constants.Constants;
import entities.animations.PlayerAnimations;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class Player extends Entity {

    private boolean left, right, attack, inAir;
    private float airMovement = -5f;
    private PlayerAnimations currentAnimation;
    private PlayerAnimations lastAnimation;

    public Player(float x, float y) {
        super(x, y, new Rectangle2D.Float( x + 32 * Constants.TILE_SCALE,  y + 16 * Constants.TILE_SCALE,(96 - 64) * Constants.TILE_SCALE,(96 - 48) * Constants.TILE_SCALE));
        left = false;
        right = false;
        inAir = false;
        attack = false;
        currentAnimation = PlayerAnimations.IDLE;
        lastAnimation = PlayerAnimations.IDLE;
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
        }
        else if(left && !right) {
            updateXPos(map, -1);
        }
        if (inAir) {
            updateYPos(map, airMovement);
            if(inAir) {
                airMovement += 0.1f;


                if(airMovement >= 0) updateAnimation(PlayerAnimations.FALL);
            }

        } else if(!checkIfPlayerCollidesUnderHim(map, hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height)) {
            airMovement = 0;
            inAir = true;
        }
    }

    private void updateXPos(Map map, float by_value) {
        if(!checkIfPlayerCanMoveToPosition(map, hitbox.x + by_value, hitbox.y, hitbox.width, hitbox.height)) return;
        x += by_value;
        hitbox.x += by_value;
    }

    private void updateYPos(Map map, float by_value) {
        if(checkIfPlayerCollidesOverHim(map, hitbox.x, hitbox.y + by_value, hitbox.width)) {

            airMovement = 0;
            return;

        } else if(checkIfPlayerCollidesUnderHim(map, hitbox.x, hitbox.y + by_value, hitbox.width, hitbox.height)) {
            inAir = false;
            updateAnimation(PlayerAnimations.IDLE);
            if((left && !right)|| (!left && right)) updateAnimation(PlayerAnimations.RUN);

            //set player to position of ground

            float playerYPos =  (hitbox.y + by_value + hitbox.height);
            int groundSpriteNumber = (int) (playerYPos / (32 * Constants.TILE_SCALE));
            hitbox.y = groundSpriteNumber * (32 * Constants.TILE_SCALE);
            hitbox.y -= hitbox.height + 1;
            y = hitbox.y;
            y -= 16 * Constants.TILE_SCALE;

            return;
        }
        y += by_value;
        hitbox.y += by_value;
    }

    public void updateAnimation(PlayerAnimations animation) {
        lastAnimation = currentAnimation;
        currentAnimation = animation;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if(currentAnimation == PlayerAnimations.RUN_SLASH || inAir) return;
        if(left && !right) updateAnimation(PlayerAnimations.RUN);
        else if(right) updateAnimation(PlayerAnimations.RUN);
        else updateAnimation(PlayerAnimations.IDLE);
        if(left && right) updateAnimation(PlayerAnimations.IDLE);
    }

    public void setRight(boolean right) {
        this.right = right;
        if(currentAnimation == PlayerAnimations.RUN_SLASH || inAir) return;
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

    public void attack() {
        if(!attack && !inAir) {
            setAttack(true);
            //TODO: Different attack animations based on running etc.
            updateAnimation(PlayerAnimations.RUN_SLASH);
            if((left && right) || (!left && !right)) updateAnimation(PlayerAnimations.IDLE_SLASH);
        }
    }
    public PlayerAnimations getCurrentPlayerAnimation() {
        return currentAnimation;
    }

    public PlayerAnimations getLastAnimation() {
        return lastAnimation;
    }
    public boolean getLeft() {
        return left;
    }
    public boolean getRight() {
        return right;
    }

    public void setAttack(boolean attack) {
        if(this.attack && !attack) {
            if(currentAnimation == PlayerAnimations.RUN_SLASH) updateAnimation(PlayerAnimations.RUN);
            else updateAnimation(PlayerAnimations.IDLE);
        }
        this.attack = attack;

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
        if(checkIfPlayerCollidesOverHim(map, x, y, width))
            return false;
        return !checkIfPlayerCollidesUnderHim(map, x, y, width, height);
    }

    private boolean checkIfPlayerCollidesUnderHim(Map map, float x, float y, float width, float height) {
        if (!checkCollisionForPosition(map, x,y + height))
            if (!checkCollisionForPosition(map, x+width,y+height))
                return false;
        return true;
    }

    private boolean checkIfPlayerCollidesOverHim(Map map, float x, float y, float width) {
        if (!checkCollisionForPosition(map, x,y))
            if (!checkCollisionForPosition(map, x+width,y))
                return false;
        return true;
    }


}
