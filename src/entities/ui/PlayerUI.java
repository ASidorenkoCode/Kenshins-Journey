package entities.ui;

import entities.animations.PlayerAnimations;
import entities.logic.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlayerUI extends EntityUI {
    Player player;
    boolean showHitBox;
    private PlayerAnimations currentAnimation;
    private long lastUpdate;


    public PlayerUI(Player player, boolean showHitBox) {
        this.player = player;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 89;
        SPRITE_PX_HEIGHT = 64;
        ENTITY_SPRITE_PATH = "kenshin/kenshin_sprites_red_right.png";
        ENTITY_SPRITE_PATH_LEFT = "kenshin/kenshin_sprites_red_left.png";
        SPRITE_Y_DIMENSION = 17;
        SPRITE_X_DIMENSION = 17;
        loadAnimations();
        currentAnimation = PlayerAnimations.IDLE;
        lastUpdate = System.nanoTime();
    }


    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox(Graphics g, int offsetX, int offsetY) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = player.getHitbox();
            g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);

            if (player.getAttackHitBoxIsActive()) {
                if (showLeftAnimations) {
                    hitbox = player.getLeftAttackHitBox();
                    g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
                } else {
                    hitbox = player.getRightAttackHitBox();
                    g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
                }
            }
        }
    }

    @Override
    void updateAnimationTick() {
        long now = System.nanoTime();
        float elapsedTime = (now - lastUpdate) / 1_000_000_000f; // convert to seconds
        lastUpdate = now;

        int tickProgress = (int) (elapsedTime / 0.01f);
        aniTick += tickProgress;

        setAnimation();

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= PlayerAnimations.DEATH.getAniSize() - 1) {
                player.setDeathAnimationFinished(true);
            }

            if (aniIndex >= currentAnimation.getAniSize()) {
                player.setAttack(false);
                player.setIsDashing(false);
                player.setHasAttacked(false);
                aniIndex = 0;
            }
        }
    }


    //TODO: setAnimation() into Player, not sure if part of ui or not
    private void setAnimation() {
        PlayerAnimations lastAnimation = currentAnimation;
        //Set animation

        if (player.getIsDashing()) currentAnimation = PlayerAnimations.DASH;
        else if (player.getIsResting()) currentAnimation = PlayerAnimations.RESTING;
        else if (player.getInAir() && player.getAttack()) {
            if (player.getAirMovement() < 0) currentAnimation = PlayerAnimations.JUMP_SLASH;
            else currentAnimation = PlayerAnimations.FALL_SLASH;
            player.setAttackHitBoxIsActive(true);
        } else if (player.getInAir() && !player.getAttack()) {
            if (player.getAirMovement() < 0) currentAnimation = PlayerAnimations.JUMP;
            else currentAnimation = PlayerAnimations.FALL;
        } else if (player.getAttack()) {
            if ((player.getLeft() && !player.getRight()) || (!player.getLeft() && player.getRight()))
                currentAnimation = PlayerAnimations.RUN_SLASH;
            else currentAnimation = PlayerAnimations.IDLE_SLASH;

            player.setAttackHitBoxIsActive((aniIndex == 0) || (aniIndex == 1) || (aniIndex == 4) || (aniIndex == 5));
        } else if ((player.getLeft() && !player.getRight()) || (!player.getLeft() && player.getRight()))
            currentAnimation = PlayerAnimations.RUN;
        else currentAnimation = PlayerAnimations.IDLE;

        if (player.isDead() && !player.getInAir()) {
            currentAnimation = PlayerAnimations.DEATH;
            aniSpeed = 3;
        } else if (player.isDead() && player.getInAir()) {
            currentAnimation = PlayerAnimations.DEATH;
            aniSpeed = 1;
        }

        //reset index
        if (currentAnimation != lastAnimation) {
            if (!(lastAnimation == PlayerAnimations.JUMP_SLASH || lastAnimation == PlayerAnimations.FALL_SLASH ||
                    lastAnimation == PlayerAnimations.IDLE_SLASH || lastAnimation == PlayerAnimations.RUN_SLASH)) {
                aniIndex = 0;
            }

            aniSpeed = currentAnimation.getAniSpeed();
        }
    }

    private boolean isAttackAnimation(PlayerAnimations animation) {
        return animation == PlayerAnimations.IDLE_SLASH || animation == PlayerAnimations.RUN_SLASH ||
                animation == PlayerAnimations.JUMP_SLASH || animation == PlayerAnimations.FALL_SLASH;
    }

    private void resetAnimationTick() {
        aniIndex = 0;
        aniTick = 0;
    }

    @Override
    public void drawAnimations(Graphics g, int offsetX, int offsetY) {

        updateAnimationTick();

        if (player.getRight() && !player.getLeft())
            showLeftAnimations = false;
        else if (player.getLeft() && !player.getRight()) showLeftAnimations = true;

        g.drawImage(animations[currentAnimation.getAniIndex() + (showLeftAnimations ? SPRITE_Y_DIMENSION : 0)][aniIndex],
                (int) player.getX() - offsetX,
                (int) player.getY() - offsetY,
                (int) (SPRITE_PX_WIDTH * 2),
                (int) (SPRITE_PX_HEIGHT * 2), null);
        drawHitBox(g, offsetX, offsetY);
    }

    public int getCurrentAniIndex() {
        return aniIndex;
    }
}
