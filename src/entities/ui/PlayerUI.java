package entities.ui;
import entities.logic.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerUI extends EntityUI {
    Player player;


    PlayerUI(Player player) {
        this.player = player;
        SPRITE_PX_WIDTH = 64;
        SPRITE_PX_HEIGHT = 32;
        ENTITY_SPRITE_PATH = "kenshin_sprite.png";
        SPRITE_Y_DIMENSION = 8;
        SPRITE_X_DIMENSION = 8;
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

            //TODO: GetSpriteAmount
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
            }
        }
    }

    @Override
    void drawAnimations(Graphics g) {
        updateAnimationTick();

        //TODO: Make animations dynamic
        g.drawImage(animations[0][aniIndex],
                (int) player.getX(),
                (int) player.getY(),
                player.getWidth(),
                player.getHeight(), null);
    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }
}
