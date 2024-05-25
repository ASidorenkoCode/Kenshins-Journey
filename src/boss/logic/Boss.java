package boss.logic;

import entities.logic.Player;
import gameObjects.logic.Finish;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Boss {


    //TODO: Duplicate vars from boss and boss ui should be merged
    private static final int BIG_PROJECTILE_WIDTH = 33;
    private static final int BIG_PROJECTILE_HEIGHT = 17;
    private static final int BIG_PROJECTILE_SCALE = 4;
    private static final int NUMBER_OF_MINI_PROJECTILES = 10;
    private static final int MINI_PROJECTILE_WIDTH = 25;
    private static final int MINI_PROJECTILE_HEIGHT = 20;
    private static final int BOSS_WIDTH = 64;
    private static final int BOSS_HEIGHT = 64;
    private static final int BOSS_SCALE = 3;

    private float x;
    private float y;
    private Rectangle2D.Float hitbox;
    private Rectangle2D.Float projectileHitbox;
    private ArrayList<Rectangle2D.Float> miniProjectileHitboxes;
    private boolean isUsingBigProjectile;
    private int health;
    private boolean isDead;

    public Boss(float x, float y) {
        setBossPosition(x,y);
        this.hitbox = new Rectangle2D.Float(this.x,this.y,BOSS_WIDTH * BOSS_SCALE,BOSS_HEIGHT * BOSS_SCALE);
        this.projectileHitbox = new Rectangle2D.Float(
                x-(BIG_PROJECTILE_WIDTH * BIG_PROJECTILE_SCALE),
                this.y,
                BIG_PROJECTILE_WIDTH * BIG_PROJECTILE_SCALE,
                BIG_PROJECTILE_HEIGHT * BIG_PROJECTILE_SCALE);
        initNewMiniProjectiles();
        this.health = 500;
        this.isUsingBigProjectile = true;
    }

    private void initNewMiniProjectiles() {
        miniProjectileHitboxes = new ArrayList<>();
        for(int i=0; i<NUMBER_OF_MINI_PROJECTILES; i++) {
            miniProjectileHitboxes.add(new Rectangle2D.Float(x,y,MINI_PROJECTILE_WIDTH,MINI_PROJECTILE_HEIGHT));
        }
    }

    public void update(Player player, Finish finish, int offset) {
        if(!isDead) {
            if(playerHitsBoss(player)  && player.getAttackNumber() <= 2) {
                decreaseHealth(player.getCurrentDamagePerAttack());
                player.increaseAttackNumber();
            }

            if(!isDead) attack(player, offset);
            else finish.setIsActive(true);
        }
    }

    private void setBossPosition(float x, float y) {
        this.y = y - BOSS_HEIGHT * BOSS_SCALE + BOSS_HEIGHT;
        this.x = x;
    }

    private boolean playerHitsBoss(Player player) {
        //only attack if attack hitbox is active
        if(!player.getAttackHitBoxIsActive()) return false;

        if(player.getIsFacingRight())
            return hitbox.intersects(player.getRightAttackHitBox());
        return hitbox.intersects(player.getLeftAttackHitBox());
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }


    private void attack(Player player, int offset) {
        if(isUsingBigProjectile) {
            bigProjectileAttack(player, offset);
        } else {
            miniProjectileAttack(player, offset);
        }
    }

    //attack one
    private void bigProjectileAttack(Player player, int offset) {
        moveProjectile();
        checkIfProjectileIsOutOfBounds(offset);
        if(projectileHitsPlayer(player)) {
            player.decreaseHealth(1);
            resetProjectile();
        }
    }
    private void resetProjectile() {
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
    private boolean projectileHitsPlayer(Player player) {
        return player.getHitbox().intersects(projectileHitbox);
    }

    //attack two
    private void miniProjectileAttack(Player player, int offset) {
        moveMiniProjectiles();
        checkIfAllMiniProjectilesAreOutOfBounds(offset);
        if(miniProjectileHitsPlayer(player)) player.decreaseHealth(1);
    }

    private void resetAllMiniProjectiles() {
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
    private boolean miniProjectileHitsPlayer(Player player) {
        for(Rectangle2D.Float hitbox: miniProjectileHitboxes) {
            if(player.getHitbox().intersects(hitbox)) {
                resetAllMiniProjectiles();
                return true;
            };
        }
        return false;
    }

    //Getter
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

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
}
