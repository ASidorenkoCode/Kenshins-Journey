package entities.ui;

import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;



abstract public class EntityUI {
    protected BufferedImage[][] animationsLeft;

    protected BufferedImage[][] animationsRight;
    protected int aniTick;
    protected int aniIndex;
    protected final static int ANI_SPEED = 25;
    protected int SPRITE_PX_WIDTH;
    protected int SPRITE_PX_HEIGHT;
    protected String ENTITY_SPRITE_PATH_LEFT;
    protected String ENTITY_SPRITE_PATH_RIGHT;
    protected int SPRITE_Y_DIMENSION;
    protected int SPRITE_X_DIMENSION;

    protected float TILE_SCALE;


    abstract void drawAttackBox();
    abstract void drawHitBox();
    protected void loadAnimations() {
        //TODO: Implement GetSpriteAtlas#
        BufferedImage img = SpriteManager.GetSpriteAtlas(ENTITY_SPRITE_PATH_LEFT);
        animationsLeft = new BufferedImage[SPRITE_Y_DIMENSION][SPRITE_X_DIMENSION];
        for (int j = 0; j < animationsLeft.length; j++)
            for (int i = 0; i < animationsLeft[j].length; i++)
                animationsLeft[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);

        //TODO: DRY Pattern betrachten
        img = SpriteManager.GetSpriteAtlas(ENTITY_SPRITE_PATH_RIGHT);
        animationsRight = new BufferedImage[SPRITE_Y_DIMENSION][SPRITE_X_DIMENSION];
        for (int j = 0; j < animationsRight.length; j++)
            for (int i = 0; i < animationsRight[j].length; i++)
                animationsRight[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);
    };
    abstract void drawAnimations(Graphics g);
    abstract void drawHealthBar();
}
