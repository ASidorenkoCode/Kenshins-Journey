package boss.ui;

import boss.logic.Boss;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BossUI {
    private Boss currentBoss;
    private boolean showHitBox;

    public BossUI(Boss currentBoss, boolean showHitBox) {
        this.currentBoss = currentBoss;
        this.showHitBox = showHitBox;
    }

    public void draw(Graphics g, int offset) {
        if(showHitBox && currentBoss != null) {
            drawProjectileHitbox(g, offset);
            drawHitbox(g, offset);
            drawMiniProjectileHitboxes(g,offset);
        }
    }

    public void drawHitbox(Graphics g, int offset) {
        Rectangle2D.Float hitbox = currentBoss.getHitbox();
        g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void drawProjectileHitbox(Graphics g, int offset) {
        Rectangle2D.Float hitbox = currentBoss.getProjectileHitbox();
        g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    public void drawMiniProjectileHitboxes(Graphics g, int offset) {
        for(Rectangle2D.Float hitbox: currentBoss.getMiniProjectileHitboxes()) {
            g.drawRect((int) hitbox.x - offset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }
}
