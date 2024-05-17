package maps.controller;

import entities.controller.EntityController;
import game.UI.GameView;
import maps.UI.MapUI;
import maps.logic.Map;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapController {
    private final static String MAPSPRITE_PATH = "mapsprites.png";
    private ArrayList<Map> maps;
    private BufferedImage[] mapSprites;
    private MapUI mapUI;
    private EntityController entityController;
    private int mapOffset;
    private int currentMapIndex = 0;

    public MapController(EntityController entityController) {
        this.entityController = entityController;
        importTileSheets();
        maps = new ArrayList<>();
        mapUI = new MapUI(this);
        buildAllMaps();

    }

    private void importTileSheets() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(MAPSPRITE_PATH);
        mapSprites = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                mapSprites[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
    }

    private void buildAllMaps() {
        BufferedImage[] allLevels = SpriteManager.GetAllMaps();
        for (BufferedImage img : allLevels)
            maps.add(new Map(img));
    }

    public void loadNextMap() {
        currentMapIndex++;
        if (currentMapIndex >= maps.size()) {
            // TODO: For now: it resets to the first map
            currentMapIndex = 0;
        }
    }


    public int getAmountOfMaps() {
        return maps.size();
    }

    public Map getCurrentMap() {
        return maps.get(currentMapIndex);
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public BufferedImage[] getMapSprites() {
        return mapSprites;
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

}