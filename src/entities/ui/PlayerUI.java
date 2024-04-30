package entities.ui;
import entities.logic.Player;
import java.awt.*;

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
        animationsDirection = animationsLeft;

    }


    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g) {
        Rectangle hitbox = player.getHitbox();
        g.drawRect(hitbox.x,hitbox.y,(int) (hitbox.width * TILE_SCALE),(int) (hitbox.height * TILE_SCALE));
    }

    private void updateAnimationTick() {
        if(player.getLastPlayerAnimation() != player.getCurrentPlayerAnimation()) {
            aniIndex = 0;
            aniTick = 0;
            player.updateAnimation(player.getCurrentPlayerAnimation());
        }
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;

            //TODO: GetSpriteAmount, static for testing purposes
            if(aniIndex >= player.getCurrentPlayerAnimation().getAniSize()) {
                aniIndex = 0;
            }
        }

    }

    @Override
    public void drawAnimations(Graphics g) {

        updateAnimationTick();

        if(player.getRight() && !player.getLeft())
           animationsDirection = animationsRight;
        else if (player.getLeft() && !player.getRight()) animationsDirection = animationsLeft;

        g.drawImage(animationsDirection[player.getCurrentPlayerAnimation().getAniIndex()][aniIndex],
                (int) player.getX(),
                (int) player.getY(),
                (int) (SPRITE_PX_WIDTH * TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * TILE_SCALE), null);
        drawHitBox(g);


    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }
}
