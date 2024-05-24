package boss.controller;

import boss.logic.Boss;
import boss.ui.BossUI;
import entities.logic.Player;
import gameObjects.logic.Finish;
import maps.controller.MapController;

import java.awt.*;

public class BossController {
    private Boss currentBoss;
    private BossUI bossUI;
    private boolean showHitBox;

    public BossController(MapController mapController, boolean showHitBox) {
        this.showHitBox = showHitBox;
        initBoss(mapController);
        bossUI = new BossUI(currentBoss, showHitBox);
    }

    public void initBoss(MapController mapController) {
        Point bossSpawn = mapController.getCurrentBossSpawn();
        if(bossSpawn == null) {
            currentBoss = null;
            return;
        }
        currentBoss = new Boss(bossSpawn.x,bossSpawn.y);

    }

    public void update(Player player, Finish finish, int offset) {
        //TODO: check if boss should be handled by map
        if(currentBoss == null) return;;
        currentBoss.update(player, finish, offset);
    }

    public void draw(Graphics g, int offset) {
        if(currentBoss == null) return;
        bossUI.draw(g, offset);
    }
}
