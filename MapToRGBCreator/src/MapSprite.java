import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapSprite {
    BufferedImage[][] tiles;
    private int[][] rgbValues;

    public MapSprite(String imagePath, int tileSize, int[][] rgbValues) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            int numTilesWide = image.getWidth() / tileSize;
            int numTilesHigh = image.getHeight() / tileSize;

            tiles = new BufferedImage[numTilesWide][numTilesHigh];

            for (int y = 0; y < numTilesHigh; y++) {
                for (int x = 0; x < numTilesWide; x++) {
                    tiles[x][y] = image.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }

            this.rgbValues = rgbValues;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getTile(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            return tiles[x][y];
        } else {
            throw new IllegalArgumentException("Invalid tile indices: " + x + ", " + y);
        }
    }

    public int getRGB(int x, int y) {
        if (x >= 0 && x < rgbValues.length && y >= 0 && y < rgbValues[0].length) {
            return rgbValues[x][y];
        } else {
            throw new IllegalArgumentException("Invalid indices: " + x + ", " + y);
        }
    }
}