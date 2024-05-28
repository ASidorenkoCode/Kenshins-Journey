package entities.ui;

import entities.animations.EnemyAnimations;
import entities.logic.Kappa;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class KappaUI extends EntityUI {

    Kappa kappa;
    boolean showHitBox;
    private EnemyAnimations currentAnimation;

    public KappaUI(Kappa kappa, boolean showHitBox) {
        this.kappa = kappa;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 64;
        SPRITE_PX_HEIGHT = 64;
        ENTITY_SPRITE_PATH = "enemies/kappa/kappaRight.png";
        ENTITY_SPRITE_PATH_LEFT = "enemies/kappa/kappaLeft.png";
        SPRITE_Y_DIMENSION = 4;
        SPRITE_X_DIMENSION = 8;
        loadAnimations();
        currentAnimation = EnemyAnimations.RUN;
    }

    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g, int offsetX, int offsetY) {
        if (kappa.isDead()) return;
        if (showHitBox) {
            Rectangle2D.Float hitbox = kappa.getHitbox();
            g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            int radius = 150;
            int diameter = radius * 2;
            int startAngle = 0;
            g2d.drawArc((int) kappa.getX() - offsetX - radius + 50, (int) kappa.getY() - offsetY - 50, diameter, diameter, startAngle, 180);
        }
    }

    @Override
    void updateAnimationTick() {
        setAnimation();

        aniTick ++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }

        if (aniIndex >= currentAnimation.getAniSize()) {
            aniIndex = 0;
        }
    }

    private void setAnimation() {
        if (kappa.isDead()) currentAnimation = EnemyAnimations.DEAD;
        else {
            if (kappa.isPlayerNear()) {
                if (kappa.isAttacking() && !kappa.hasAttacked()) {
                    currentAnimation = EnemyAnimations.ATTACK;
                } else {
                    currentAnimation = EnemyAnimations.IDLE;
                }
            } else {
                currentAnimation = EnemyAnimations.RUN;
            }
        }
    }

    void drawAttackHitbox(Graphics g, int offsetX, int offsetY) {
        if (kappa.isDead()) return;
        if (kappa.isAttacking() && showHitBox) {
            Rectangle2D.Float attackHitbox = kappa.getAttackHitbox();
            g.setColor(Color.RED);
            g.drawRect((int) attackHitbox.x - offsetX, (int) attackHitbox.y - offsetY, (int) attackHitbox.width, (int) attackHitbox.height);
        }
    }

    @Override
    public void drawAnimations(Graphics g, int offsetX, int offsetY) {

        updateAnimationTick();

        showLeftAnimations = !kappa.isMoveRight();

        int yPos = (int) kappa.getY();
        if (kappa.isDead()) {
            yPos -= 8;
        }

        g.drawImage(animations[currentAnimation.getAniIndex() + (showLeftAnimations ? SPRITE_Y_DIMENSION : 0)][aniIndex],
                (int) kappa.getX() - offsetX,
                yPos - offsetY,
                (int) (SPRITE_PX_WIDTH * 1.5),
                (int) (SPRITE_PX_HEIGHT * 1.5), null);
        drawHitBox(g, offsetX, offsetY);
        drawAttackHitbox(g, offsetX, offsetY);
        drawHealthBar(g, offsetX, offsetY);
    }

    public void drawHealthBar(Graphics g, int offsetX, int offsetY) {
        if (kappa.isDead()) return;

        if (kappa.getHealth() < kappa.getMaxHealth()) {
            int healthBarHeight = 10;
            int healthBarWidth = (int) (kappa.getHitbox().width * 0.7);
            int healthBarX = (int) (kappa.getX() + kappa.getHitbox().width / 2 - healthBarWidth / 2) - offsetX + 10;
            int healthBarY = (int) kappa.getY() - healthBarHeight - 5 - offsetY;

            int currentHealthBarWidth = (int) ((kappa.getHealth() / (float) kappa.getMaxHealth()) * healthBarWidth);

            g.setColor(Color.RED);
            g.fillRect(healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);
            g.setColor(Color.BLACK);
            g.drawRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        }
    }
}
