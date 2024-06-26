package entities.logic;

import game.logic.Highscore;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class Enemy extends Entity {
    private final float speed;
    boolean moveRight;
    private final static int ATTACK_SPEED = 600;
    private int attackCount = 0;
    private final int maxHealth = 100;
    private boolean isScoreIncreased = false;
    private boolean isEntityNear = false;
    private boolean isAttacking = false;
    private Rectangle2D.Float attackHitbox;
    private boolean hasAttacked = false;
    private boolean inAir = true;

    public Enemy(float x, float y, float speed) {
        super(x, y - 20, new Rectangle2D.Float(x + 10, y - 20, 64, 86), false, 20);
        this.speed = speed;
        this.moveRight = true;
        this.attackHitbox = new Rectangle2D.Float();
    }

    public boolean isEntityInsideChecker(Entity entity) {
        // checks if entity is near to enemy so enemy can attack player
        float distanceX = Math.abs(x - entity.getX());
        float distanceY = Math.abs(y - entity.getY());
        boolean isEntityUnderneath = entity.getY() > y;
        isEntityNear = !isEntityUnderneath && Math.sqrt(distanceX * distanceX + distanceY * distanceY) < 150;
        return isEntityNear;
    }


    public void update(Map map, Player player, Highscore highscore) {

        if (isDead) {
            if (!isScoreIncreased) {
                highscore.increaseHighscoreForEnemies();
                setScoreIncreased(true);
            }
            return;
        }


        boolean move = true;
        if (isEntityHitboxNextToEnemy(player)) {
            updateAttackHitbox();
            move = false;
        }

        //reset attack Count, when enemy has attacked and break is over
        if (hasAttacked) {
            attackCount++;
            if (attackCount >= ATTACK_SPEED) {
                hasAttacked = false;
                attackCount = 0;
            }
        }

        handleAttack(player);
        //dont move if is attacking or its specified
        if (isAttacking || !move) return;

        if (inAir)
            if (!checkIfEnemyCollidesUnderHim(map, hitbox.x, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.y++;
                y++;
            } else inAir = false;

        if (moveRight) {
            updateXPos(map, speed);
            checkIfEnemyCollidesOnRight(map, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        } else {
            updateXPos(map, -speed);
            checkIfEnemyCollidesOnLeft(map, hitbox.x, hitbox.y, hitbox.height);
        }


    }


    private void updateXPos(Map map, float by_value) {

        if (checkIfEnemyIsOverEdge(map, hitbox.x + by_value, hitbox.y + 1, hitbox.width, hitbox.height, moveRight)) {

            moveRight = !moveRight;

        }
        x += by_value;
        hitbox.x += by_value;
    }

    private void updateAttackHitbox() {
        float attackHitboxWidth = 50;
        float attackHitboxHeight = 64;
        float attackHitboxX = moveRight ? x + hitbox.width : x - attackHitboxWidth;
        float attackHitboxY = y;
        attackHitbox = new Rectangle2D.Float(attackHitboxX, attackHitboxY, attackHitboxWidth, attackHitboxHeight);
    }

    private boolean isEntityHitboxNextToEnemy(Entity entity) {

        Rectangle2D.Float e1Hitbox = entity.getHitbox();
        Rectangle2D.Float enemyHitbox = hitbox;

        Rectangle2D.Float e1HitboxBuffered = new Rectangle2D.Float(e1Hitbox.x - 1, e1Hitbox.y - 1, e1Hitbox.width + 2, e1Hitbox.height + 2);
        Rectangle2D.Float enemyHitboxBuffered = new Rectangle2D.Float(enemyHitbox.x - 1, enemyHitbox.y - 1, enemyHitbox.width + 2, enemyHitbox.height + 2);

        return enemyHitboxBuffered.intersects(e1HitboxBuffered);
    }

    public boolean checkForCollisonOnPosition(Map map, float x, float y) {
        if (x < 0 || y < 0) return true;

        int[][] mapData = map.getMapData();
        int tile_x = (int) (x / 64);
        int tile_y = (int) (y / 64);

        if (tile_y >= mapData.length || tile_x >= mapData[0].length) {
            return true;
        }

        return (mapData[tile_y][tile_x] >= 11 && mapData[tile_y][tile_x] < 48) ||
                (mapData[tile_y][tile_x] > 74 && mapData[tile_y][tile_x] < 81) ||
                (mapData[tile_y][tile_x] >= 27 && mapData[tile_y][tile_x] < 75);
    }

    private boolean checkIfEnemyCollidesUnderHim(Map map, float x, float y, float width, float height) {
        if (checkForCollisonOnPosition(map, x, y + height))
            return !checkForCollisonOnPosition(map, x + width, y + height);
        return true;
    }

    private boolean checkIfEnemyCollidesOnLeft(Map map, float x, float y, float height) {
        for (int i = 0; i <= height; i++) {
            if (!checkForCollisonOnPosition(map, x, y + i)) return false;
        }
        return true;
    }

    private boolean checkIfEnemyCollidesOnRight(Map map, float x, float y, float width, float height) {
        for (int i = 0; i <= height; i++) {
            if (!checkForCollisonOnPosition(map, x + width, y + i)) return false;
        }
        return true;
    }


    private boolean checkIfEnemyIsOverEdge(Map map, float x, float y, float width, float height, boolean isRight) {
        if (isRight) return checkForCollisonOnPosition(map, x + width, y + height);
        return checkForCollisonOnPosition(map, x, y + height);
    }


    private void handleAttack(Player player) {
        if (isEntityInsideChecker(player)) {
            setMoveRight(x < player.getX());
            if (!hasAttacked) {
                if (attackHitbox.intersects(player.getHitbox())) {
                    setIsAttacking(true);
                    player.decreaseHealth(1);
                    setHasAttacked(true);
                }
            }
        }
    }


    //getter and setter

    public void resetHealth() {
        this.health = this.maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setScoreIncreased(boolean isScoreIncreased) {
        this.isScoreIncreased = isScoreIncreased;
    }

    public boolean isScoreIncreased() {
        return isScoreIncreased;
    }

    public boolean isEntityNear() {
        return isEntityNear;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public Rectangle2D.Float getAttackHitbox() {
        return attackHitbox;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
}
