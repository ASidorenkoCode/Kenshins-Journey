package maps.ui;

import maps.controller.MapController;
import maps.logic.Map;
import spriteControl.SpriteManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapUI {
    private final MapController mapController;
    private ArrayList<Map> maps;
    private final static String MAPSPRITE_PATH = "sprite/mapsprites.png";
    private BufferedImage[] mapSprites;
    private BufferedImage backgroundImage = null;
    private int currentMapIndex;


    public MapUI(MapController mapController) {
        this.mapController = mapController;
        this.maps = mapController.getMaps();
        importTileSheets();
        buildAllMaps();
    }

    public void draw(Graphics g, int mapOffsetX, int mapOffsetY, int mapIndex, boolean isForeGround) {
        if (!isForeGround) {
            BufferedImage background = mapBackgroundImage(mapIndex);
            g.drawImage(background, -mapOffsetX, -mapOffsetY, null);
        }

        int mapHeight = mapController.getCurrentMap().getHeight();
        int tileSize = mapController.getCurrentMap().getTileSize();
        int mapWidth = maps.get(mapIndex).getMapData()[0].length;

        for (int j = 0; j < mapHeight; j++) {
            for (int i = 0; i < mapWidth; i++) {
                int index = maps.get(mapIndex).getSpriteIndex(i, j);
                if (index >= 0 && index < getMapSprites().length) {
                    int x = tileSize * i - mapOffsetX;
                    int y = tileSize * j - mapOffsetY;
                    // Draw tiles based on foreground or background
                    if ((isForeGround && index > 48) || (!isForeGround && index <= 48)) {
                        g.drawImage(getMapSprites()[index], x, y, tileSize, tileSize, null);
                    }
                } else {
                    throw new IndexOutOfBoundsException("Index out of bounds: " + index);
                }
            }
        }
    }

    private void buildAllMaps() {
        for (BufferedImage img : SpriteManager.GetAllMaps()) {
            maps.add(new Map(img));
        }
    }

    public BufferedImage mapBackgroundImage(int mapIndex) {
        int mapWidth = mapController.getCurrentMap().getTileSize() * maps.get(mapIndex).getMapData()[0].length;
        int mapHeight = mapController.getCurrentMap().getTileSize() * maps.get(mapIndex).getMapData().length;

        // Get the currentMapIndex from the MapController
        int currentMapIndex = mapController.getCurrentMapIndex();

        // Reset the backgroundImage when switching maps
        if (backgroundImage == null || this.currentMapIndex != currentMapIndex) {
            backgroundImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = backgroundImage.createGraphics();


            g.fillRect(0, 0, mapWidth, mapHeight);


            //TODO: Implement Background image
            try {
                BufferedImage mountainImage = ImageIO.read(new File("res/screens/startScreen/backgroundImage.png"));
                int scaledWidth = (int) (mountainImage.getWidth() * 2.1);
                int scaledHeight = (int) (mountainImage.getHeight() * 2.1);
                Image scaledMountainImage = mountainImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
                int imageX = 0;
                int imageY = mapHeight - scaledHeight;
                int endOfBackgroundImageY = mapHeight - scaledHeight;

                GradientPaint gradientPaintAbove = new GradientPaint(0, 0, Color.decode("#2D434F"), 0, endOfBackgroundImageY, Color.decode("#2D434F"));
                GradientPaint gradientPaintBelow = new GradientPaint(0, endOfBackgroundImageY, Color.decode("#033750"), 0, mapHeight, Color.decode("#033750"));

                g.setPaint(gradientPaintAbove);
                g.fillRect(0, 0, mapWidth, endOfBackgroundImageY);

                g.setPaint(gradientPaintBelow);
                g.fillRect(0, endOfBackgroundImageY, mapWidth, mapHeight);
                while (imageX < mapWidth) {
                    g.drawImage(scaledMountainImage, imageX, imageY, null);
                    imageX += scaledWidth;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            g.dispose();
            this.currentMapIndex = currentMapIndex;
        }

        return backgroundImage;
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

    public BufferedImage mapBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }


}