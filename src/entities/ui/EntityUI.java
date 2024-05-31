package entities.ui;

import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;


abstract public class EntityUI {
    protected BufferedImage[][] animations;
    protected boolean showLeftAnimations;
    protected int aniTick;
    protected int aniIndex;
    protected int aniSpeed = 30;
    protected int SPRITE_PX_WIDTH;
    protected int SPRITE_PX_HEIGHT;
    protected String ENTITY_SPRITE_PATH;
    protected String ENTITY_SPRITE_PATH_LEFT;
    protected int SPRITE_Y_DIMENSION;
    protected int SPRITE_X_DIMENSION;


    abstract void drawAttackBox();

    abstract void drawHitBox(Graphics g, int offsetX, int offsetY);

    protected void loadAnimations() {
        animations = new BufferedImage[SPRITE_Y_DIMENSION * 2][SPRITE_X_DIMENSION];
        loadAnimationSprites(ENTITY_SPRITE_PATH, animations, 0);
        Optional.ofNullable(ENTITY_SPRITE_PATH_LEFT).ifPresent(path -> loadAnimationSprites(path, animations, SPRITE_Y_DIMENSION));
    }

    private void loadAnimationSprites(String spritePath, BufferedImage[][] animations, int offset) {
        BufferedImage img = SpriteManager.GetSpriteAtlas(spritePath);
        for (int j = 0; j < animations.length / 2; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j + offset][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);
    }

    abstract void updateAnimationTick();

    abstract void drawAnimations(Graphics g, int offsetX, int offsetY);
}
