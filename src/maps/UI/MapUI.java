package maps.UI;

import game.UI.GameView;
import maps.controller.MapController;
import maps.logic.Map;

import java.awt.*;
import java.util.ArrayList;

public class MapUI {
    private final MapController mapController;
    private ArrayList<Map> maps;
    private int mapIndex = 0;

    public MapUI(MapController mapController) {
        this.mapController = mapController;
        this.maps = mapController.getMaps();
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < GameView.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < maps.get(mapIndex).getMapData()[0].length; i++) {
                int index = maps.get(mapIndex).getSpriteIndex(i, j);
                if (index >= 0 && index < mapController.getMapSprites().length) {
                    int x = GameView.TILES_DEFAULT_SIZE * 2 * i - lvlOffset;
                    int y = GameView.TILES_DEFAULT_SIZE * 2 * j;
                    g.drawImage(mapController.getMapSprites()[index], x, y, GameView.TILES_DEFAULT_SIZE* 2, GameView.TILES_DEFAULT_SIZE * 2, null);
                } else {
                    System.out.println("Index out of bounds: " + index);
                }
            }
    }
    public Map getCurrentMap() {
        return mapController.getCurrentMap();
    }
}