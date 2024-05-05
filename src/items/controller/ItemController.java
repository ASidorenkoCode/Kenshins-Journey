package items.controller;

import entities.logic.Player;
import items.logic.Heart;
import items.logic.Item;
import items.logic.PowerRing;
import items.ui.HeartUI;
import items.ui.ItemUI;
import items.ui.PowerRingUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class ItemController {

    private HeartUI heartUI;
    private ArrayList<Heart> hearts;

    private PowerRing powerRing;
    private PowerRingUI powerRingUI;

    private ItemUI[] menu;

    public ItemController(boolean showHitBox) {
        initDummyHearts();
        heartUI = new HeartUI(hearts, showHitBox);
        menu = new ItemUI[10];

        powerRing = new PowerRing(200, 200);
        powerRingUI = new PowerRingUI(powerRing, showHitBox);

        initDummyMenu();
    }

    public void update(Player player) {
        for(Heart h: hearts) {
            h.handleItem(player);
        }
    }

    public void drawItems(Graphics g, int offset) {
        heartUI.drawAnimations(g, offset);
        drawMenu(g);
    }


    //Dummy data
    public void initDummyHearts() {
        hearts = new ArrayList<>();
        //TODO: Add based on rgb
        hearts.add(new Heart(400, 500));
        hearts.add(new Heart(500, 500));
        hearts.add(new Heart(600, 500));
        hearts.add(new Heart(700, 500));

    }

    //Dummy menu data
    public void initDummyMenu() {
        menu[0] = powerRingUI;
        menu[1] = powerRingUI;
        menu[2] = powerRingUI;
        menu[3] = powerRingUI;
        menu[4] = powerRingUI;
        menu[5] = powerRingUI;
        menu[6] = powerRingUI;
        menu[7] = powerRingUI;
        menu[8] = powerRingUI;
        menu[9] = powerRingUI;



    }

    public void drawMenu(Graphics g) {
        int startingX = 100;
        int y = 800;

        g.fillRect(100, 800, 950, 74);
        for(ItemUI i: menu) {
            if(i != null) {
                i.drawMenuImage(startingX, y, g);
                startingX+=100;
            }

        }
    }
}
