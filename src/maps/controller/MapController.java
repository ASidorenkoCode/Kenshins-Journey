package maps.controller;

import entities.logic.Entity;
import maps.UI.MapUI;
import maps.logic.Map;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapController {
    private ArrayList<Map> maps;
    private BufferedImage[] mapSprites;

    private final static String MAPSPRITE_PATH = "mapsprites.png";

    public MapController() {
        importTileSheets();
        maps = new ArrayList<>();
        buildAllMaps();
    }

    private void importTileSheets() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(MAPSPRITE_PATH);
        mapSprites = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                mapSprites[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }    }

    private void buildAllMaps() {
        BufferedImage[] allLevels = SpriteManager.GetAllMaps();
        for (BufferedImage img : allLevels)
            maps.add(new Map(img));
    }

    public void loadNextMap() {
        int currentMapIndex = 0;
    }

    public int getAmountOfMaps() {
        return maps.size();
    }

    public Map getCurrentMap() {
        return maps.get(0);
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public BufferedImage[] getMapSprites() {
        return mapSprites;
    }

    public boolean checkCollisionOfEntityWithCurrentMapTiles(Entity entity) {
        Rectangle2D.Float hitbox = entity.getHitbox();
        int[][] mapData = getCurrentMap().getMapData();
        //bottom left corner
        int tile_x = (int) (hitbox.x / mapData.length);
        int tile_y = (int) (hitbox.y + hitbox.height / mapData[0].length);
        if(mapData[tile_x][tile_y] != 11) return true;

        //bottom right corner
        tile_x = (int) (hitbox.x + hitbox.width / mapData.length);
        tile_y = (int) (hitbox.y + hitbox.height / mapData[0].length);
        return mapData[tile_x][tile_y] != 11;

    }
}