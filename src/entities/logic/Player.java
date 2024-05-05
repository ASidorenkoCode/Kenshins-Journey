package entities.logic;

import constants.Constants;
import entities.ui.PlayerUI;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class Player extends Entity {

    private boolean left, right, attack, inAir, attackHitBoxIsActive;
    private float airMovement = -5f;
    private Rectangle2D.Float rightAttackHitBox;
    private Rectangle2D.Float leftAttackHitBox;
    private boolean hasDynamicAdjustedPlayerDirectionHitbox = false;
    private boolean isDead = false;
    private boolean hasAttacked = false;
    private int maximumDamagePerAttack = 20;
    private int damageDealtInCurrentAttack = 0;
    private int playerHealth = 2;
    private int currentMaxHearts = 3;
    private int totalMaxHearts = 3;


    public Player(float x, float y) {
        super(x, y, new Rectangle2D.Float(x + 25 * Constants.TILE_SCALE, y + 16 * Constants.TILE_SCALE, (96 - 69) * Constants.TILE_SCALE, (96 - 48) * Constants.TILE_SCALE));
        rightAttackHitBox = new Rectangle2D.Float((x + 25 * Constants.TILE_SCALE) + 32 * Constants.TILE_SCALE, y + 8 * Constants.TILE_SCALE, (96 - 64) * Constants.TILE_SCALE, (96 - 48) * Constants.TILE_SCALE);
        leftAttackHitBox = new Rectangle2D.Float((x + 25 * Constants.TILE_SCALE) - 32 * Constants.TILE_SCALE, y + 8 * Constants.TILE_SCALE, (96 - 64) * Constants.TILE_SCALE, (96 - 48) * Constants.TILE_SCALE);
        left = false;
        right = false;
        inAir = false;
        attack = false;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    void updatePushback() {

    }

    public void updateSpawnPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.hitbox.x = x + 32 * Constants.TILE_SCALE;
        this.hitbox.y = y + 16 * Constants.TILE_SCALE;
        this.leftAttackHitBox.x = (x + 32 * Constants.TILE_SCALE) - 32 * Constants.TILE_SCALE;
        this.leftAttackHitBox.y = y + 8 * Constants.TILE_SCALE;
        this.rightAttackHitBox.x = (x + 32 * Constants.TILE_SCALE) + 32 * Constants.TILE_SCALE;
        this.rightAttackHitBox.y = y + 8 * Constants.TILE_SCALE;
    }

    @Override
    public void update(Map map) {
        if (!isDead()) {
            if (right && !left) {
                updateXPos(map, 1);
            } else if (left && !right) {
                updateXPos(map, -1);
            }
            if (inAir) {
                updateYPos(map, airMovement);
                if (inAir) {
                    airMovement += 0.1f;
                }

            } else if (!checkIfPlayerCollidesUnderHim(map, hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height)) {
                airMovement = 0;
                inAir = true;
            }
        }
    }

    private void updateXPos(Map map, float by_value) {
        if (!checkIfPlayerCanMoveToPosition(map, hitbox.x + by_value, hitbox.y, hitbox.width, hitbox.height)) return;
        x += by_value;
        hitbox.x += by_value;
        adjustPlayerHitboxPosition(map);
        rightAttackHitBox.x = hitbox.x + hitbox.width;
        leftAttackHitBox.x = hitbox.x - leftAttackHitBox.width;
    }

    private void adjustPlayerHitboxPosition(Map map) {
        if (getLeft() && !getRight() && !hasDynamicAdjustedPlayerDirectionHitbox) {
            if (checkIfPlayerCanMoveToPosition(map, hitbox.x + 20, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x += 20;
                hasDynamicAdjustedPlayerDirectionHitbox = true;
            }
        } else if (!getLeft() && getRight() && hasDynamicAdjustedPlayerDirectionHitbox) {
            if (checkIfPlayerCanMoveToPosition(map, hitbox.x - 20, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x -= 20;
                hasDynamicAdjustedPlayerDirectionHitbox = false;
            }
        }
    }

    public void collisionWithEntity(Entity entity, PlayerUI playerUI) {
        if (collidesWith(entity)) {
            float newPosX = calculateNewPosition(entity);
            getHitbox().x = newPosX;
            setX(newPosX - 57);
            getRightAttackHitBox().x = newPosX + 64;
            getLeftAttackHitBox().x = newPosX - 64;
            // TODO: make a stable damage output method, because this one sometimes work and sometimes not
            if (entity instanceof BigOrc && getAttackHitBoxIsActive() && !hasAttacked && damageDealtInCurrentAttack < maximumDamagePerAttack) {
                ((BigOrc) entity).decreaseHealth(10);
                damageDealtInCurrentAttack += 10;
                hasAttacked = true;
            } else if (hasAttacked && playerUI.getCurrentAniIndex() == 6) {
                hasAttacked = false;
                damageDealtInCurrentAttack = 0;
            }
        }
    }

    private void updateYPos(Map map, float by_value) {
        if (checkIfPlayerCollidesOverHim(map, hitbox.x, hitbox.y + by_value, hitbox.width)) {

            airMovement = 0;
            return;

        } else if (checkIfPlayerCollidesUnderHim(map, hitbox.x, hitbox.y + by_value, hitbox.width, hitbox.height)) {
            inAir = false;

            float playerYPos = (hitbox.y + by_value + hitbox.height);
            int groundSpriteNumber = (int) (playerYPos / (32 * Constants.TILE_SCALE));
            hitbox.y = groundSpriteNumber * (32 * Constants.TILE_SCALE);
            hitbox.y -= hitbox.height + 1;
            y = hitbox.y;
            y -= 16 * Constants.TILE_SCALE;

            return;
        }
        y += by_value;
        hitbox.y += by_value;
        rightAttackHitBox.y = hitbox.y - 16;
        leftAttackHitBox.y = hitbox.y - 16;
    }

    public void jump() {
        if (!inAir) {
            inAir = true;
            airMovement = -5f;
        }
    }

    public void attack() {
        if (!attack) {
            setAttack(true);
        }
    }

    public boolean getLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean getAttackHitBoxIsActive() {
        return attackHitBoxIsActive;
    }

    public void setAttackHitBoxIsActive(boolean attackHitBoxIsActive) {
        this.attackHitBoxIsActive = attackHitBoxIsActive;
    }

    public boolean collidesWith(Entity entity) {
        return this.hitbox.intersects(entity.getHitbox().getBounds2D());
    }

    public boolean checkForCollisonOnPosition(Map map, float x, float y) {
        if (x < 0) return true;
        if (y < 0) return true;

        //TODO: Constants for Tile width and height
        int[][] mapData = map.getMapData();
        int tile_x = (int) (x / 64);
        int tile_y = (int) (y / 64);
        return mapData[tile_y][tile_x] != 11;
    }

    private boolean checkIfPlayerCanMoveToPosition(Map map, float x, float y, float width, float height) {
        if (checkIfPlayerCollidesOverHim(map, x, y, width))
            return false;
        return !checkIfPlayerCollidesUnderHim(map, x, y, width, height);
    }

    private boolean checkIfPlayerCollidesUnderHim(Map map, float x, float y, float width, float height) {
        if (!checkForCollisonOnPosition(map, x, y + height))
            if (!checkForCollisonOnPosition(map, x + width, y + height))
                return false;
        return true;
    }

    private boolean checkIfPlayerCollidesOverHim(Map map, float x, float y, float width) {
        if (!checkForCollisonOnPosition(map, x, y))
            if (!checkForCollisonOnPosition(map, x + width, y))
                return false;
        return true;
    }

    public Rectangle2D.Float getRightAttackHitBox() {
        return rightAttackHitBox;
    }

    public Rectangle2D.Float getLeftAttackHitBox() {
        return leftAttackHitBox;
    }

    public boolean getInAir() {
        return inAir;
    }

    public boolean getAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {

        this.attack = attack;

    }

    public float getAirMovement() {
        return airMovement;
    }

    private float calculateNewPosition(Entity entity) {
        if (getHitbox().x < entity.getHitbox().x) {
            return entity.getHitbox().x - getHitbox().width;
        } else {
            return entity.getHitbox().x + entity.getHitbox().width;
        }
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void decreaseHealth() {
        if (playerHealth > 0) {
            playerHealth--;
        }
    }

    public boolean isDead() {
        return playerHealth == 0;
    }

    public boolean getDeathAnimationFinished() {
        return this.isDead;
    }

    public void setDeathAnimationFinished(boolean isDead) {
        this.isDead = isDead;
    }

    public int resetHealth() {
        return playerHealth = totalMaxHearts * 2;
    }

    public int getTotalHearts() {
        return totalMaxHearts;
    }

    public void setTotalHearts(int totalHearts) {
        this.totalMaxHearts = totalHearts;
    }
}
