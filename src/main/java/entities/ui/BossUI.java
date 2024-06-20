package entities.ui;

import entities.logic.Boss;
import game.UI.GameView;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class BossUI extends EntityUI {

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
        super();
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

    @Override
    void drawAttackBox() {
        //is not used
    }

    @Override
    void drawHitBox(Graphics g, int offsetX, int offsetY) {
        //empty is not used
    }

    @Override
    public void drawAnimations(Graphics g, int offsetX, int offsetY) {
        if (currentBoss != null) {
            if (!currentBoss.getIsDead()) {
                drawBoss(g, offsetX, offsetY);
                if (currentBoss.getIsDead()) return;
                updateAnimationTick();
                if (currentBoss.getIsUsingBigProjectile()) {
                    drawBigProjectile(g, offsetX, offsetY);
                    if (showHitBox) drawBigProjectileHitbox(g, offsetX, offsetY);
                } else {
                    drawMiniProjectile(g, offsetX, offsetY);
                    if (showHitBox) drawMiniProjectileHitboxes(g, offsetX, offsetY);
                }
                if (showHitBox) drawBossHitbox(g, offsetX, offsetY);

            }
            drawHealthBar(g);

        }
    }
    //load animations

   @Override
   public void loadAnimations() {
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



    //Update animations
    @Override
    public void updateAnimationTick() {
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

    public void drawHealthBar(Graphics g) {
        int healthBarHeight = GameView.GAME_HEIGHT / 30;
        int healthBarWidth = GameView.GAME_WIDTH - GameView.GAME_WIDTH / 2;
        int healthBarX = (GameView.GAME_WIDTH / 2) / 2;
        int healthBarY = GameView.GAME_HEIGHT / 8;

        int cornerRadius = 30;

        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(healthBarX - healthBarWidth / 50, healthBarY - healthBarHeight * 2, healthBarWidth + (healthBarWidth / 50) * 2, healthBarHeight * 3 + healthBarHeight / 2, cornerRadius, cornerRadius);

        g.setColor(Color.WHITE);
        g.setFont(new Font("MS GOTHIC", Font.PLAIN, GameView.GAME_WIDTH / 55));
        g.drawString(currentBoss.getName(), healthBarX, healthBarY - healthBarHeight / 2);

        int currentHealthBarWidth = (int) ((currentBoss.getHealth() / (float) currentBoss.getMaxHealth()) * healthBarWidth);

        g.setColor(Color.WHITE);
        g.fillRoundRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight, cornerRadius, cornerRadius);
        g.setColor(Color.BLACK);
        g.drawRoundRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight, cornerRadius, cornerRadius);
        g.setColor(Color.RED);
        g.fillRoundRect(healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight, cornerRadius, cornerRadius);

    }
}
