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
    private final static int BIG_PROJECTILE_SPRITE_PX_WIDTH = 33;
    private final static int BIG_PROJECTILE_SPRITE_PX_HEIGHT = 17;
    private BufferedImage[] bigProjectile;
    private int bigProjectileAniIndex;

    //Mini projectile vars
    private final static int MINI_PROJECTILE_ANI_LENGTH = 1;
    private final static String MINI_PROJECTILE_SPRITE_PATH = "boss/mini_fireball.png";
    private final static int MINI_PROJECTILE_SPRITE_PX_WIDTH = 24;
    private final static int MINI_PROJECTILE_SPRITE_PX_HEIGHT = 20;
    private BufferedImage[] miniProjectile;
    private int miniProjectileAniIndex;

    //Boss vars
    private final static int BOSSS_ANI_LENGTH = 1;
    private final static int BOSS_NUMBER_OF_ANI = 1;
    private final static String BOSS_SPRITE_PATH = "boss/boss.png";
    private final static int BOSS_SPRITE_PX_WIDTH = 64;
    private final static int BOSS_SPRITE_PX_HEIGHT = 64;
    private BufferedImage[][] boss;
    private int bossAniIndex;

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
        this.boss = new BufferedImage[BOSS_NUMBER_OF_ANI][BOSSS_ANI_LENGTH];
        this.aniTick = 0;
        this.bigProjectileAniIndex = 0;
        this.miniProjectileAniIndex = 0;
        loadAnimations();
    }
    //load animations

    private void loadAnimations() {
        loadBigProjectileAnimationSprites();
        loadMiniProjectileAnimationSprites();
        loadBossAnimationSprites();
    }

    private void loadBigProjectileAnimationSprites() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(BIG_PROJECTILE_SPRITE_PATH);
        for (int j = 0; j < bigProjectile.length; j++)
            bigProjectile[j] = img.getSubimage(
                    j * BIG_PROJECTILE_SPRITE_PX_WIDTH,
                    0,
                    BIG_PROJECTILE_SPRITE_PX_WIDTH,
                    BIG_PROJECTILE_SPRITE_PX_HEIGHT);
    }

    private void loadMiniProjectileAnimationSprites() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(MINI_PROJECTILE_SPRITE_PATH);
        for (int j = 0; j < miniProjectile.length; j++)
            miniProjectile[j] = img.getSubimage(
                    j * MINI_PROJECTILE_SPRITE_PX_WIDTH,
                    0,
                    MINI_PROJECTILE_SPRITE_PX_WIDTH,
                    MINI_PROJECTILE_SPRITE_PX_HEIGHT);
    }

    private void loadBossAnimationSprites() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(BOSS_SPRITE_PATH);
        for (int j = 0; j < boss.length; j++)
            for (int i = 0; i < boss[j].length; i++)
                boss[j][i] = img.getSubimage(
                        i * BOSS_SPRITE_PX_WIDTH,
                        j * BOSS_SPRITE_PX_HEIGHT,
                        BOSS_SPRITE_PX_WIDTH,
                        BOSS_SPRITE_PX_HEIGHT);
    }


    public void draw(Graphics g, int offsetX, int offsetY) {
        if(currentBoss != null) {
            //TODO: fitting animations and death animation
            if(!currentBoss.getIsDead()) {
                drawBoss(g, offsetX, offsetY);
                if(currentBoss.getIsDead()) return;
                updateAnimationTick();
                if(currentBoss.getIsUsingBigProjectile()) {
                    drawBigProjectile(g, offsetX, offsetY);
                    if(showHitBox) drawBigProjectileHitbox(g, offsetX, offsetY);
                }
                else {
                    drawMiniProjectile(g, offsetX, offsetY);
                    if(showHitBox) drawMiniProjectileHitboxes(g,offsetX, offsetY);
                }
                if(showHitBox) drawBossHitbox(g, offsetX, offsetY);
            }

        }

    }

    //Update animations
    void updateAnimationTick() {
        aniTick++;

        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            bigProjectileAniIndex++;
            miniProjectileAniIndex++;
            bossAniIndex++;
            if (bigProjectileAniIndex >= BIG_PROJECTILE_ANI_LENGTH) {
                bigProjectileAniIndex = 0;
            }
            //Maybe you can use different anis of different size later
            if (miniProjectileAniIndex >= MINI_PROJECTILE_ANI_LENGTH) {
                miniProjectileAniIndex = 0;
            }
            if (bossAniIndex >= BOSSS_ANI_LENGTH) {
                bossAniIndex = 0;
            }
        }
    }


    //Draw hitboxes
    public void drawBossHitbox(Graphics g, int offsetX, int offsetY) {
        Rectangle2D.Float hitbox = currentBoss.getHitbox();
        g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
    }

    public void drawBigProjectileHitbox(Graphics g, int offsetX, int offsetY) {
        Rectangle2D.Float hitbox = currentBoss.getProjectileHitbox();
        g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
    }
    public void drawMiniProjectileHitboxes(Graphics g, int offsetX, int offsetY) {
        for(Rectangle2D.Float hitbox: currentBoss.getMiniProjectileHitboxes()) {
            g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
        }
    }

    //Draw Animations
    public void drawBoss(Graphics g, int offsetX, int offsetY) {
        Rectangle2D.Float hitbox = currentBoss.getHitbox();
        //TODO: Make multiple boss animations -> remove static 0
        g.drawImage(boss[0][bossAniIndex], (int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height, null);
    }
    public void drawBigProjectile(Graphics g, int offsetX, int offsetY) {
        Rectangle2D.Float hitbox = currentBoss.getProjectileHitbox();
        g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
        g.drawImage(bigProjectile[bigProjectileAniIndex],
                (int) hitbox.x - offsetX,
                (int) hitbox.y - offsetY,
                (int) hitbox.width,
                (int) hitbox.height, null);
    }

    public void drawMiniProjectile(Graphics g, int offsetX, int offsetY) {
        for(Rectangle2D.Float hitbox: currentBoss.getMiniProjectileHitboxes()) {
            g.drawImage(miniProjectile[miniProjectileAniIndex],
                    (int) hitbox.x - offsetX,
                    (int) hitbox.y - offsetY,
                    (int) hitbox.width,
                    (int) hitbox.height, null);
        }
    }
}
