package entities.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class EntityUI {
    protected BufferedImage[][] animations;
    protected int aniTick;

    protected int aniIndex;
    protected final static int ANI_SPEED = 25;
    abstract void drawAttackBox();
    abstract void drawHitBox();
    abstract void loadAnimations();
    abstract void drawAnimations(Graphics g);
    abstract void drawHealthBar();
}
