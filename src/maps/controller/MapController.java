package maps.controller;

import entities.controller.EntityController;
import game.UI.GameView;
import maps.logic.Map;
import maps.ui.MapUI;

import java.awt.*;
import java.util.ArrayList;

public class MapController {
    private ArrayList<Map> maps;
    private MapUI mapUI;
    private EntityController entityController;
    private int mapOffset;
    private int currentMapIndex = 0;

    public MapController(EntityController entityController) {
        this.entityController = entityController;
        maps = new ArrayList<>();
        mapUI = new MapUI(this);

    }

    public void loadNextMap() {
        currentMapIndex++;
        if (currentMapIndex >= maps.size()) {
            // TODO: For now: it resets to the first map
            currentMapIndex = 0;
        }
    }


    public Map getCurrentMap() {
        return maps.get(currentMapIndex);
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public void draw(Graphics g) {
        checkCloseToBorder();
        mapUI.draw(g, getMapOffset(), currentMapIndex);
    }

    public void checkCloseToBorder() {
        int playerX = (int) entityController.getPlayer().getHitbox().x;
        int middleOfScreen = GameView.GAME_WIDTH / 2;
        int maxMapOffsetX = getCurrentMap().getmaxMapOffsetX();
        double smoothingFactor = 0.05;

        mapOffset += (int) (((playerX - middleOfScreen) - mapOffset) * smoothingFactor);
        mapOffset = Math.max(Math.min(mapOffset, maxMapOffsetX), 0);
    }

    public int getMapOffset() {
        return mapOffset;
    }

    public Point getCurrentPlayerSpawn() {
        return maps.get(currentMapIndex).getPlayerSpawn();
    }

    public Point getCurrentFinishSpawn() {
        return maps.get(currentMapIndex).getFinishSpawn();
    }

    public ArrayList<Point> getCurrentKappaSpawns() {
        return maps.get(currentMapIndex).getKappaSpawns();
    }

    public ArrayList<Point> getCurrentItemSpawns() {
        return maps.get(currentMapIndex).getItemSpawns();
    }

    public void setEntityController(EntityController entityController) {
        this.entityController = entityController;
    }

    public Point getCurrentBossSpawn() {
        return maps.get(currentMapIndex).getBossSpawn();
    }
}