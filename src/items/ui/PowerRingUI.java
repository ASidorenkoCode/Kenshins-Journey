package items.ui;

import constants.Constants;
import items.logic.Heart;
import items.logic.PowerRing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class PowerRingUI extends ItemUI {
    PowerRing powerRing;
    boolean showHitBox;

    public PowerRingUI(PowerRing powerRing, boolean showHitBox) {
        this.powerRing = powerRing;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 16;
        SPRITE_PX_HEIGHT = 16;
        ENTITY_SPRITE_PATH = "items/power_ring.png";
        SPRITE_X_DIMENSION = 1;
        MENU_SPRITE_PATH = "items/power_ring.png";
        loadAnimations();

    }

    @Override
    public void drawHitBox(Graphics g, int offset) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = powerRing.getHitbox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }
    @Override
    public void drawAnimations(Graphics g, int offset) {
        updateAnimationTick();
        g.drawImage(animations[aniIndex],
                (int) powerRing.getX() - offset,
                (int) powerRing.getY(),
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
