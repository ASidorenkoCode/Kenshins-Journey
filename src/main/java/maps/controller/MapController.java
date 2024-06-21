package maps.controller;

import entities.controller.EntityController;
import game.UI.GameView;
import game.logic.Highscore;
import maps.logic.Map;
import maps.ui.MapUI;

import java.awt.*;
import java.util.ArrayList;

public class MapController {
    private final ArrayList<Map> maps;
    private final MapUI mapUI;
    private EntityController entityController;
    private int mapOffsetX;
    private int mapOffsetY;
    private int currentMapIndex;

    public MapController(EntityController entityController, Highscore highscore) {
        this.entityController = entityController;
        maps = new ArrayList<>();
        mapUI = new MapUI(this);
        mapOffsetY = getCurrentPlayerSpawn().y;
        loadCurrentMapIndex(highscore.getAllHighscores().size());
    }

    public void loadCurrentMapIndex(int mapIndex) {
        currentMapIndex = mapIndex;
    }


    public Map getCurrentMap() {
        return maps.get(currentMapIndex);
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public void draw(Graphics g, boolean isForeGround) {
        checkCloseToBorder();
        mapUI.draw(g, getMapOffsetX(), getMapOffsetY(), currentMapIndex, isForeGround);
    }


    //calculates offsets for camera
    public void checkCloseToBorder() {
        int playerX = (int) entityController.getPlayer().getHitbox().x;
        int playerY = (int) entityController.getPlayer().getHitbox().y;
        int middleOfScreenX = GameView.GAME_WIDTH / 2;
        int quarterOfScreenY = GameView.GAME_HEIGHT / 2;
        int maxMapOffsetX = getCurrentMap().getmaxMapOffsetX();
        int maxMapOffsetY = getCurrentMap().getmaxMapOffsetY();
        double smoothingFactor = 0.05;

        mapOffsetX += (int) (((playerX - middleOfScreenX) - mapOffsetX) * smoothingFactor);
        mapOffsetY += (int) (((playerY - quarterOfScreenY) - mapOffsetY) * smoothingFactor);


        mapOffsetX = Math.max(Math.min(mapOffsetX, maxMapOffsetX), 0);
        mapOffsetY = Math.max(Math.min(mapOffsetY, maxMapOffsetY), 0);
    }

    public int getMapOffsetX() {
        return mapOffsetX;
    }

    public int getMapOffsetY() {
        return mapOffsetY;
    }


    public Point getCurrentPlayerSpawn() {
        return maps.get(currentMapIndex).getPlayerSpawn();
    }

    public Point getCurrentFinishSpawn() {
        return maps.get(currentMapIndex).getFinishSpawn();
    }

    public ArrayList<Point> getCurrentEnemySpawns() {
        return maps.get(currentMapIndex).getEnemySpawns();
    }

    public ArrayList<Point> getCurrentItemSpawns() {
        return maps.get(currentMapIndex).getItemSpawns();
    }

    public Point getCurrentBossSpawn() {
        return maps.get(currentMapIndex).getBossSpawn();
    }

    public int getCurrentLevelNumber() {
        return currentMapIndex + 1;
    }

    public int getCurrentMapIndex() {
        return currentMapIndex;
    }

    public MapUI getMapUI() {
        return mapUI;
    }

    public void setEntityController(EntityController entityController) {
        this.entityController = entityController;
    }
}