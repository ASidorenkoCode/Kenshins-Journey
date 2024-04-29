package entities.ui;
import entities.logic.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerUI extends EntityUI {
    Player player;


    public PlayerUI(Player player, float tile_scale) {
        this.player = player;
        SPRITE_PX_WIDTH = 96;
        SPRITE_PX_HEIGHT = 64;
        ENTITY_SPRITE_PATH = "kenshin/kenshin_sprites_black.png";
        SPRITE_Y_DIMENSION = 13;
        SPRITE_X_DIMENSION = 8;
        TILE_SCALE = tile_scale;
        loadAnimations();
    }


    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox() {

    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;

            //TODO: GetSpriteAmount, static for testing purposes
           // if (aniIndex >= GetSpriteAmount(state)) {
            if(aniIndex >= 8) {
                aniIndex = 0;
            }
        }
    }

    @Override
    public void drawAnimations(Graphics g) {
        updateAnimationTick();

        //TODO: Make animations dynamic
        g.drawImage(animations[0][aniIndex],
                (int) player.getX(),
                (int) player.getY(),
                (int) (SPRITE_PX_WIDTH * TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * TILE_SCALE), null);
    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }
}
