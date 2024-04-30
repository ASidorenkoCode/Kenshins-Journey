package entities.ui;

import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;



abstract public class EntityUI {
    protected BufferedImage[][] animationsLeft;

    protected BufferedImage[][] animationsRight;

    protected BufferedImage[][] animationsDirection;
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
    abstract void drawHitBox(Graphics g);


    protected void loadAnimations() {
        animationsLeft = loadAnimationSprites(ENTITY_SPRITE_PATH_LEFT);
        animationsRight = loadAnimationSprites(ENTITY_SPRITE_PATH_RIGHT);
    }

    private BufferedImage[][] loadAnimationSprites(String spritePath) {
        BufferedImage img = SpriteManager.GetSpriteAtlas(spritePath);
        BufferedImage[][] animations = new BufferedImage[SPRITE_Y_DIMENSION][SPRITE_X_DIMENSION];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);
        return animations;
    }
    abstract void drawAnimations(Graphics g);
    abstract void drawHealthBar();
}
