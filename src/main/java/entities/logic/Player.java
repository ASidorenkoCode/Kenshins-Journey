package entities.logic;
import game.UI.GameView;
import game.logic.Highscore;
import maps.logic.Map;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Player extends Entity {

    private boolean left, right, attack, inAir, attackHitBoxIsActive, isResting, isDashing, isFacingRight;
    private float airMovement = -6f;
    private Rectangle2D.Float rightAttackHitBox;
    private Rectangle2D.Float leftAttackHitBox;
    private boolean hasDynamicAdjustedPlayerDirectionHitbox = false;
    private boolean hasAttacked = false;
    private int currentDamagePerAttack = 20;
    private int damageDealtInCurrentAttack = 0;
    private int currentMaxHearts = 3;
    private int totalMaxHearts = 3;
    private boolean isHitByEnemy = false;
    private boolean deathAnimationFinished = false;

    private final static int STANDARD_DAMAGE = 20;

    //movement variables
    private final static float MAX_TIME = 1;
    private float currentGroundMovement = 0;
    private float time = 0;

    private boolean canDoubleJump = false;
    private boolean hasDoubleJumped = false;

    //attack variables


    public Player(float x, float y) {
        super(x, y, new Rectangle2D.Float(x + 50, y + 32, (96 - 69) * 2, (96 - 48) * 2), false, 2);
        rightAttackHitBox = new Rectangle2D.Float((x + 50) + 64, y + 16, (96 - 64) * 2, (96 - 48) * 2);
        leftAttackHitBox = new Rectangle2D.Float((x + 50) - 64, y + 16, (96 - 64) * 2, (96 - 48) * 2);
        left = false;
        right = false;
        inAir = false;
        attack = false;
        isFacingRight = true;
        resetMaximumDamagePerAttack();
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


    private void updateGroundMovement() {

        if(time < MAX_TIME) {
            //TODO: Improve speed difference
            time += 0.05f;
            currentGroundMovement = time * time / (time * time + (1 - time) * (1 - time));
        }
    }


    public void updateSpawnPoint(int x, int y) {
        this.x = x;
        this.y = y;
        this.hitbox.x = x + 50;
        this.hitbox.y = y + 32;
        this.leftAttackHitBox.x = (x + 50) - 64;
        this.leftAttackHitBox.y = y + 16;
        this.rightAttackHitBox.x = (x + 50) + 64;
        this.rightAttackHitBox.y = y + 16;
        resetMaximumDamagePerAttack();
    }

    public void update(Map map, Boss boss, ArrayList<Kappa> kappas, Highscore highscore) {

        if (highscore.getCurrentHighscore() == 0) {
            setPlayerHealth(0);
        }


        if(!attack) {
            attackHitBoxIsActive = false;
        } else {
            handlePlayerAttacksEntity(boss);
            for (Kappa kappa: kappas) {
                handlePlayerAttacksEntity(kappa);
            }
        }


        if(isResting) {
        //TODO: better suiting resting
        if(health<totalMaxHearts*2) {
            health++;
        }
        return;
        }

        if (!getDeathAnimationFinished()) {

            float currentSpeed = currentGroundMovement;
            if(isDashing) {
                currentSpeed*=2;
            }
            if (right && !left) {
                isFacingRight = true;
                updateXPos(map, boss, kappas, currentSpeed);
                updateGroundMovement();
            } else if (left && !right) {
                updateXPos(map, boss, kappas, -currentSpeed);
                updateGroundMovement();
                isFacingRight = false;
            } else time = 0;

            if (inAir && !isDashing) {

                updateYPos(map, boss, kappas, airMovement);

                if (inAir) {
                    airMovement += 0.1f;
                }

            } else if (!checkIfPlayerCollidesUnderHim(map, boss, kappas, hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height)) {
                airMovement = 0;
                inAir = true;
            }
            // TODO: implement new y position, for now It's just a workaround with times 3
            if (this.getHitbox().x < 0 || this.getHitbox().y > GameView.GAME_HEIGHT*3) {
                this.decreaseHealth(getHealth());
            }
        }
    }



    private void updateXPos(Map map, Boss boss, ArrayList<Kappa> kappas, float byValue) {
        if (!checkIfPlayerCanMoveToPosition(map, boss, kappas, hitbox.x + byValue, hitbox.y, hitbox.width, hitbox.height)) return;
        x += byValue;
        hitbox.x += byValue;
        adjustPlayerHitboxPosition(map, boss, kappas);
        rightAttackHitBox.x = hitbox.x + hitbox.width;
        leftAttackHitBox.x = hitbox.x - leftAttackHitBox.width;
    }

    private void adjustPlayerHitboxPosition(Map map, Boss boss, ArrayList<Kappa> kappas) {
        //TODO: Change hitbox movement bug
        if (getLeft() && !getRight() && !hasDynamicAdjustedPlayerDirectionHitbox) {
            if (checkIfPlayerCanMoveToPosition(map, boss, kappas,hitbox.x + 20, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x += 20;
                hasDynamicAdjustedPlayerDirectionHitbox = true;
            }
        } else if (!getLeft() && getRight() && hasDynamicAdjustedPlayerDirectionHitbox) {
            if (checkIfPlayerCanMoveToPosition(map, boss,kappas, hitbox.x - 20, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x -= 20;
                hasDynamicAdjustedPlayerDirectionHitbox = false;
            }
        }
    }


    public int getCurrentDamagePerAttack() {
        return currentDamagePerAttack;
    }



    private void updateYPos(Map map, Boss boss, ArrayList<Kappa> kappas, float by_value) {
        if (checkIfPlayerCollidesOverHim(map, boss, kappas, hitbox.x, hitbox.y + by_value, hitbox.width)) {

            airMovement = 0;
            return;

        } else if (checkIfPlayerCollidesUnderHim(map, boss, kappas, hitbox.x, hitbox.y + by_value, hitbox.width, hitbox.height)) {
            landed();

            float playerYPos = (hitbox.y + by_value + hitbox.height);
            int groundSpriteNumber = (int) (playerYPos / (64));
            hitbox.y = groundSpriteNumber * (64);
            hitbox.y -= hitbox.height + 1;
            y = hitbox.y;
            y -= 32;

            return;
        }
        y += by_value;
        hitbox.y += by_value;
        rightAttackHitBox.y = hitbox.y - 16;
        leftAttackHitBox.y = hitbox.y - 16;
    }

    public void jump() {
        if (!inAir && !isResting ) {
            inAir = true;
            airMovement = -6f;
            canDoubleJump = true; // Player can double jump after first jump
        } else if (inAir && !hasDoubleJumped && canDoubleJump) {
            airMovement = -6f; // Double jump
            hasDoubleJumped = true; // Player has double jumped
        }
    }

    public void landed() {
        inAir = false;
        canDoubleJump = false;
        hasDoubleJumped = false;
    }

    public void attack() {
        if (!attack) {
            setAttack(true);
        }
    }

    private void handlePlayerAttacksEntity(Entity entity) {
        if(!hasAttacked && entity != null) {
            //only attack if attack hitbox is active
            if(!attackHitBoxIsActive) return;
            Rectangle2D.Float entityHitbox = entity.getHitbox();

            Rectangle2D.Float attackHitbox = leftAttackHitBox;
            if(isFacingRight) attackHitbox = rightAttackHitBox;

            if(attackHitbox.intersects(entityHitbox)) {
                entity.decreaseHealth(currentDamagePerAttack);
                setHasAttacked(true);
            }
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

    public boolean collidesOnPosition(Map map, Boss boss, ArrayList<Kappa> kappas, float x, float y) {

        for(Kappa kappa: kappas) {
            if(!kappa.isDead()) {
                Rectangle2D.Float kappaHitbox = kappa.getHitbox();
                if(kappaHitbox.contains(new Point((int) x, (int) y))) return true;
            }
        }


        if(boss != null) {
            if(!boss.getIsDead()) {
                Rectangle2D.Float bossHitbox = boss.getHitbox();
                if(bossHitbox.contains(new Point((int) x, (int) y))) return true;
            }
        }

        if (x < 0) return true;
        if (y < 0) return false;

        int[][] mapData = map.getMapData();
        int tile_x = (int) (x / 64);
        int tile_y = (int) (y / 64);

        if (tile_y >= mapData.length || tile_x >= mapData[0].length) {
            return false;
        }

        return mapData[tile_y][tile_x] < 11 || mapData[tile_y][tile_x] < 48 && mapData[tile_y][tile_x] > 11 || mapData[tile_y][tile_x] < 81 && mapData[tile_y][tile_x] > 75;
    }

    private boolean checkIfPlayerCanMoveToPosition(Map map, Boss boss, ArrayList<Kappa> kappas, float x, float y, float width, float height) {
        if (checkIfPlayerCollidesOverHim(map, boss, kappas, x, y, width)) return false;
        if (checkIfPlayerCollidesOnRight(map, boss, kappas, x, y, width, height)) return false;
        if (checkIfPlayerCollidesOnLeft(map, boss, kappas, x, y, height)) return false;
        return !checkIfPlayerCollidesUnderHim(map, boss, kappas, x, y, width, height);
    }

    private boolean checkIfPlayerCollidesUnderHim(Map map, Boss boss, ArrayList<Kappa> kappas,  float x, float y, float width, float height) {
        for(int i=0; i<=width; i++) {
            if(collidesOnPosition(map,boss,kappas,x+i, y+height)) return true;
        }
        return false;
    }

    private boolean checkIfPlayerCollidesOverHim(Map map, Boss boss, ArrayList<Kappa> kappas, float x, float y, float width) {
        for(int i=0; i<=width; i++) {
            if(collidesOnPosition(map,boss,kappas,x+i, y)) return true;
        }
        return false;
    }

    private boolean checkIfPlayerCollidesOnRight(Map map, Boss boss, ArrayList<Kappa> kappas, float x, float y, float width, float height) {
        for(int i=0; i<=height; i++) {
            if(collidesOnPosition(map,boss, kappas, x+width, y+i)) return true;
        }
        return false;
    }

    private boolean checkIfPlayerCollidesOnLeft(Map map, Boss boss, ArrayList<Kappa> kappas, float x, float y, float height) {
        for(int i=0; i<=height; i++) {
            if(collidesOnPosition(map,boss, kappas, x, y+i)) return true;
        }
        return false;
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

    public void resetDeath() {
        deathAnimationFinished = false;
        isDead = false;
    }


    public void setPlayerHealth(int playerHealth) {
        this.health = playerHealth;
    }

    @Override
    public void decreaseHealth(int amount) {
        super.decreaseHealth(amount);
        setHitByEnemy(true);
    }

    public boolean getDeathAnimationFinished() {
        return deathAnimationFinished;
    }

    public void setDeathAnimationFinished(boolean deathAnimationFinished) {
        this.deathAnimationFinished = deathAnimationFinished;
    }

    public int resetHealth() {
        return health = totalMaxHearts * 2;
    }

    public int getTotalHearts() {
        return totalMaxHearts;
    }

    public void setTotalHearts(int totalHearts) {
        this.totalMaxHearts = totalHearts;
    }

    public boolean isJumping() {
        return inAir;
    }

    public boolean isHitByEnemy() {
        return isHitByEnemy;
    }

    public void setHitByEnemy(boolean hitByEnemy) {
        isHitByEnemy = hitByEnemy;
    }

    public boolean getIsResting() {
        return isResting;
    }

    public void setIsRestingIfNotInAir(boolean isResting) {
        if(inAir) return;
        this.isResting = isResting;
    }

    public boolean getIsDashing() {
        return isDashing;
    }

    public void setIsDashing(boolean isDashing) {
        this.isDashing = isDashing;
    }

    public void increaseMaximumDamagePerAttack(int byValue) {
        this.currentDamagePerAttack += byValue;
    }

    public void resetMaximumDamagePerAttack() {
        this.currentDamagePerAttack = STANDARD_DAMAGE;
    }

    public boolean getIsFacingRight() {
        return isFacingRight;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
    public boolean getHasAttacked() {
        return hasAttacked;
    }
}