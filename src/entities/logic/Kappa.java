package entities.logic;

import maps.logic.Map;

import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

public class Kappa extends Entity {
    private float speed;
    boolean moveRight;
    private final static float HORIZONTAL_OFFSET = 40;
    private int health = 100;
    private int maxHealth = 100;
    private boolean isScoreIncreased = false;
    private boolean isPlayerNear = false;
    private boolean isAttacking = false;
    private Rectangle2D.Float attackHitbox;
    private boolean hasAttacked = false;
    private boolean isDead = false;
    private Timer attackTimer;
    private boolean inAir = true;

    public Kappa(float x, float y, float speed) {
        super(x, y - 20, new Rectangle2D.Float(x + 10, y - 20, 64, 86));
        this.speed = speed;
        this.moveRight = true;
        attackTimer = new Timer();
        attackHitbox = new Rectangle2D.Float();
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }

    public boolean isPlayerNearChecker(Player player) {
        float distanceX = Math.abs(x - player.getX());
        float distanceY = Math.abs(y - player.getY());
        boolean isPlayerUnderneath = player.getY() > y;
        isPlayerNear = !isPlayerUnderneath && Math.sqrt(distanceX * distanceX + distanceY * distanceY) < 150;
        return isPlayerNear;
    }


    public void update(Map map, Player player) {

        if (isPlayerHitboxNextToKappaHitbox(player) || isDead) {
            return;
        }

        if (inAir)
            if (!checkIfEnemyCollidesUnderHim(map, hitbox.x, hitbox.y, hitbox.width, hitbox.height)) {
                hitbox.y++;
                y++;
            } else inAir = false;


        if (isPlayerNearChecker(player)) {
            if (player.getX() < x) moveRight = false;
            else moveRight = true;
        }

        if (moveRight) {
            updateXPos(map, speed);
        } else updateXPos(map, -speed);

        if (isAttacking) {
            updateAttackHitbox();
        }
    }

    private void updateXPos(Map map, float by_value) {

        if (checkIfEnemyIsOverEdge(map, hitbox.x + by_value, hitbox.y + 1, hitbox.width, hitbox.height, moveRight)) {

            moveRight = !moveRight;

        }
        x += by_value;
        hitbox.x += by_value;
    }

    public void updateAttackHitbox() {
        float attackHitboxWidth = 50;
        float attackHitboxHeight = 64;
        float attackHitboxX = moveRight ? x + hitbox.width : x - attackHitboxWidth;
        float attackHitboxY = y;
        attackHitbox = new Rectangle2D.Float(attackHitboxX, attackHitboxY, attackHitboxWidth, attackHitboxHeight);
    }

    public void updateSpawnPoint(int x, int y) {
        this.x = x;
        this.y = y - HORIZONTAL_OFFSET;
        this.hitbox.x = x;
        this.hitbox.y = y - HORIZONTAL_OFFSET;
    }

    public boolean checkForCollisonOnPosition(Map map, float x, float y) {
        if (x < 0) return false;
        if (y < 0) return false;

        int[][] mapData = map.getMapData();
        int tile_x = (int) (x / 64);
        int tile_y = (int) (y / 64);

        if (tile_x >= 0 && tile_x < mapData[0].length && tile_y >= 0 && tile_y < mapData.length) {
            return mapData[tile_y][tile_x] >= 11 && (mapData[tile_y][tile_x] <= 11 || mapData[tile_y][tile_x] > 48) && (mapData[tile_y][tile_x] > 81 || mapData[tile_y][tile_x] <= 75);
        } else {
            return false;
        }
    }

    private boolean checkIfEnemyCollidesUnderHim(Map map, float x, float y, float width, float height) {
        if (checkForCollisonOnPosition(map, x, y + height))
            return !checkForCollisonOnPosition(map, x + width, y + height);
        return true;
    }

    private boolean checkIfEnemyIsOverEdge(Map map, float x, float y, float width, float height, boolean isRight) {
        if (isRight) return checkForCollisonOnPosition(map, x + width, y + height);
        return checkForCollisonOnPosition(map, x, y + height);
    }

    public void resetHealth() {
        this.health = this.maxHealth;
    }

    public void startAttacking(Player player) {

        if (player.isJumping()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isPlayerNear()) {
                        isAttacking = true;
                        attackPlayer(player);

                        attackTimer.cancel();
                        attackTimer = new Timer();

                        attackTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                stopAttacking();
                            }
                        }, 3000);
                    }
                }
            }, 1000);
        } else {
            isAttacking = true;
            attackPlayer(player);

            attackTimer.cancel();
            attackTimer = new Timer();

            attackTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stopAttacking();
                }
            }, 3000);
        }
    }

    public void stopAttacking() {
        hasAttacked = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isAttacking = false;
            }
        }, 1000);
    }

    public void attackPlayer(Player player) {
        if (!hasAttacked) {
            player.decreaseHealth(1);
            hasAttacked = true;
        }
    }


    public boolean isPlayerHitboxNextToKappaHitbox(Player player) {

        Rectangle2D.Float playerHitbox = player.getHitbox();
        Rectangle2D.Float kappaHitbox = this.getHitbox();

        Rectangle2D.Float playerHitboxBuffered = new Rectangle2D.Float(playerHitbox.x - 1, playerHitbox.y - 1, playerHitbox.width + 2, playerHitbox.height + 2);
        Rectangle2D.Float kappaHitboxBuffered = new Rectangle2D.Float(kappaHitbox.x - 1, kappaHitbox.y - 1, kappaHitbox.width + 2, kappaHitbox.height + 2);

        return playerHitboxBuffered.intersects(kappaHitboxBuffered);
    }

    public int getHealth() {
        return health;
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

    public boolean isPlayerNear() {
        return isPlayerNear;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public Rectangle2D.Float getAttackHitbox() {
        return attackHitbox;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    void updatePushback() {
    }
}
