public class Main {
    public static void main(String[] args) {
        ModifiedSpritesGenerator generator = new ModifiedSpritesGenerator("MapToRGBCreator/res/forbidden_values.txt");
        generator.generate("MapToRGBCreator/res/mapsprites.png", "MapToRGBCreator/res/modified_sprites.png", 32);

        SpriteConverter spriteConverter = new SpriteConverter();

        int[][] rgbValues = spriteConverter.convert("MapToRGBCreator/res/mapsprites.png", "MapToRGBCreator/res/modified_sprites.png", 32);

        MapSprite mapSprite = new MapSprite("MapToRGBCreator/res/mapsprites.png", 32, rgbValues);
        MapSprite rpgSprite = new MapSprite("MapToRGBCreator/res/modified_sprites.png", 32, rgbValues);

        MapSpritePainter mapSpritePainter = new MapSpritePainter(mapSprite, rpgSprite);
        mapSpritePainter.paintAndSave("MapToRGBCreator/res/mapsprites-export.png", "MapToRGBCreator/res/output.png", 32);
    }
}