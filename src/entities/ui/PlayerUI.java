package entities.ui;

import constants.Constants;
import entities.logic.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlayerUI extends EntityUI {
    Player player;
    boolean showHitBox;

    public PlayerUI(Player player, boolean showHitBox) {
        this.player = player;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 96;
        SPRITE_PX_HEIGHT = 96;
        ENTITY_SPRITE_PATH_RIGHT = "kenshin/kenshin_sprites_black_right.png";
        ENTITY_SPRITE_PATH_LEFT = "kenshin/kenshin_sprites_black_left.png";
        SPRITE_Y_DIMENSION = 15;
        SPRITE_X_DIMENSION = 17;
        loadAnimations();
        animationsDirection = animationsLeft;

    }


    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = player.getHitbox();
            g.drawRect((int) hitbox.x, (int) hitbox.y, (int) (hitbox.width * Constants.TILE_SCALE), (int) (hitbox.height * Constants.TILE_SCALE));
        }
    }

    private void updateAnimationTick() {
        if (aniIndex >= player.getCurrentPlayerAnimation().getAniSize()) {
            resetAnimationTick();
        }
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;

            //TODO: GetSpriteAmount, static for testing purposes
            if (aniIndex >= player.getCurrentPlayerAnimation().getAniSize()) {
                aniIndex = 0;
            }
        }

    }

    private void resetAnimationTick() {
        aniIndex = 0;
        aniTick = 0;
    }

    @Override
    public void drawAnimations(Graphics g) {

        updateAnimationTick();

        if (player.getRight() && !player.getLeft())
            animationsDirection = animationsRight;
        else if (player.getLeft() && !player.getRight()) animationsDirection = animationsLeft;

        g.drawImage(animationsDirection[player.getCurrentPlayerAnimation().getAniIndex()][aniIndex],
                (int) player.getX(),
                (int) player.getY(),
                (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
        drawHitBox(g);


    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }
}
