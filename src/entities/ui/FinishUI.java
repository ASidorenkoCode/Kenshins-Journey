package entities.ui;

import constants.Constants;
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
        //TODO: Animations of portal

    }


    @Override
    void drawHealthBar() {
        //TODO: Not needed, maybe delete for all entites
    }
}
