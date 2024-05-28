package maps.ui;

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

    public void draw(Graphics g, int mapOffsetX, int mapOffsetY, int mapIndex, boolean isForeGround) {
        if (!isForeGround) {
            BufferedImage background = getBackgroundImage();
            g.drawImage(background, 0, 0, null);
        }

        int mapHeight = mapController.getCurrentMap().getHeight();
        for (int j = 0; j < mapHeight; j++)
            for (int i = 0; i < maps.get(mapIndex).getMapData()[0].length; i++) {
                int index = maps.get(mapIndex).getSpriteIndex(i, j);
                if (index >= 0 && index < getMapSprites().length) {
                    int x = GameView.TILES_DEFAULT_SIZE * 2 * i - mapOffsetX;
                    int y = GameView.TILES_DEFAULT_SIZE * 2 * j - mapOffsetY;
                    if (index > 48 && index < 75 && isForeGround) {
                        g.drawImage(getMapSprites()[index], x, y, GameView.TILES_DEFAULT_SIZE * 2, GameView.TILES_DEFAULT_SIZE * 2, null);
                    } else if (!isForeGround)
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

    public BufferedImage getBackgroundImage() {
        int width = GameView.TILES_DEFAULT_SIZE * 2 * maps.get(0).getMapData()[0].length;
        int height = GameView.TILES_IN_HEIGHT * GameView.TILES_DEFAULT_SIZE * 2;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.decode("#639bff")); // TODO: change to a bakckground image later on
        g.fillRect(0, 0, width, height);
        g.dispose();
        return image;
    }

    private void importTileSheets() {
        BufferedImage img = SpriteManager.GetSpriteAtlas(MAPSPRITE_PATH);
        int subImageWidth = 32;
        int subImageHeight = 32;
        int rows = img.getHeight() / subImageHeight;
        int cols = img.getWidth() / subImageWidth;
        mapSprites = new BufferedImage[rows * cols];
        for (int j = 0; j < rows; j++)
            for (int i = 0; i < cols; i++) {
                int index = j * cols + i;
                mapSprites[index] = img.getSubimage(i * subImageWidth, j * subImageHeight, subImageWidth, subImageHeight);
            }
    }

    public BufferedImage[] getMapSprites() {
        return mapSprites;
    }

}