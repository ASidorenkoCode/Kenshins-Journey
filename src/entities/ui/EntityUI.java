package entities.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class EntityUI {
    protected BufferedImage[][] animations;
    protected int aniTick;
    protected int aniIndex;
    protected final static int ANI_SPEED = 25;
    protected int SPRITE_PX_WIDTH;
    protected int SPRITE_PX_HEIGHT;
    protected String ENTITY_SPRITE_PATH;
    protected int SPRITE_Y_DIMENSION;
    protected int SPRITE_X_DIMENSION;


    abstract void drawAttackBox();
    abstract void drawHitBox();
    protected void loadAnimations() {
        //TODO: Implement GetSpriteAtlas
        BufferedImage img = LoadSave.GetSpriteAtlas(ENTITY_SPRITE_PATH);
        animations = new BufferedImage[SPRITE_Y_DIMENSION][SPRITE_X_DIMENSION];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);
    };
    abstract void drawAnimations(Graphics g);
    abstract void drawHealthBar();
}
