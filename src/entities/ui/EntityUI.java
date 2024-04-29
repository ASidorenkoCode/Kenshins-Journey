package entities.ui;

import java.awt.image.BufferedImage;

abstract public class EntityUI {
    protected BufferedImage[][] animations;
    abstract void drawAttackBox();
    abstract void drawHitBox();
    abstract void loadAnimations();
    abstract void drawAnimations();
    abstract void drawHealthBar();
}
