package items.ui;


import constants.Constants;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class ItemUI {
    protected BufferedImage[] animations;
    protected BufferedImage menuImage;
    protected int aniTick;
    protected int aniIndex;
    protected int aniSpeed = 20;
    protected int SPRITE_PX_WIDTH;
    protected int SPRITE_PX_HEIGHT;
    protected String ENTITY_SPRITE_PATH;
    protected String MENU_SPRITE_PATH;
    protected int SPRITE_X_DIMENSION;
    abstract void drawHitBox(Graphics g, int offset);

    protected void loadAnimations() {
        animations = new BufferedImage[SPRITE_X_DIMENSION];
        BufferedImage img = SpriteManager.GetSpriteAtlas(ENTITY_SPRITE_PATH);
        for (int j = 0; j < animations.length; j++)
            animations[j] = img.getSubimage(
                    j * SPRITE_PX_WIDTH,
                    0,
                    SPRITE_PX_WIDTH,
                    SPRITE_PX_HEIGHT);

        menuImage = SpriteManager.GetSpriteAtlas(MENU_SPRITE_PATH);
    }

    public void drawMenuImage(float x, float y, Graphics g) {
        g.drawImage(menuImage, (int) x, (int) y, (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE), (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
    }

    abstract void updateAnimationTick();
    abstract void drawAnimations(Graphics g, int offset);

}
