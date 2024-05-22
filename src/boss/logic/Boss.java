package boss.logic;

import entities.logic.Entity;
import entities.logic.Kappa;
import entities.logic.Player;
import entities.ui.PlayerUI;
import gameObjects.logic.Finish;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Boss {

    private static final int PROJECTILE_LENGTH = 70;
    private static final int NUMBER_OF_MINI_PROJECTILES = 10;
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
        this.projectileHitbox = new Rectangle2D.Float(x-PROJECTILE_LENGTH,y,PROJECTILE_LENGTH,30);
        this.health = 20;
        initNewMiniProjectiles();
        this.isUsingOneProjectile = true;
    }

    private void initNewMiniProjectiles() {
        miniProjectileHitboxes = new ArrayList<>();
        for(int i=0; i<NUMBER_OF_MINI_PROJECTILES; i++) {
            miniProjectileHitboxes.add(new Rectangle2D.Float(x,y,5,5));
        }
    }

    public void update(Player player, Finish finish) {
        if(!isDead) {
            if(playerHitsBoss(player)) decreaseHealth(player.getCurrentDamagePerAttack());
            if(!isDead) attack(player);
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


    private void attack(Player player) {
        //TODO: Player needs to have a cool down for the next attack + no movement is allowed when loading screen is active
        if(isUsingOneProjectile) {
            attackOne(player);
        } else {
            attackTwo(player);
        }
    }

    //attack one
    private void attackOne(Player player) {
        moveProjectile();
        checkIfProjectileIsOutOfBounds();
        if(projectileHitsPlayer(player)) player.decreaseHealth(1);
    }
    private void checkIfProjectileIsOutOfBounds() {
        if(projectileHitbox.x <= 0) {
            projectileHitbox.x = x-PROJECTILE_LENGTH;
            isUsingOneProjectile = false;
        }
    }
    private void moveProjectile() {
        projectileHitbox.x -= 1;
    }
    private boolean projectileHitsPlayer(Player player) {
        return player.getHitbox().intersects(projectileHitbox);
    }

    //attack two
    private void attackTwo(Player player) {
        moveMiniProjectiles();
        checkIfAllMiniProjectilesAreOutOfBounds();
        if(miniProjectileHitsPlayer(player)) player.decreaseHealth(1);
    }
    private void checkIfAllMiniProjectilesAreOutOfBounds() {
        for(Rectangle2D.Float hitbox: miniProjectileHitboxes) {
            if(hitbox.x > 0) return;
        }
        initNewMiniProjectiles();
        isUsingOneProjectile = true;
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
            if(player.getHitbox().intersects(hitbox)) return true;
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
