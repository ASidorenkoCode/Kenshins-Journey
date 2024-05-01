package maps.controller;
import constants.Constants;
import entities.controller.EntityController;
import game.UI.GameView;
import maps.UI.MapUI;
import maps.logic.Map;
import spriteControl.SpriteManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapController {
    private ArrayList<Map> maps;
    private BufferedImage[] mapSprites;
    private MapUI mapUI;
    private EntityController entityController;
    private int mapOffset;
    private int leftBorder = (int) (0.49 * GameView.GAME_WIDTH);
    private int rightBorder = (int) (0.51 * GameView.GAME_WIDTH);
    private final static String MAPSPRITE_PATH = "mapsprites.png";

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

    public void draw(Graphics g, int offset) {
        mapUI.draw(g, offset);
    }

    public void checkCloseToBorder() {
        int playerX = (int) entityController.getPlayer().getHitbox().x;
        int diff = playerX - mapOffset;

        if (diff > rightBorder)
            mapOffset += diff - rightBorder;
        else if (diff < leftBorder)
            mapOffset += diff - leftBorder;

        mapOffset = Math.max(Math.min(mapOffset, getCurrentMap().getmaxMapOffsetX()), 0);
    }

    public int getMapOffset() {
        return mapOffset;
    }
}