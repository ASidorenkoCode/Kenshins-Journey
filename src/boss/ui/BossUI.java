package boss.ui;

import boss.logic.Boss;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BossUI {

    //Big projectile vars
    private final static int BIG_PROJECTILE_ANI_LENGTH = 5;
    private final static String BIG_PROJECTILE_SPRITE_PATH = "boss/fireball.png";
    private final static int BIG_SPRITE_PX_WIDTH = 33;
    private final static int BIG_SPRITE_PX_HEIGHT = 17;
    private BufferedImage[] bigProjectile;
    private int bigProjectileAniIndex;

    //TODO: Use different sprites for mini projectiles
    //Mini projectile vars
    private final static int MINI_PROJECTILE_ANI_LENGTH = 5;
    private final static String MINI_PROJECTILE_SPRITE_PATH = "boss/fireball.png";
    private final static int MINI_SPRITE_PX_WIDTH = 33;
    private final static int MINI_SPRITE_PX_HEIGHT = 17;
    private BufferedImage[] miniProjectile;
    private int miniProjectileAniIndex;

    //overall used vars
    private final static int ANI_SPEED = 20;
    private int aniTick;
    private Boss currentBoss;
    private boolean showHitBox;

    public BossUI(Boss currentBoss, boolean showHitBox) {
        this.currentBoss = currentBoss;
        this.showHitBox = showHitBox;
        this.bigProjectile = new BufferedImage[BIG_PROJECTILE_ANI_LENGTH];
        this.miniProjectile = new BufferedImage[MINI_PROJECTILE_ANI_LENGTH];
        this.aniTick = 0;
        this.bigProjectileAniIndex = 0;
        this.miniProjectileAniIndex = 0;
        loadAnimations();
    }

    public void draw(Graphics g, int offset) {
        if(currentBoss != null) {
            if(currentBoss.getIsDead()) return;
            updateAnimationTick();
            if(currentBoss.getIsUsingBigProjectile()) {
                drawBigProjectile(g, offset);
                if(showHitBox) drawBigProjectileHitbox(g, offset);
            }
            else {
                drawMiniProjectile(g, offset);
                if(showHitBox) drawMiniProjectileHitboxes(g,offset);
            }
            if(showHitBox) drawHitbox(g, offset);
        }
    }

    //load animations

    private void loadAnimations() {
        loadBigProjectileAnimationSprites();
        loadMiniProjectileAnimationSprites();
    }

    private void loadBigProjectileAnimationSprites() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(BIG_PROJECTILE_SPRITE_PATH);
        for (int j = 0; j < bigProjectile.length; j++)
            bigProjectile[j] = img.getSubimage(
                    j * BIG_SPRITE_PX_WIDTH,
                    0,
                    BIG_SPRITE_PX_WIDTH,
                    BIG_SPRITE_PX_HEIGHT);
    }

    private void loadMiniProjectileAnimationSprites() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(MINI_PROJECTILE_SPRITE_PATH);
        for (int j = 0; j < miniProjectile.length; j++)
            miniProjectile[j] = img.getSubimage(
                    j * MINI_SPRITE_PX_WIDTH,
                    0,
                    MINI_SPRITE_PX_WIDTH,
                    MINI_SPRITE_PX_HEIGHT);
    }


    //Update animations
    void updateAnimationTick() {
        aniTick++;

        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            bigProjectileAniIndex++;
            miniProjectileAniIndex++;
            if (bigProjectileAniIndex >= BIG_PROJECTILE_ANI_LENGTH) {
                bigProjectileAniIndex = 0;
            }
            //Maybe you can use different anis of different size later
            if (miniProjectileAniIndex >= MINI_PROJECTILE_ANI_LENGTH) {
                miniProjectileAniIndex = 0;
            }
        }
    }


    //Draw hitboxes
    public void drawHitbox(Graphics g, int offset) {
        Rectangle2D.Float hitbox = currentBoss.getHitbox();
        g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void drawBigProjectileHitbox(Graphics g, int offset) {
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
                (int) hitbox.width,
                (int) hitbox.height, null);
    }

    public void drawMiniProjectile(Graphics g, int offset) {
        for(Rectangle2D.Float hitbox: currentBoss.getMiniProjectileHitboxes()) {
            g.drawImage(miniProjectile[miniProjectileAniIndex],
                    (int) hitbox.x - offset,
                    (int) hitbox.y,
                    (int) hitbox.width,
                    (int) hitbox.height, null);
        }
    }
}
