package entities.ui;
import entities.logic.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PlayerUI extends EntityUI {
    Player player;


    public PlayerUI(Player player, float tile_scale) {
        this.player = player;
        SPRITE_PX_WIDTH = 96;
        SPRITE_PX_HEIGHT = 96;
        ENTITY_SPRITE_PATH_RIGHT = "kenshin/kenshin_sprites_black_right.png";
        ENTITY_SPRITE_PATH_LEFT = "kenshin/kenshin_sprites_black_left.png";
        SPRITE_Y_DIMENSION = 15;
        SPRITE_X_DIMENSION = 17;
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
            if(aniIndex >= player.currentPlayerAnimation().getAniSize()) {
                aniIndex = 0;
            }
        }
    }

    @Override
    public void drawAnimations(Graphics g) {
        updateAnimationTick();


        BufferedImage[][] animations;
        if(player.getRight())
           animations = animationsRight;
        else animations = animationsLeft;


        g.drawImage(animations[player.currentPlayerAnimation().getAniIndex()][aniIndex],
                (int) player.getX(),
                (int) player.getY(),
                (int) (SPRITE_PX_WIDTH * TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * TILE_SCALE), null);
    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }

    public void handleUserInput(KeyEvent e) {
        player.setLeft(true);
    }
}
