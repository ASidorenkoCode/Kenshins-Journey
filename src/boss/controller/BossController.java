package boss.controller;

import boss.logic.Boss;
import boss.ui.BossUI;
import entities.logic.Player;

import java.awt.*;
import java.util.ArrayList;

public class BossController {
    private Boss currentBoss;

    private BossUI bossUI;

    public BossController(boolean showHitBox) {
        currentBoss = new Boss(1200,580);
        bossUI = new BossUI(currentBoss, showHitBox);
    }

    public void update(Player player) {
        //TODO: check if boss should be handled by map
        currentBoss.update(player);
    }

    public void draw(Graphics g, int offset) {
        bossUI.draw(g, offset);
    }
}
