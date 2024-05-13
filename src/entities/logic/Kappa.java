package entities.logic;

import maps.logic.Map;

import java.awt.geom.Rectangle2D;
import java.util.Timer;
import java.util.TimerTask;

public class Kappa extends Entity{

    private float leftBoundary;
    private float rightBoundary;
    private float speed;
    boolean right;
    private final static float HORIZONTAL_OFFSET = 40; //needed because
    private int health = 100;
    private int maxHealth = 100;
    private boolean isScoreIncreased = false;

    private static int nextId = 0;
    private int id;
    private boolean isPlayerNear = false;
    private boolean isAttacking = false;
    private Rectangle2D.Float attackHitbox;
    private boolean hasAttacked = false;
    private boolean isDead = false;
    private Timer attackTimer;




    public Kappa(float x, float y, float leftBoundary, float rightBoundary, float speed) {
        super(x, (int) ((y - 15) * 1.5), new Rectangle2D.Float(x + 10, (int) ((y - 13) * 1.5),(96 - 60) * 2,(96 - 55) * 2));
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.speed = speed;
        this.right = true;
        this.id = nextId++;
        attackTimer = new Timer();
        attackHitbox = new Rectangle2D.Float();
    }

    @Override
    void updatePushback() {
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

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void update(Map map, Player player) {
        if (isPlayerHitboxNextToKappaHitbox(player) || isDead) {
            return;
        }

        if(isPlayerNearChecker(player) && !player.isDead()) {
            right = player.getX() > x;

            if(right && x < rightBoundary) {
                updateXPos(map, speed);
            }
            else if(!right && x > leftBoundary) {
                updateXPos(map, -speed);
            }
        }
        else {
            if(right && x < rightBoundary) {
                updateXPos(map, speed);
            }
            else if(!right && x > leftBoundary) {
                updateXPos(map, -speed);
            }
            else {
                right = !right;
            }
        }

        if (isAttacking) {
            updateAttackHitbox();
        }
    }

    private void updateXPos(Map map, float by_value) {
        if(!checkIfPlayerCanMoveToPosition(map, hitbox.x + by_value, hitbox.y, hitbox.width, hitbox.height)) return;
        x += by_value;
        hitbox.x += by_value;
    }

    public void updateAttackHitbox() {
        float attackHitboxWidth = 50;
        float attackHitboxHeight = 64;
        float attackHitboxX = right ? x + hitbox.width : x - attackHitboxWidth;
        float attackHitboxY = y;
        attackHitbox = new Rectangle2D.Float(attackHitboxX, attackHitboxY, attackHitboxWidth, attackHitboxHeight);
    }

    public void updateSpawnPoint (int x, int y) {
        this.x = x;
        this.y = y - HORIZONTAL_OFFSET;
        this.hitbox.x = x;
        this.hitbox.y = y - HORIZONTAL_OFFSET;
    }

    public boolean checkForCollisonOnPosition(Map map, float x, float y) {
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
        if (!checkForCollisonOnPosition(map, x,y + height))
            if (!checkForCollisonOnPosition(map, x+width,y+height))
                return false;
        return true;
    }

    private boolean checkIfPlayerCollidesOverHim(Map map, float x, float y, float width) {
        if (!checkForCollisonOnPosition(map, x,y))
            if (!checkForCollisonOnPosition(map, x+width,y))
                return false;
        return true;
    }

    public boolean isRight() {
        return right;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void resetHealth() {
        this.health = this.maxHealth;
    }

    public void setScoreIncreased(boolean isScoreIncreased) {
        this.isScoreIncreased = isScoreIncreased;
    }

    public boolean isScoreIncreased() {
        return isScoreIncreased;
    }

    public int getId() {
        return id;
    }

    public boolean isPlayerNear() {
        return isPlayerNear;
    }

    public boolean isAttacking() {
        return isAttacking;
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

    public Rectangle2D.Float getAttackHitbox() {
        return attackHitbox;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public boolean isPlayerHitboxNextToKappaHitbox(Player player) {

        Rectangle2D.Float playerHitbox = player.getHitbox();
        Rectangle2D.Float kappaHitbox = this.getHitbox();

        Rectangle2D.Float playerHitboxBuffered = new Rectangle2D.Float(playerHitbox.x - 1, playerHitbox.y - 1, playerHitbox.width + 2, playerHitbox.height + 2);
        Rectangle2D.Float kappaHitboxBuffered = new Rectangle2D.Float(kappaHitbox.x - 1, kappaHitbox.y - 1, kappaHitbox.width + 2, kappaHitbox.height + 2);

        return playerHitboxBuffered.intersects(kappaHitboxBuffered);
    }

    @Override
    public boolean isDead() {
        return isDead;
    }
}
