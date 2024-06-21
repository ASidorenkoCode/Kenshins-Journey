import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteConverter {
    public int[][] convert(String mapSpritesPath, String modifiedSpritesPath, int spriteSize) {
        try {
            File mapSpritesFile = new File(mapSpritesPath);
            File modifiedSpritesFile = new File(modifiedSpritesPath);

            BufferedImage mapSpritesImage = ImageIO.read(mapSpritesFile);
            BufferedImage modifiedSpritesImage = ImageIO.read(modifiedSpritesFile);

            int width = mapSpritesImage.getWidth();
            int height = mapSpritesImage.getHeight();

            int[][] rgbValues = new int[width][height];

            for (int spriteY = 0; spriteY < height; spriteY += spriteSize) {
                for (int spriteX = 0; spriteX < width; spriteX += spriteSize) {
                    int spriteIndex = (spriteX / spriteSize) + (spriteY / spriteSize);

                    for (int y = spriteY; y < spriteY + spriteSize && y < height; y++) {
                        for (int x = spriteX; x < spriteX + spriteSize && x < width; x++) {
                            if (x < modifiedSpritesImage.getWidth() && y < modifiedSpritesImage.getHeight()) {
                                int newRgb = modifiedSpritesImage.getRGB(x, y);

                                if (spriteX == 11 * spriteSize && spriteY == 0) {
                                    int alpha = (newRgb >> 24) & 0xFF;
                                    int green = (newRgb >> 8) & 0xFF;
                                    int blue = newRgb & 0xFF;
                                    newRgb = (alpha << 24) | (11 << 16) | (green << 8) | blue;
                                }

                                rgbValues[x][y] = newRgb;
                            }
                        }
                    }
                }
            }

            return rgbValues;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}