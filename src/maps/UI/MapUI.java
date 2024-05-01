package maps.UI;

import game.UI.GameView;
import maps.controller.MapManager;
import maps.logic.Map;

import java.awt.*;
import java.util.ArrayList;

public class MapUI {
    private final MapManager mapManager;
    private ArrayList<Map> maps;
    private int mapIndex = 0;

    public MapUI() {
        this.mapManager = new MapManager();
        this.maps = mapManager.getMaps();
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < GameView.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < maps.get(mapIndex).getMapData()[0].length; i++) {
                int index = maps.get(mapIndex).getSpriteIndex(i, j);
                if (index >= 0 && index < mapManager.getMapSprites().length) {
                    int x = GameView.TILES_DEFAULT_SIZE * 2 * i - lvlOffset;
                    int y = GameView.TILES_DEFAULT_SIZE * 2 * j;
                    g.drawImage(mapManager.getMapSprites()[index], x, y, GameView.TILES_DEFAULT_SIZE* 2, GameView.TILES_DEFAULT_SIZE * 2, null);
                } else {
                    System.out.println("Index out of bounds: " + index);
                }
            }
    }
    public Map getCurrentMap() {
        return mapManager.getCurrentMap();
    }
}