package boss.logic;

import entities.logic.Entity;
import entities.logic.Kappa;
import entities.logic.Player;
import entities.ui.PlayerUI;

import java.awt.geom.Rectangle2D;

public class Boss {

    private static final int PROJECTILE_LENGTH = 70;
    private float x;
    private float y;
    private Rectangle2D.Float hitbox;
    private Rectangle2D.Float projectileHitbox;
    private int health;
    private boolean isDead;

    public Boss(float x, float y) {
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle2D.Float(x,y,60,60);
        this.projectileHitbox = new Rectangle2D.Float(x-PROJECTILE_LENGTH,y,PROJECTILE_LENGTH,30);
        this.health = 400;
    }

    public void update(Player player) {
        if(health > 0) {
            if(playerHitsBoss(player)) decreaseHealth(player.getCurrentDamagePerAttack());
            moveProjectile();
            checkIfProjectileIsOutOfBounds();
        }
    }

    private boolean playerHitsBoss(Player player) {
        if (isDead || !player.getAttackHitBoxIsActive()) {
            return false;
        }
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


    private void checkIfProjectileIsOutOfBounds() {
        if(projectileHitbox.x <= 0) {
            projectileHitbox.x = x-PROJECTILE_LENGTH;
        }
    }
    private void moveProjectile() {
        projectileHitbox.x -= 1;
    }


    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public Rectangle2D.Float getProjectileHitbox() {
        return projectileHitbox;
    }
}
