package entities.logic;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Boss extends Entity {
    private static final int BIG_PROJECTILE_WIDTH = 33;
    private static final int BIG_PROJECTILE_HEIGHT = 17;
    private static final int BIG_PROJECTILE_SCALE = 4;
    private static final int NUMBER_OF_MINI_PROJECTILES = 10;
    private static final int MINI_PROJECTILE_WIDTH = 25;
    private static final int MINI_PROJECTILE_HEIGHT = 20;
    private static final int BOSS_WIDTH = 64;
    private static final int BOSS_HEIGHT = 64;
    private static final int BOSS_SCALE = 4;
    private Rectangle2D.Float projectileHitbox;
    private ArrayList<Rectangle2D.Float> miniProjectileHitboxes;
    private boolean isUsingBigProjectile;
    private final static int JUMP_SPEED = 500;
    private int jumpCount;
    private float airMovement;
    private boolean inAir;
    private float previosY;

    private boolean isScoreIncreased = false;


    public Boss(float x, float y) {
        super(x,y, new Rectangle2D.Float(0,0,0,0), false, 20);
        setBossPosition(x,y);
        this.hitbox = new Rectangle2D.Float(this.x,this.y,BOSS_WIDTH * BOSS_SCALE,BOSS_HEIGHT * BOSS_SCALE);
        this.projectileHitbox = new Rectangle2D.Float(
                x-(BIG_PROJECTILE_WIDTH * BIG_PROJECTILE_SCALE),
                this.y,
                BIG_PROJECTILE_WIDTH * BIG_PROJECTILE_SCALE,
                BIG_PROJECTILE_HEIGHT * BIG_PROJECTILE_SCALE);
        initNewMiniProjectiles();
        this.health = 20;
        this.isUsingBigProjectile = true;
        this.airMovement = -5f;
        this.inAir = false;
    }


    private void initNewMiniProjectiles() {
        miniProjectileHitboxes = new ArrayList<>();
        for(int i=0; i<NUMBER_OF_MINI_PROJECTILES; i++) {
            miniProjectileHitboxes.add(new Rectangle2D.Float(x,y,MINI_PROJECTILE_WIDTH,MINI_PROJECTILE_HEIGHT));
        }
    }

    public void update(int offsetX, Player player) {
    public void update(int offset, Player player, Highscore highscore, Finish finish) {
        if(!isDead) {

            attack(offsetX, player);

            if(inAir) {
                //TODO: Static movement or based on hitbox?
                if(previosY < y) {
                    y = previosY;
                    hitbox.y = previosY;
                    inAir = false;
                    airMovement = -5f;
                } else {
                    updateYPosByAirMovement();
                    airMovement += 0.1f;
                }
            } else {
                jumpCount++;
                if(jumpCount >= JUMP_SPEED) {
                    jumpCount = 0;
                    inAir = true;
                    previosY = y;
                }
            }
        } else {
            if(!isScoreIncreased) {
                highscore.increaseHighscoreForBoss();
                setScoreIncreased(true);
                finish.setIsActive(true);
            }
        }

    }

    private void updateYPosByAirMovement() {
        y += airMovement;
        hitbox.y += airMovement;

        //only update not used projectiles
        if(isUsingBigProjectile) {
            for (Rectangle2D.Float miniProjectileHitbox : miniProjectileHitboxes) {
                miniProjectileHitbox.y += airMovement;
            }
        } else projectileHitbox.y += airMovement;

    }

    private void setBossPosition(float x, float y) {
        this.y = y - BOSS_HEIGHT * BOSS_SCALE + BOSS_HEIGHT;
        this.x = x;
    }


    private void attack(int offset, Player player) {
        if(isUsingBigProjectile) {
            bigProjectileAttack(offset, player);

        } else {
            miniProjectileAttack(offset, player);
        }
    }

    //attack one
    private void bigProjectileAttack(int offset, Player player) {
        moveProjectile();
        if(player.getHitbox().intersects(projectileHitbox)) {
            player.decreaseHealth(1);
            resetProjectile();
            return;
        }
        checkIfProjectileIsOutOfBounds(offset);

    }
    public void resetProjectile() {
        projectileHitbox.x = x- BIG_PROJECTILE_WIDTH;
        isUsingBigProjectile = false;
    }
    private void checkIfProjectileIsOutOfBounds(int offset) {
        if(projectileHitbox.x <= 0 + offset) {
            resetProjectile();
        }
    }
    private void moveProjectile() {
        projectileHitbox.x -= 1;
    }

    //attack two
    private void miniProjectileAttack(int offset, Player player) {
        moveMiniProjectiles();
        for(Rectangle2D.Float hitbox: miniProjectileHitboxes) {
            if(player.getHitbox().intersects(hitbox)) {
                resetAllMiniProjectiles();
                player.decreaseHealth(1);
                return;
            }
        }
        checkIfAllMiniProjectilesAreOutOfBounds(offset);
    }

    public void resetAllMiniProjectiles() {
        initNewMiniProjectiles();
        isUsingBigProjectile = true;
    }
    private void checkIfAllMiniProjectilesAreOutOfBounds(int offset) {
        for(Rectangle2D.Float hitbox: miniProjectileHitboxes) {
            if(hitbox.x > 0 + offset) return;
        }
        resetAllMiniProjectiles();
    }
    private void moveMiniProjectiles() {
        float startingYMovement = 0.9f;
        for(Rectangle2D.Float hitbox: miniProjectileHitboxes) {
            hitbox.x -= 1;
            hitbox.y += startingYMovement;
            startingYMovement -= 0.3f;
        }
    }

    //Getter

    public Rectangle2D.Float getProjectileHitbox() {
        return projectileHitbox;
    }
    public ArrayList<Rectangle2D.Float> getMiniProjectileHitboxes() {
        return miniProjectileHitboxes;
    }

    public boolean getIsUsingBigProjectile() {
        return isUsingBigProjectile;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setScoreIncreased(boolean isScoreIncreased) {
        this.isScoreIncreased = isScoreIncreased;
    }

    public boolean isScoreIncreased() {
        return isScoreIncreased;
    }
}
