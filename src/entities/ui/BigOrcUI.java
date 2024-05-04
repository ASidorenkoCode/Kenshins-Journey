package entities.ui;

import constants.Constants;
import entities.animations.EnemyAnimations;
import entities.logic.BigOrc;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BigOrcUI extends EntityUI {

    BigOrc bigOrc;
    boolean showHitBox;
    private EnemyAnimations currentAnimation;

    public BigOrcUI(BigOrc bigOrc, boolean showHitBox) {
        this.bigOrc = bigOrc;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 64;
        SPRITE_PX_HEIGHT = 55;
        ENTITY_SPRITE_PATH = "bigOrc/enemy1Right.png";
        ENTITY_SPRITE_PATH_LEFT = "bigOrc/enemy1Left.png";
        SPRITE_Y_DIMENSION = 2;
        SPRITE_X_DIMENSION = 4;
        loadAnimations();
        currentAnimation = EnemyAnimations.RUN;

    }

    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g, int offset) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = bigOrc.getHitbox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    @Override
    void updateAnimationTick() {

        setAnimation();
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
        }
        if (aniIndex >= SPRITE_X_DIMENSION) {
            aniIndex = 0;
        }
    }

    private void setAnimation() {
        currentAnimation = EnemyAnimations.RUN;
    }

    @Override
    public void drawAnimations(Graphics g, int offset) {

        updateAnimationTick();

        showLeftAnimations = !bigOrc.isRight();

        g.drawImage(animations[currentAnimation.getAniIndex() + (showLeftAnimations ? SPRITE_Y_DIMENSION : 0)][aniIndex],
                (int) bigOrc.getX() - offset,
                (int) bigOrc.getY(),
                (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
        drawHitBox(g, offset);
        drawHealthBar(g, offset);
    }
    public void drawHealthBar(Graphics g, int offset) {
        if (bigOrc.getHealth() < bigOrc.getMaxHealth()) {
            int healthBarHeight = 10;
            int healthBarWidth = (int) (bigOrc.getHitbox().width * 0.7);
            int healthBarX = (int) bigOrc.getX() + (int) bigOrc.getHitbox().width / 2 - healthBarWidth / 2 - offset;
            int healthBarY = (int) bigOrc.getY() - healthBarHeight - 5;

            int currentHealthBarWidth = (int) ((bigOrc.getHealth() / (float) bigOrc.getMaxHealth()) * healthBarWidth);

            g.setColor(Color.RED);
            g.fillRect(healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);
            g.setColor(Color.BLACK);
            g.drawRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        }
    }


}
