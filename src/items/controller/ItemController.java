package items.controller;

import constants.Constants;
import entities.logic.Player;
import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ItemController {
    //Rebuild of class
    private int aniTick;
    private final boolean showHitBox;
    private static final int ANI_SPEED = 20;
    private static final int SPRITE_PX_WIDTH = 32;
    private static final int SPRITE_PX_HEIGHT = 32;
    private static final String ENTITY_SPRITE_PATH = "items.png";
    private static final int SPRITE_X_DIMENSION = 7;
    private static final int SPRITE_Y_DIMENSION = 2;
    private BufferedImage[][] animations;
    private Item[] menu;
    private ArrayList<Item> itemsOnMap;
    private int aniIndex;

    public ItemController(boolean showHitBox) {
        this.showHitBox = showHitBox;
        menu = new Item[10];
        loadAnimations();
        itemsOnMap = new ArrayList<>();
        aniIndex = 0;


        addItemToMap(new Heart(300, 600));
        addItemToMap(new PowerRing(800, 550));
    }

    public void update(Player player) {
        for(int i=0;i<itemsOnMap.size();i++) {
            if(player.getHitbox().intersects(itemsOnMap.get(i).getHitbox())) collectItem(itemsOnMap.get(i));
        }
    }


    //Dummy menu data
    public void initDummyMenu() {
        menu[0] = itemsOnMap.get(0);
    }



    //Rebuild of class

    private void loadAnimations() {
        animations = new BufferedImage[SPRITE_Y_DIMENSION][SPRITE_X_DIMENSION];
        BufferedImage img = SpriteManager.GetSpriteAtlas(ENTITY_SPRITE_PATH);
        for (int j = 0; j < animations.length; j++)
            for (int i=0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);
            }
    }

    private void updateAnimationsTicks() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if(aniIndex>=7) aniIndex = 0;
        }
    }

    private void drawItem(Item item, Graphics g, int offset) {
        g.drawImage(animations[item.getAnimationType().getAniIndex()][aniIndex],
        (int) item.getX() - offset,
                (int) item.getY(),
                (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
        if (showHitBox) {
            Rectangle2D.Float hitbox = item.getHitbox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    public void drawMapItems(Graphics g, int offset) {
        updateAnimationsTicks();
        for(int i=0; i<itemsOnMap.size(); i++) {
            drawItem(itemsOnMap.get(i), g, offset);
        }
    }

    private void addItemToMap(Item item) {
        itemsOnMap.add(item);
    }

    private void collectItem(Item item) {
        //TODO: dynamic menu
        menu[1] = item;

        itemsOnMap.remove(item);

    }

    public void selectItem(Player player, int index) {
        Item selectedItem = menu[index];
        menu[index] = null;

        if(selectedItem instanceof Heart) {
            Heart heart = (Heart) selectedItem;
            heart.handleItem(player);
        }
    }


}
