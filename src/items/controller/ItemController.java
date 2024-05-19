package items.controller;
import entities.controller.EntityController;
import entities.logic.Player;
import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;
import maps.controller.MapController;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

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

    public ItemController(MapController mapController, boolean showHitBox) {
        this.showHitBox = showHitBox;
        menu = new Item[5];
        loadAnimations();
        aniIndex = 0;
        initItems(mapController);
    }

    public void initItems(MapController mapController) {
        itemsOnMap = new ArrayList<>();
        if(mapController.getCurrentItemSpawns().isEmpty()) return;
        int itemPlacementCount = 0;
        while(itemPlacementCount == 0) {
            for(Point p: mapController.getCurrentItemSpawns()) {
                Random random = new Random();
                double probability = random.nextDouble();
                if (probability <= 0.25) {
                    System.out.println(true);
                    itemsOnMap.add(new Heart(p.x,p.y));
                    itemPlacementCount++;
                }
            }
        }
    }
    public void update(EntityController entityController) {
        for(int i=0;i<itemsOnMap.size();i++) {
            if(entityController.getPlayer().getHitbox().intersects(itemsOnMap.get(i).getHitbox())) collectItem(itemsOnMap.get(i));
        }
    }

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
                SPRITE_PX_WIDTH * 2,
                SPRITE_PX_HEIGHT * 2, null);
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

    private void collectItem(Item item) {
        //TODO: dynamic menu
        for(int i=0; i< menu.length; i++) {
            if(menu[i] == null) {
                menu[i] = item;
                itemsOnMap.remove(item);
                return;
            }
        }



    }

    public void selectItem(Player player, int index) {
        Item selectedItem = menu[index];
        menu[index] = null;

        if(selectedItem instanceof Heart heart) {
            heart.handleItem(player);
            return;
        }

        if(selectedItem instanceof PowerRing powerRing) {
            powerRing.handleItem(player);
        }
    }


    public Item[] getMenu() {
        return menu;
    }

    public void drawStaticItemImage(Graphics g, Item item, int x, int y) {
        g.drawImage(animations[item.getAnimationType().getAniIndex()][0],
                x,
                y,
                SPRITE_PX_WIDTH * 2,
                SPRITE_PX_HEIGHT * 2, null);
    }


}
