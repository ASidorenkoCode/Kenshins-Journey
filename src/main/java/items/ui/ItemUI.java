package items.ui;

import items.logic.Item;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemUI {
    private static final int ANI_SPEED = 20;
    private final int SPRITE_PX_WIDTH = 32;
    private final int SPRITE_PX_HEIGHT = 32;
    private final String ENTITY_SPRITE_PATH = "items.png";
    private final int SPRITE_X_DIMENSION = 7;
    private final int SPRITE_Y_DIMENSION = 2;
    private int aniTick;
    private int aniIndex;

    public ItemUI() {
        this.aniIndex = 0;
    }

    private void drawItem(Item item, Graphics g, int offsetX, int offsetY, boolean showHitBox, BufferedImage[][] animations) {
        g.drawImage(animations[item.getAnimationType().getAniIndex()][aniIndex],
                (int) item.getX() - offsetX,
                (int) item.getY() - offsetY,
                SPRITE_PX_WIDTH * 2,
                SPRITE_PX_HEIGHT * 2, null);
        if (showHitBox) {
            Rectangle2D.Float hitbox = item.getHitbox();
            g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
        }
    }

    public void drawMapItems(Graphics g, int offsetX, int offsetY, ArrayList<Item> itemsOnMap, boolean showHitBox, BufferedImage[][] animations) {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 7) aniIndex = 0;
        }

        for (int i = 0; i < itemsOnMap.size(); i++) {
            drawItem(itemsOnMap.get(i), g, offsetX, offsetY, showHitBox, animations);
        }
    }

    public void drawStaticItemImage(Graphics g, Item item, int x, int y, BufferedImage[][] animations) {
        g.drawImage(animations[item.getAnimationType().getAniIndex()][0],
                x,
                y,
                SPRITE_PX_WIDTH * 2,
                SPRITE_PX_HEIGHT * 2, null);
    }

    public int getSPRITE_PX_WIDTH() {
        return SPRITE_PX_WIDTH;
    }

    public int getSPRITE_PX_HEIGHT() {
        return SPRITE_PX_HEIGHT;
    }

    public String getENTITY_SPRITE_PATH() {
        return ENTITY_SPRITE_PATH;
    }

    public int getSPRITE_X_DIMENSION() {
        return SPRITE_X_DIMENSION;
    }

    public int getSPRITE_Y_DIMENSION() {
        return SPRITE_Y_DIMENSION;
    }
}
