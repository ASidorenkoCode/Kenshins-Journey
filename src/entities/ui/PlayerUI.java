package entities.ui;

import constants.Constants;
import entities.animations.PlayerAnimations;
import entities.logic.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlayerUI extends EntityUI {
    Player player;
    boolean showHitBox;
    private PlayerAnimations currentAnimation;
    private PlayerAnimations lastAnimation;

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
        currentAnimation = PlayerAnimations.IDLE;

    }


    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g, int offset) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = player.getHitbox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
            hitbox = player.getRightAttackHitBox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
            hitbox = player.getLeftAttackHitBox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    private void updateAnimationTick() {
        setAnimation();
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= currentAnimation.getAniSize()) {
                player.setAttack(false);
                aniIndex = 0;
            }
        }

    }

    private void setAnimation() {
        PlayerAnimations lastAnimation = currentAnimation;
        //Set animation
        if (player.getInAir()) {
            if(player.getAirMovement() < 0) currentAnimation = PlayerAnimations.JUMP;
            else currentAnimation = PlayerAnimations.FALL;
        } else if(player.getAttack()) {
            if((player.getLeft() && !player.getRight()) || (!player.getLeft() && player.getRight())) currentAnimation = PlayerAnimations.RUN_SLASH;
            else currentAnimation = PlayerAnimations.IDLE_SLASH;
        } else if((player.getLeft() && !player.getRight()) || (!player.getLeft() && player.getRight())) currentAnimation = PlayerAnimations.RUN;
        else currentAnimation = PlayerAnimations.IDLE;

        //reset index
        if(currentAnimation != lastAnimation) {
            if(!(lastAnimation == PlayerAnimations.IDLE_SLASH || lastAnimation == PlayerAnimations.RUN_SLASH)) aniIndex = 0;
        }
    }

    private void resetAnimationTick() {
        aniIndex = 0;
        aniTick = 0;
    }

    @Override
    public void drawAnimations(Graphics g, int offset) {

        updateAnimationTick();

        if (player.getRight() && !player.getLeft())
            animationsDirection = animationsRight;
        else if (player.getLeft() && !player.getRight()) animationsDirection = animationsLeft;

        g.drawImage(animationsDirection[currentAnimation.getAniIndex()][aniIndex],
                (int) player.getX() - offset,
                (int) player.getY(),
                (int) (SPRITE_PX_WIDTH * Constants.TILE_SCALE),
                (int) (SPRITE_PX_HEIGHT * Constants.TILE_SCALE), null);
        drawHitBox(g, offset);


    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }
}
