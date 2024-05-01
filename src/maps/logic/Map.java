package maps.logic;

import constants.Constants;
import game.UI.GameView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map {
    private BufferedImage mapImage;
    private int[][] mapData;
    private int[][] entities;
    private int[][] objects;

    private int mapTilesWide;
    private int maxTilesOffset;
    private int maxMapOffsetX;
    private Point playerSpawn;

    public Map(BufferedImage img) {
        this.mapImage = img;
        mapData = new int[img.getHeight()][img.getWidth()];
        loadMap();
        calculateMapOffsets();
    }

    private void loadMap() {
        for (int y = 0; y < mapImage.getHeight(); y++)
            for (int x = 0; x < mapImage.getWidth(); x++) {
                Color c = new Color(mapImage.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadMapData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }

    }


    private void loadMapData(int redValue, int x, int y) {
        if (redValue >= 48)
            mapData[y][x] = 0;
        else
            mapData[y][x] = redValue;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case 100 -> playerSpawn = new Point(x * GameView.TILES_DEFAULT_SIZE, y * GameView.TILES_DEFAULT_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        // TODO: implement objects
    }

    private void calculateMapOffsets() {
        mapTilesWide = mapImage.getWidth();
        maxTilesOffset = mapTilesWide - GameView.TILES_IN_WIDTH;
        maxMapOffsetX = (int) (GameView.TILES_DEFAULT_SIZE * Constants.TILE_SCALE * maxTilesOffset);
    }

    public int getmaxMapOffsetX() {
        return maxMapOffsetX;
    }

    public int[][] getMapData() {
        return mapData;
    }

    public int getSpriteIndex(int x, int y) {
        return mapData[y][x];
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }


}