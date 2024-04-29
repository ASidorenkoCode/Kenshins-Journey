package entities.ui;
import entities.logic.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerUI extends EntityUI {
    Player player;


    public final static int SPRITE_PX_WIDTH = 64;
    public final static int SPRITE_PX_HEIGHT = 32;
    public final static String PlAYER_SPRITE_PATH = "";

    PlayerUI(Player player) {
        this.player = player;
    }


    @Override
    void drawAttackBox() {

    }

    @Override
    void drawHitBox() {

    }

    @Override
    void loadAnimations() {
        //TODO: Implement GetSpriteAtlas
        BufferedImage img = LoadSave.GetSpriteAtlas(PlAYER_SPRITE_PATH);
        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(
                        i * SPRITE_PX_WIDTH,
                        j * SPRITE_PX_HEIGHT,
                        SPRITE_PX_WIDTH,
                        SPRITE_PX_HEIGHT);

    }


    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;

            //TODO: GetSpriteAmount
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
            }
        }
    }

    @Override
    void drawAnimations(Graphics g) {
        updateAnimationTick();

        //TODO: Make animations dynamic
        g.drawImage(animations[0][aniIndex],
                (int) player.getX(),
                (int) player.getY(),
                player.getWidth(),
                player.getHeight(), null);
    }


    @Override
    void drawHealthBar() {
        //TODO: Implement
    }
}
