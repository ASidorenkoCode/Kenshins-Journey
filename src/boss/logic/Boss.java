package boss.logic;

import entities.logic.Player;
import gameObjects.logic.Finish;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Boss {

    private static final int BIG_PROJECTILE_WIDTH = 33;
    private static final int BIG_PROJECTILE_HEIGHT = 17;
    private static final int BIG_PROJECTILE_SCALE = 4;
    private static final int NUMBER_OF_MINI_PROJECTILES = 10;
    private static final int MINI_PROJECTILE_WIDTH = 35;
    private static final int MINI_PROJECTILE_HEIGHT = 35;

    private float x;
    private float y;
    private Rectangle2D.Float hitbox;
    private Rectangle2D.Float projectileHitbox;
    private ArrayList<Rectangle2D.Float> miniProjectileHitboxes;
    private boolean isUsingOneProjectile;
    private int health;
    private boolean isDead;

    public Boss(float x, float y) {
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle2D.Float(x,y,60,60);
        this.projectileHitbox = new Rectangle2D.Float(
                x-(BIG_PROJECTILE_WIDTH * BIG_PROJECTILE_SCALE),
                y,
                BIG_PROJECTILE_WIDTH * BIG_PROJECTILE_SCALE,
                BIG_PROJECTILE_HEIGHT * BIG_PROJECTILE_SCALE);
        this.health = 20;
        initNewMiniProjectiles();
        this.isUsingOneProjectile = true;
    }

    private void initNewMiniProjectiles() {
        miniProjectileHitboxes = new ArrayList<>();
        for(int i=0; i<NUMBER_OF_MINI_PROJECTILES; i++) {
            miniProjectileHitboxes.add(new Rectangle2D.Float(x,y,MINI_PROJECTILE_WIDTH,MINI_PROJECTILE_HEIGHT));
        }
    }

    public void update(Player player, Finish finish, int offset) {
        if(!isDead) {
            if(playerHitsBoss(player)) decreaseHealth(player.getCurrentDamagePerAttack());
            if(!isDead) attack(player, offset);
            else finish.setIsActive(true);
        }
    }

    private boolean playerHitsBoss(Player player) {
        if (!player.getAttackHitBoxIsActive()) return false;
        if(player.getLeftAttackHitBox().intersects(hitbox)) return true;
        return player.getRightAttackHitBox().intersects(hitbox);
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }


    private void attack(Player player, int offset) {
        if(isUsingOneProjectile) {
            attackOne(player, offset);
        } else {
            attackTwo(player, offset);
        }
    }

    //attack one
    private void attackOne(Player player, int offset) {
        moveProjectile();
        checkIfProjectileIsOutOfBounds(offset);
        if(projectileHitsPlayer(player)) {
            player.decreaseHealth(1);
            resetProjectile();
        }
    }
    private void resetProjectile() {
        projectileHitbox.x = x- BIG_PROJECTILE_WIDTH;
        isUsingOneProjectile = false;
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
    private void attackTwo(Player player, int offset) {
        moveMiniProjectiles();
        checkIfAllMiniProjectilesAreOutOfBounds(offset);
        if(miniProjectileHitsPlayer(player)) player.decreaseHealth(1);
    }

    private void resetAllMiniProjectiles() {
        initNewMiniProjectiles();
        isUsingOneProjectile = true;
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

    //Getter and setter
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public Rectangle2D.Float getProjectileHitbox() {
        return projectileHitbox;
    }
    public ArrayList<Rectangle2D.Float> getMiniProjectileHitboxes() {
        return miniProjectileHitboxes;
    }
}
