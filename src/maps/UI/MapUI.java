package maps.UI;

import game.UI.GameView;
import maps.controller.MapController;
import maps.logic.Map;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapUI {
    private final MapController mapController;
    private ArrayList<Map> maps;
    private final static String MAPSPRITE_PATH = "mapsprites.png";
    private BufferedImage[] mapSprites;


    public MapUI(MapController mapController) {
        this.mapController = mapController;
        this.maps = mapController.getMaps();
        importTileSheets();
        buildAllMaps();
    }

    public void draw(Graphics g, int mapOffset, int mapIndex) {
        for (int j = 0; j < GameView.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < maps.get(mapIndex).getMapData()[0].length; i++) {
                int index = maps.get(mapIndex).getSpriteIndex(i, j);
                if (index >= 0 && index < getMapSprites().length) {
                    int x = GameView.TILES_DEFAULT_SIZE * 2 * i - mapOffset;
                    int y = GameView.TILES_DEFAULT_SIZE * 2 * j;
                    g.drawImage(getMapSprites()[index], x, y, GameView.TILES_DEFAULT_SIZE * 2, GameView.TILES_DEFAULT_SIZE * 2, null);
                } else {
                    System.out.println("Index out of bounds: " + index);
                }
            }
    }

    private void buildAllMaps() {
        BufferedImage[] allLevels = SpriteManager.GetAllMaps();
        for (BufferedImage img : allLevels)
            maps.add(new Map(img));
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

    public BufferedImage[] getMapSprites() {
        return mapSprites;
    }

}