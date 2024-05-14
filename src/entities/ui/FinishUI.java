package entities.ui;

import entities.logic.Finish;
import entities.logic.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class FinishUI extends EntityUI {
    Finish finish;
    boolean showHitBox;

    public FinishUI(Finish finish, boolean showHitBox) {
        this.finish = finish;
        this.showHitBox = showHitBox;
        SPRITE_PX_WIDTH = 60;
        SPRITE_PX_HEIGHT = 60;
        ENTITY_SPRITE_PATH = "flag_animation.png"; //when there is only one direction, use the default option right
        SPRITE_Y_DIMENSION = 1;
        SPRITE_X_DIMENSION = 5;
        loadAnimations();

    }


    @Override
    void drawAttackBox() {

    }

    @Override
    public void drawHitBox(Graphics g, int offset) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = finish.getHitbox();
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    private void resetAnimationTick() {
        aniIndex = 0;
        aniTick = 0;
    }

    @Override
    public void drawAnimations(Graphics g, int offset) {
        updateAnimationTick();
        g.drawImage(animations[0][aniIndex], //only one dimension and only one direction
                (int) finish.getX() - offset,
                (int) finish.getY(),
                SPRITE_PX_WIDTH * 2,
                SPRITE_PX_HEIGHT * 2, null);
        drawHitBox(g, offset);

    }

    @Override
    void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= 4) {
                aniIndex = 0;
            }
        }
    }
}
