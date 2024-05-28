package gameObjects.ui;

import entities.ui.EntityUI;
import gameObjects.logic.Finish;
import gameObjects.logic.GameObject;
import spriteControl.SpriteManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GameObjectUI {

    private static final int ANI_SPEED = 20;
    private static final int ANI_LENGTH = 5;
    private final int SPRITE_PX_WIDTH = 60;
    private final int SPRITE_PX_HEIGHT = 60;
    private final String GAME_OBJECT_SPRITE_PATH = "flag_animation.png";
    private final int SPRITE_X_DIMENSION = 5;
    private final int SPRITE_Y_DIMENSION = 1;
    private int aniTick;
    private int aniIndex;
    private BufferedImage[][] animations;

    Finish finish;
    boolean showHitBox;

    public GameObjectUI(Finish finish, boolean showHitBox) {
        this.finish = finish;
        this.showHitBox = showHitBox;
        loadAnimations();
    }

    private void loadAnimations() {
        animations = new BufferedImage[SPRITE_Y_DIMENSION][SPRITE_X_DIMENSION];
        BufferedImage img = SpriteManager.GetSpriteAtlas(GAME_OBJECT_SPRITE_PATH);
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        0,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);
            }
    }
    private void drawHitBoxOfObjects(GameObject object, Graphics g, int offsetX, int offsetY) {
        if (showHitBox) {
            Rectangle2D.Float hitbox = object.getHitbox();
            g.drawRect((int) hitbox.x - offsetX, (int) hitbox.y - offsetY, (int) hitbox.width, (int) hitbox.height);
        }
    }

    private void drawObject(GameObject object, Graphics g, int offsetX, int offsetY)  {
        g.drawImage(animations[object.getObjectAnimation().getAniIndex()][aniIndex], //only one dimension and only one direction
                (int) object.getX() - offsetX,
                (int) object.getY() - offsetY,
                SPRITE_PX_WIDTH * 2,
                SPRITE_PX_HEIGHT * 2, null);
        drawHitBoxOfObjects(object, g, offsetX, offsetY);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= ANI_LENGTH) {
                aniIndex = 0;
            }
        }
    }

    public void drawAnimations(Graphics g, int offsetX, int offsetY) {
        if(!finish.getIsActive()) return;
        updateAnimationTick();
        drawObject(finish, g, offsetX, offsetY);
    }
}
