import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ModifiedSpritesGenerator {
    private final ForbiddenValues forbiddenValues;

    public ModifiedSpritesGenerator(String forbiddenValuesPath) {
        forbiddenValues = new ForbiddenValues(forbiddenValuesPath);
    }

    public void generate(String mapSpritesPath, String modifiedSpritesPath, int tileSize) {
        try {
            File mapSpritesFile = new File(mapSpritesPath);
            File modifiedSpritesFile = new File(modifiedSpritesPath);

            BufferedImage mapSpritesImage = ImageIO.read(mapSpritesFile);

            if (!modifiedSpritesFile.exists() || mapSpritesImage.getWidth() != ImageIO.read(modifiedSpritesFile).getWidth() || mapSpritesImage.getHeight() != ImageIO.read(modifiedSpritesFile).getHeight()) {
                BufferedImage modifiedSpritesImage = new BufferedImage(mapSpritesImage.getWidth(), mapSpritesImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Random random = new Random();

                int red = 0;

                for (int y = 0; y < mapSpritesImage.getHeight(); y += tileSize) {
                    for (int x = 0; x < mapSpritesImage.getWidth(); x += tileSize) {
                        int green;
                        do {
                            green = random.nextInt(256);
                        } while (forbiddenValues.isGreenForbidden(green));

                        int blue;
                        do {
                            blue = random.nextInt(256);
                        } while (forbiddenValues.isBlueForbidden(blue));

                        int alpha = 255;

                        int newRgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                        for (int ty = 0; ty < tileSize; ty++) {
                            for (int tx = 0; tx < tileSize; tx++) {
                                if (x + tx < modifiedSpritesImage.getWidth() && y + ty < modifiedSpritesImage.getHeight()) {
                                    modifiedSpritesImage.setRGB(x + tx, y + ty, newRgb);
                                }
                            }
                        }

                        red = (red + 1) % 256;
                    }
                }

                ImageIO.write(modifiedSpritesImage, "png", modifiedSpritesFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}