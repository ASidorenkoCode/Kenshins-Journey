package items.ui;

import constants.Constants;
import items.logic.Heart;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HeartUI extends ItemUI {
    Heart heart;
    boolean showHitBox;

    public HeartUI(Heart heart, boolean showHitBox) {
        this.heart = heart;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 32;
        SPRITE_PX_HEIGHT = 32;
        ENTITY_SPRITE_PATH = "items/heart_item.png";
        SPRITE_X_DIMENSION = 7;
        loadAnimations();

    }

    @Override
    public void drawHitBox(Graphics g, int offset) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = heart.getHitbox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }
    @Override
    public void drawAnimations(Graphics g, int offset) {
        updateAnimationTick();
        g.drawImage(animations[aniIndex],
                (int) heart.getX() - offset,
                (int) heart.getY(),
                (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
        drawHitBox(g, offset);

    }

    @Override
    void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= 4) {
                aniIndex = 0;
            }
        }
    }
}
