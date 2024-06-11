package entities.ui;

import entities.animations.EnemyAnimations;
import entities.logic.Enemy;
import spriteControl.SpriteData;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class EnemyUI extends EntityUI {

    private final List<SpriteData> SPRITE_PATHS = Arrays.asList(
            new SpriteData("enemies/kappa/kappaRight.png", 8, 4, 4, 8, 1, 1.5f, 0, 8),
            new SpriteData("enemies/worm/wormRight.png", 16, 9, 9, 16, 8, 2f, 30, 30)
    );
    private int idleAniSize;
    private int runAniSize;
    private int attackAniSize;
    private int deadAniSize;
    private float enemyScale;
    private float enemyYPositionAlive;
    private float enemyYPositionDead;
    private Enemy enemy;
    private EnemyAnimations currentAnimation;
    private boolean showHitBox;
    private boolean hasPlayedDeadAnimation = false;


    public EnemyUI(Enemy enemy, boolean showHitBox) {
        this.enemy = enemy;
        this.showHitBox = showHitBox;
        loadRandomSprite();
        loadAnimations();
        currentAnimation = EnemyAnimations.RUN;
    }

    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g, int offsetX, int offsetY) {
        if (enemy.isDead()) return;
        if (showHitBox) {
            Rectangle2D.Float hitbox = enemy.getHitbox();
            g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            int radius = 150;
            int diameter = radius * 2;
            int startAngle = 0;
            g2d.drawArc((int) enemy.getX() - offsetX - radius + 50, (int) enemy.getY() - offsetY - 50, diameter, diameter, startAngle, 180);
        }
    }

    @Override
    void updateAnimationTick() {
        setAnimation();

        aniTick ++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            if (currentAnimation == EnemyAnimations.DEAD && aniIndex == deadAniSize - 1) {
                hasPlayedDeadAnimation = true;
            }
            if (!(currentAnimation == EnemyAnimations.DEAD && hasPlayedDeadAnimation)) {
                aniIndex++;
            }
        }

        if (aniIndex >= currentAnimation.getAniSize(this)) {
            enemy.setIsAttacking(false);
            aniIndex = 0;
        }
    }

    public void loadRandomSprite() {
        Random rand = new Random();
        SpriteData randomSpriteData = SPRITE_PATHS.get(rand.nextInt(SPRITE_PATHS.size()));
        String randomSpritePathRight = randomSpriteData.getPath();
        String randomSpritePathLeft = randomSpritePathRight.replace("Right", "Left");

        SPRITE_PX_WIDTH = 64;
        SPRITE_PX_HEIGHT = 64;
        ENTITY_SPRITE_PATH = randomSpritePathRight;
        ENTITY_SPRITE_PATH_LEFT = randomSpritePathLeft;
        SPRITE_X_DIMENSION = randomSpriteData.getXDimension();
        SPRITE_Y_DIMENSION = 4;
        idleAniSize = randomSpriteData.getIdleAniSize();
        runAniSize = randomSpriteData.getRunAniSize();
        attackAniSize = randomSpriteData.getAttackAniSize();
        deadAniSize = randomSpriteData.getDeadAniSize();
        enemyScale = randomSpriteData.getScale();
        enemyYPositionAlive = randomSpriteData.getYAdjustmentLiving();
        enemyYPositionDead = randomSpriteData.getYAdjustmentDead();
    }

    private void setAnimation() {
        if (enemy.isDead()) currentAnimation = EnemyAnimations.DEAD;
        else {
            if (enemy.isEntityNear()) {
                if (enemy.isAttacking()) {
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
        if (enemy.isDead()) return;
        if (enemy.isAttacking() && showHitBox) {
            Rectangle2D.Float attackHitbox = enemy.getAttackHitbox();
            g.setColor(Color.RED);
            g.drawRect((int) attackHitbox.x - offsetX, (int) attackHitbox.y - offsetY, (int) attackHitbox.width, (int) attackHitbox.height);
        }
    }

    @Override
    public void drawAnimations(Graphics g, int offsetX, int offsetY) {

        updateAnimationTick();

        showLeftAnimations = !enemy.isMoveRight();

        int yPos = (int) (enemy.getY() - enemyYPositionAlive);
        if (enemy.isDead()) {
            yPos = (int) (enemy.getY() - enemyYPositionDead);
        }

        g.drawImage(animations[currentAnimation.getAniIndex() + (showLeftAnimations ? SPRITE_Y_DIMENSION : 0)][aniIndex],
                (int) enemy.getX() - offsetX,
                yPos - offsetY,
                (int) (SPRITE_PX_WIDTH * enemyScale),
                (int) (SPRITE_PX_HEIGHT * enemyScale), null);
        drawHitBox(g, offsetX, offsetY);
        drawAttackHitbox(g, offsetX, offsetY);
        drawHealthBar(g, offsetX, offsetY);
    }

    public void drawHealthBar(Graphics g, int offsetX, int offsetY) {
        if (enemy.isDead()) return;

        if (enemy.getHealth() < enemy.getMaxHealth()) {
            int healthBarHeight = 10;
            int healthBarWidth = (int) (enemy.getHitbox().width * 0.7);
            int healthBarX = (int) (enemy.getX() + enemy.getHitbox().width / 2 - healthBarWidth / 2) - offsetX + 10;
            int healthBarY = (int) enemy.getY() - healthBarHeight - 5 - offsetY;

            int currentHealthBarWidth = (int) ((enemy.getHealth() / (float) enemy.getMaxHealth()) * healthBarWidth);

            g.setColor(Color.RED);
            g.fillRect(healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);
            g.setColor(Color.BLACK);
            g.drawRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        }
    }

    public int getIdleAniSize() {
        return idleAniSize;
    }

    public int getRunAniSize() {
        return runAniSize;
    }

    public int getAttackAniSize() {
        return attackAniSize;
    }

    public int getDeadAniSize() {
        return deadAniSize;
    }
}
