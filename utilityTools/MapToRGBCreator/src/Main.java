public class Main {
    public static void main(String[] args) {
        ModifiedSpritesGenerator generator = new ModifiedSpritesGenerator("utilityTools/MaptoRGBCreator/res/forbidden_values.txt");
        generator.generate("utilityTools/MaptoRGBCreator/res/mapsprites.png", "utilityTools/MaptoRGBCreator/res/modified_sprites.png", 32);

        SpriteConverter spriteConverter = new SpriteConverter();

        int[][] rgbValues = spriteConverter.convert("utilityTools/MaptoRGBCreator/res/mapsprites.png", "utilityTools/MaptoRGBCreator/res/modified_sprites.png", 32);

        MapSprite mapSprite = new MapSprite("utilityTools/MaptoRGBCreator/res/mapsprites.png", 32, rgbValues);
        MapSprite rpgSprite = new MapSprite("utilityTools/MaptoRGBCreator/res/modified_sprites.png", 32, rgbValues);

        MapSpritePainter mapSpritePainter = new MapSpritePainter(mapSprite, rpgSprite);
        mapSpritePainter.paintAndSave("utilityTools/MaptoRGBCreator/res/mapsprites-export.png", "utilityTools/MaptoRGBCreator/res/output.png", 32);
    }
}