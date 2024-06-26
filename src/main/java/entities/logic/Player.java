package entities.logic;

import game.logic.Highscore;
import maps.logic.Map;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Player extends Entity implements Serializable {

    private static final long serialVersionUID = -8236329990641323382L;
    private boolean left, right, attack, inAir, attackHitBoxIsActive, isResting, isDashing, isFacingRight;
    private float airMovement = -6f;
    private final Rectangle2D.Float rightAttackHitBox;
    private final Rectangle2D.Float leftAttackHitBox;
    private boolean hasDynamicAdjustedPlayerDirectionHitbox = false;
    private boolean hasAttacked = false;
    private int currentDamagePerAttack = 20;
    private final int damageDealtInCurrentAttack = 0;
    private final int totalHealth = 6;
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
        super(x, y, new Rectangle2D.Float(x + 50, y + 32, (96 - 69) * 2, (96 - 48) * 2), false, 6);
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

    public void update(Map map, Boss boss, ArrayList<Enemy> enemies, Highscore highscore) {


        if(!attack) {
            attackHitBoxIsActive = false;
        } else {
            handlePlayerAttacksEntity(boss);
            for (Enemy enemy : enemies) {
                handlePlayerAttacksEntity(enemy);
            }
        }


        if(isResting) {
            if (health < 6) {
                health++;
                highscore.decreaseHighscoreForHeart();
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
                updateXPos(map, boss, enemies, currentSpeed);
                updateGroundMovement();
            } else if (left && !right) {
                updateXPos(map, boss, enemies, -currentSpeed);
                updateGroundMovement();
                isFacingRight = false;
            } else time = 0;

            if (inAir && !isDashing) {

                updateYPos(map, boss, enemies, airMovement);

                if (inAir) {
                    airMovement += 0.1f;
                }

            } else if (!checkIfPlayerCollidesUnderHim(map, boss, enemies, hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height)) {
                airMovement = 0;
                inAir = true;
            }
            // TODO: implement new y position, for now It's just a workaround with times 3
            if (this.getHitbox().x < 0 || this.getHitbox().y > map.getMapData().length * map.getTileSize()) {
                this.decreaseHealth(getHealth());
            }
        }
    }


    private void updateXPos(Map map, Boss boss, ArrayList<Enemy> enemies, float byValue) {
        if (!checkIfPlayerCanMoveToPosition(map, boss, enemies, hitbox.x + byValue, hitbox.y, hitbox.width, hitbox.height))
            return;
        x += byValue;
        hitbox.x += byValue;
        adjustPlayerHitboxPosition(map, boss, enemies);
        rightAttackHitBox.x = hitbox.x + hitbox.width;
        leftAttackHitBox.x = hitbox.x - leftAttackHitBox.width;
    }

    private void adjustPlayerHitboxPosition(Map map, Boss boss, ArrayList<Enemy> enemies) {
        if (getLeft() && !getRight() && !hasDynamicAdjustedPlayerDirectionHitbox) {
            if (checkIfPlayerCanMoveToPosition(map, boss, enemies, hitbox.x + 20, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x += 20;
                hasDynamicAdjustedPlayerDirectionHitbox = true;
            }
        } else if (!getLeft() && getRight() && hasDynamicAdjustedPlayerDirectionHitbox) {
            if (checkIfPlayerCanMoveToPosition(map, boss, enemies, hitbox.x - 20, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.x -= 20;
                hasDynamicAdjustedPlayerDirectionHitbox = false;
            }
        }
    }


    public int getCurrentDamagePerAttack() {
        return currentDamagePerAttack;
    }


    private void updateYPos(Map map, Boss boss, ArrayList<Enemy> enemies, float by_value) {
        if (checkIfPlayerCollidesOverHim(map, boss, enemies, hitbox.x, hitbox.y + by_value, hitbox.width)) {

            airMovement = 0;
            return;

        } else if (checkIfPlayerCollidesUnderHim(map, boss, enemies, hitbox.x, hitbox.y + by_value, hitbox.width, hitbox.height)) {
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

    public boolean collidesOnPosition(Map map, Boss boss, ArrayList<Enemy> enemies, float x, float y) {

        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                Rectangle2D.Float enemyHitbox = enemy.getHitbox();
                if (enemyHitbox.contains(new Point((int) x, (int) y))) return true;
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

        return mapData[tile_y][tile_x] < 11 || mapData[tile_y][tile_x] < 48 && mapData[tile_y][tile_x] > 11 || mapData[tile_y][tile_x] < 81 && mapData[tile_y][tile_x] > 74 || mapData[tile_y][tile_x] < 27 && mapData[tile_y][tile_x] > 23;
    }

    private boolean checkIfPlayerCanMoveToPosition(Map map, Boss boss, ArrayList<Enemy> enemies, float x, float y, float width, float height) {
        if (checkIfPlayerCollidesOverHim(map, boss, enemies, x, y, width)) return false;
        if (checkIfPlayerCollidesOnRight(map, boss, enemies, x, y, width, height)) return false;
        if (checkIfPlayerCollidesOnLeft(map, boss, enemies, x, y, height)) return false;
        return !checkIfPlayerCollidesUnderHim(map, boss, enemies, x, y, width, height);
    }

    private boolean checkIfPlayerCollidesUnderHim(Map map, Boss boss, ArrayList<Enemy> enemies, float x, float y, float width, float height) {
        for(int i=0; i<=width; i++) {
            if (collidesOnPosition(map, boss, enemies, x + i, y + height)) return true;
        }
        return false;
    }

    private boolean checkIfPlayerCollidesOverHim(Map map, Boss boss, ArrayList<Enemy> enemies, float x, float y, float width) {
        for(int i=0; i<=width; i++) {
            if (collidesOnPosition(map, boss, enemies, x + i, y)) return true;
        }
        return false;
    }

    private boolean checkIfPlayerCollidesOnRight(Map map, Boss boss, ArrayList<Enemy> enemies, float x, float y, float width, float height) {
        for(int i=0; i<=height; i++) {
            if (collidesOnPosition(map, boss, enemies, x + width, y + i)) return true;
        }
        return false;
    }

    private boolean checkIfPlayerCollidesOnLeft(Map map, Boss boss, ArrayList<Enemy> enemies, float x, float y, float height) {
        for(int i=0; i<=height; i++) {
            if (collidesOnPosition(map, boss, enemies, x, y + i)) return true;
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
        return health = totalHealth;
    }

    public int getTotalHealth() {
        return totalHealth;
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

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }


    public static class PlayerSerializer {

        private static final String FILE_PLAYER_PATH = "res/player.txt";

        public static void writePlayer(Player player) {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Path.of(FILE_PLAYER_PATH)))) {
                oos.writeInt(player.getHealth());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static int readPlayerHealth() throws IOException {
            Path filePath = Path.of(FILE_PLAYER_PATH);
            if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                return -1; // return -1 or any other value to indicate that the file doesn't exist or is empty
            }

            int health = -1;

            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                health = ois.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return health;
        }
    }
}
