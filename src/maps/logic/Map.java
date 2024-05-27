package maps.logic;

import game.UI.GameView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map {
    private BufferedImage mapImage;
    private int[][] mapData;
    private int[][] entities;
    private int[][] objects;

    private int mapTilesWide;
    private int maxTilesOffset;
    private int maxMapOffsetX;
    private Point playerSpawn;
    private Point finishSpawn;
    private ArrayList<Point> kappaSpawns;

    private ArrayList<Point> itemSpawns;
    private Point bossSpawn;

    public Map(BufferedImage img) {
        this.kappaSpawns = new ArrayList<>();
        this.itemSpawns = new ArrayList<>();
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
        if (redValue >= 80)
            mapData[y][x] = 0;
        else
            mapData[y][x] = redValue;
    }

    private void loadEntities(int greenValue, int x, int y) {
        int originalX = x * GameView.TILES_DEFAULT_SIZE * 2;
        int scaledX = originalX - 96;
        int originalY = y * GameView.TILES_DEFAULT_SIZE * 2;
        int scaledY = originalY - 65;

        switch (greenValue) {
            case 100 -> playerSpawn = new Point(scaledX, scaledY);
            case 101 -> finishSpawn = new Point(originalX, originalY);
            case 102 -> kappaSpawns.add(new Point(originalX, originalY));
            case 104 -> bossSpawn = new Point(originalX, originalY);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        int originalX = x * GameView.TILES_DEFAULT_SIZE * 2;
        int originalY = y * GameView.TILES_DEFAULT_SIZE * 2;

        switch (blueValue) {
            case 100 -> itemSpawns.add(new Point(originalX, originalY));
        }
    }

    private void calculateMapOffsets() {
        mapTilesWide = mapImage.getWidth();
        maxTilesOffset = mapTilesWide - GameView.TILES_IN_WIDTH;
        maxMapOffsetX = GameView.TILES_DEFAULT_SIZE * 2 * maxTilesOffset;
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

    public Point getFinishSpawn() {
        return finishSpawn;
    }

    public ArrayList<Point> getKappaSpawns() {
        return kappaSpawns;
    }

    public ArrayList<Point> getItemSpawns() {
        return itemSpawns;
    }

    public Point getBossSpawn() {
        return bossSpawn;
    }
}