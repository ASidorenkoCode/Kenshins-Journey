package items.controller;

import entities.controller.EntityController;
import entities.logic.Player;
import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;
import items.ui.ItemUI;
import maps.controller.MapController;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class ItemController {
    //Rebuild of class
    private boolean showHitBox;
    private final static int MENU_SIZE = 5;



    //TODO: animations should be part of item as seen with object
    private BufferedImage[][] animations;
    private Item[] menu;
    private ArrayList<Item> itemsOnMap;
    private ItemUI itemUI;

    public ItemController(MapController mapController, boolean showHitBox) {
        this.showHitBox = showHitBox;
        this.itemUI = new ItemUI();
        menu = new Item[MENU_SIZE];
        loadAnimations();
        initItems(mapController);
    }

    public void initItems(MapController mapController) {
        itemsOnMap = new ArrayList<>();
        if (mapController.getCurrentItemSpawns().isEmpty()) return;
        //if Power Ring is placed set to true, because it is only allowed to use one power ring on one map
        boolean powerRingIsPlaced = false;

        //choose Items
        int itemPlacementCount = 0;
        while (itemPlacementCount == 0) {
            for (Point p : mapController.getCurrentItemSpawns()) {
                Random random = new Random();
                double probability = random.nextDouble();
                if (probability <= 0.25) {
                    if (powerRingIsPlaced) {
                        itemsOnMap.add(new Heart(p.x, p.y));
                        itemPlacementCount++;
                        continue;
                    }
                    //decide between heart and power ring

                    Random powerRingRandom = new Random();
                    double powerRingProbability = powerRingRandom.nextDouble();
                    if (powerRingProbability <= 0.25) {
                        itemsOnMap.add(new PowerRing(p.x, p.y));
                        itemPlacementCount++;
                        powerRingIsPlaced = true;
                        continue;
                    }

                    itemsOnMap.add(new Heart(p.x, p.y));
                    itemPlacementCount++;
                }
            }
        }
    }

    public void deleteAllItemsFromMenu() {
        menu = new Item[MENU_SIZE];
    }

    public void update(EntityController entityController) {
        for (int i = 0; i < itemsOnMap.size(); i++) {
            if (entityController.getPlayer().getHitbox().intersects(itemsOnMap.get(i).getHitbox()))
                collectItem(itemsOnMap.get(i));
        }
    }

    public void draw(Graphics g, int mapOffsetX, int mapOffsetY) {
        itemUI.drawMapItems(g, mapOffsetX, mapOffsetY, itemsOnMap, showHitBox, getAnimations());
    }


    //TODO: Move to ItemUI
    private void loadAnimations() {
        animations = new BufferedImage[itemUI.getSPRITE_Y_DIMENSION()][itemUI.getSPRITE_X_DIMENSION()];
        BufferedImage img = SpriteManager.GetSpriteAtlas(itemUI.getENTITY_SPRITE_PATH());
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(
                        i * itemUI.getSPRITE_PX_WIDTH(),
                        j * itemUI.getSPRITE_PX_HEIGHT(),
                        itemUI.getSPRITE_PX_WIDTH(),
                        itemUI.getSPRITE_PX_HEIGHT());
            }
    }

    private void collectItem(Item item) {
        //TODO: dynamic menu
        for (int i = 0; i < menu.length; i++) {
            if (menu[i] == null) {
                menu[i] = item;
                itemsOnMap.remove(item);
                return;
            }
        }
    }

    public void selectItem(Player player, int index) {
        index = index - 1;

        if (index < 0 || index >= menu.length) {
            return;
        }

        Item selectedItem = menu[index];

        boolean itemUsed = false;
        if (selectedItem instanceof Heart heart) {
            itemUsed = heart.handleItem(player);
        } else if (selectedItem instanceof PowerRing powerRing) {
            powerRing.handleItem(player);
            itemUsed = true;
        }

        if (itemUsed) {
            menu[index] = null;
        }
    }

    public Item[] getMenu() {
        return menu;
    }

    public ItemUI getItemUI() {
        return itemUI;
    }

    public ArrayList<Item> getItemsOnMap() {
        return itemsOnMap;
    }

    public boolean isShowHitBox() {
        return showHitBox;
    }

    public BufferedImage[][] getAnimations() {
        return animations;
    }
}
