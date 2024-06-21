import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapSpritePainter {
    private final MapSprite mapSprite;
    private final MapSprite rgbSprite;

    public MapSpritePainter(MapSprite mapSprite, MapSprite rgbSprite) {
        this.mapSprite = mapSprite;
        this.rgbSprite = rgbSprite;
    }

    public void paintAndSave(String inputPath, String outputPath, int tileSize) {
        try {
            BufferedImage inputImage = ImageIO.read(new File(inputPath));
            BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            int numTilesWide = inputImage.getWidth() / tileSize;
            int numTilesHigh = inputImage.getHeight() / tileSize;

            BufferedImage[][] inputTiles = new BufferedImage[numTilesWide][numTilesHigh];

            for (int y = 0; y < numTilesHigh; y++) {
                for (int x = 0; x < numTilesWide; x++) {
                    inputTiles[x][y] = inputImage.getSubimage(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }

            for (int y = 0; y < numTilesHigh; y++) {
                for (int x = 0; x < numTilesWide; x++) {
                    BufferedImage inputTile = inputTiles[x][y];
                    boolean matchFound = false;

                    for (int my = 0; my < mapSprite.tiles[0].length && !matchFound; my++) {
                        for (int mx = 0; mx < mapSprite.tiles.length && !matchFound; mx++) {
                            BufferedImage mapTile = mapSprite.getTile(mx, my);

                            if (areImagesEqual(inputTile, mapTile)) {
                                int argb = rgbSprite.getRGB(mx * 32, my * 32);

                                int alpha = (argb >> 24) & 0xFF;
                                int red = (argb >> 16) & 0xFF;
                                int green = (argb >> 8) & 0xFF;
                                int blue = argb & 0xFF;

                                for (int ty = 0; ty < tileSize; ty++) {
                                    for (int tx = 0; tx < tileSize; tx++) {
                                        int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                                        outputImage.setRGB(x * tileSize + tx, y * tileSize + ty, rgb);
                                    }
                                }

                                matchFound = true;
                            }
                        }
                    }

                    if (!matchFound && isTileTransparent(inputTile)) {
                        int argb = rgbSprite.getRGB(11 * 32, 0);

                        int alpha = (argb >> 24) & 0xFF;
                        int red = (argb >> 16) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int blue = argb & 0xFF;

                        for (int ty = 0; ty < tileSize; ty++) {
                            for (int tx = 0; tx < tileSize; tx++) {
                                int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                                outputImage.setRGB(x * tileSize + tx, y * tileSize + ty, rgb);
                            }
                        }
                    }
                }
            }

            ImageIO.write(outputImage, "png", new File(outputPath));

            BufferedImage shrunkImage = shrinkImage(outputImage, tileSize);
            ImageIO.write(shrunkImage, "png", new File("MapToRGBCreator/res/shrunk_output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean areColorsCloseEnough(int argb1, int argb2) {
        int red1 = (argb1 >> 16) & 0xFF;
        int green1 = (argb1 >> 8) & 0xFF;
        int blue1 = argb1 & 0xFF;

        int red2 = (argb2 >> 16) & 0xFF;
        int green2 = (argb2 >> 8) & 0xFF;
        int blue2 = argb2 & 0xFF;

        int colorTolerance = 5;

        return Math.abs(red1 - red2) <= colorTolerance &&
                Math.abs(green1 - green2) <= colorTolerance &&
                Math.abs(blue1 - blue2) <= colorTolerance;
    }

    private boolean areImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int argb1 = img1.getRGB(x, y);
                int argb2 = img2.getRGB(x, y);

                if (argb1 != argb2) {
                    if (!areColorsCloseEnough(argb1, argb2)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean areTilesEqual(BufferedImage tile1, BufferedImage tile2) {
        if (tile1.getWidth() == tile2.getWidth() && tile1.getHeight() == tile2.getHeight()) {
            for (int y = 0; y < tile1.getHeight(); y++) {
                for (int x = 0; x < tile1.getWidth(); x++) {
                    int argb1 = tile1.getRGB(x, y);
                    int argb2 = tile2.getRGB(x, y);

                    if (argb1 != argb2) {
                        if (!areColorsCloseEnough(argb1, argb2)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private boolean isTileTransparent(BufferedImage tile) {
        for (int y = 0; y < tile.getHeight(); y++) {
            for (int x = 0; x < tile.getWidth(); x++) {
                int argb = tile.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;

                if (alpha != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public BufferedImage shrinkImage(BufferedImage outputImage, int tileSize) {
        int numTilesWide = outputImage.getWidth() / tileSize;
        int numTilesHigh = outputImage.getHeight() / tileSize;

        BufferedImage shrunkImage = new BufferedImage(numTilesWide, numTilesHigh, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < numTilesHigh; y++) {
            for (int x = 0; x < numTilesWide; x++) {
                int sumAlpha = 0;
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;

                for (int ty = 0; ty < tileSize; ty++) {
                    for (int tx = 0; tx < tileSize; tx++) {
                        int argb = outputImage.getRGB(x * tileSize + tx, y * tileSize + ty);

                        int alpha = (argb >> 24) & 0xFF;
                        int red = (argb >> 16) & 0xFF;
                        int green = (argb >> 8) & 0xFF;
                        int blue = argb & 0xFF;

                        sumAlpha += alpha;
                        sumRed += red;
                        sumGreen += green;
                        sumBlue += blue;
                    }
                }

                int avgAlpha = sumAlpha / (tileSize * tileSize);
                int avgRed = sumRed / (tileSize * tileSize);
                int avgGreen = sumGreen / (tileSize * tileSize);
                int avgBlue = sumBlue / (tileSize * tileSize);

                int avgRgb = (avgAlpha << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue;
                shrunkImage.setRGB(x, y, avgRgb);
            }
        }

        return shrunkImage;
    }
}