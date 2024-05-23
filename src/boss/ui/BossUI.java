package boss.ui;

import boss.logic.Boss;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BossUI {
    private final static int BIG_PROJECTILE_ANI_LENGTH = 5;
    private final static String BIG_PROJECTILE_SPRITE_PATH = "boss/fireball.png";
    private final static int SPRITE_PX_WIDTH = 33;
    private final static int SPRITE_PX_HEIGHT = 17;
    private final static int ANI_SPEED = 20;
    private int aniTick;
    private int bigProjectileAniIndex;
    private Boss currentBoss;
    private BufferedImage[] bigProjectile;
    private boolean showHitBox;

    public BossUI(Boss currentBoss, boolean showHitBox) {
        this.currentBoss = currentBoss;
        this.showHitBox = showHitBox;
        this.bigProjectile = new BufferedImage[BIG_PROJECTILE_ANI_LENGTH];
        this.aniTick = 0;
        this.bigProjectileAniIndex = 0;
        loadAnimations();
    }

    public void draw(Graphics g, int offset) {
        updateAnimationTick();
        if(showHitBox && currentBoss != null) {
            drawProjectileHitbox(g, offset);
            drawHitbox(g, offset);
            drawMiniProjectileHitboxes(g,offset);
        }
        drawBigProjectile(g, offset);
    }

    //load animations

    private void loadAnimations() {
        loadBigProjectileAnimationSprites();
    }

    private void loadBigProjectileAnimationSprites() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(BIG_PROJECTILE_SPRITE_PATH);
        for (int j = 0; j < bigProjectile.length; j++)
            bigProjectile[j] = img.getSubimage(
                    j * SPRITE_PX_WIDTH,
                    0,
                    SPRITE_PX_WIDTH,
                    SPRITE_PX_HEIGHT);
    }


    //Update animations
    void updateAnimationTick() {
        aniTick++;

        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            bigProjectileAniIndex++;
            if (bigProjectileAniIndex >= BIG_PROJECTILE_ANI_LENGTH) {
                bigProjectileAniIndex = 0;
            }
        }
    }


    //Draw hitboxes
    public void drawHitbox(Graphics g, int offset) {
        Rectangle2D.Float hitbox = currentBoss.getHitbox();
        g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void drawProjectileHitbox(Graphics g, int offset) {
        Rectangle2D.Float hitbox = currentBoss.getProjectileHitbox();
        g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    public void drawMiniProjectileHitboxes(Graphics g, int offset) {
        for(Rectangle2D.Float hitbox: currentBoss.getMiniProjectileHitboxes()) {
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    //Draw Animations
    public void drawBigProjectile(Graphics g, int offset) {
        Rectangle2D.Float hitbox = currentBoss.getProjectileHitbox();
        g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        g.drawImage(bigProjectile[bigProjectileAniIndex],
                (int) hitbox.x - offset,
                (int) hitbox.y,
                (int) (SPRITE_PX_WIDTH),
                (int) (SPRITE_PX_HEIGHT), null);
    }
}
