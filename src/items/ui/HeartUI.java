package items.ui;

import constants.Constants;
import items.logic.Heart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class HeartUI extends ItemUI {
    ArrayList<Heart> hearts;
    boolean showHitBox;

    public HeartUI(ArrayList<Heart> hearts, boolean showHitBox) {
        this.hearts = hearts;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 32;
        SPRITE_PX_HEIGHT = 32;
        ENTITY_SPRITE_PATH = "items/heart_item.png";
        SPRITE_X_DIMENSION = 7;
        MENU_SPRITE_PATH = "items/heart_menu.png";
        loadAnimations();

    }

    @Override
    public void drawHitBox(Graphics g, int offset) {
        if (showHitBox) {
            for(Heart h: hearts) {
                if(h.getIsActive()) {
                    Rectangle2D.Float hitbox = h.getHitbox();
                    g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
                }
            }
        }
    }
    @Override
    public void drawAnimations(Graphics g, int offset) {
        for(Heart h: hearts) {
            if(h.getIsActive()) {
                updateAnimationTick();
                g.drawImage(animations[aniIndex],
                        (int) h.getX() - offset,
                        (int) h.getY(),
                        (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE),
                        (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
                drawHitBox(g, offset);
            }
        }

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
