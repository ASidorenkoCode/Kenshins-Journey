package entities.controller;

import entities.logic.BigOrc;
import entities.logic.Finish;
import entities.logic.Player;
import entities.ui.BigOrcUI;
import entities.ui.FinishUI;
import entities.ui.PlayerUI;
import maps.controller.MapController;
import screens.LoadingScreen;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    private FinishUI finishUI;
    private Finish finish;

    private BigOrc bigOrc;
    private BigOrcUI bigOrcUI;

    public EntityController(boolean showHitBox, Point PlayerPoint, Point FinishPoint, Point BigOrcPoint, Point BigOrcRoutePoint) {
        player = new Player(PlayerPoint.x, PlayerPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
        finish = new Finish(FinishPoint.x, FinishPoint.y);
        finishUI = new FinishUI(finish, showHitBox);
        bigOrc = new BigOrc(BigOrcPoint.x, BigOrcPoint.y, BigOrcPoint.x, BigOrcRoutePoint.x, 1);
        bigOrcUI = new BigOrcUI(bigOrc, showHitBox);
    }

    public void update(MapController mapController, LoadingScreen loadingScreen) {
        if (finish.checkIfPlayerIsInFinish(player)) {
            loadingScreen.displayLoadingScreen();
            mapController.loadNextMap();
            player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
            finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
            bigOrc.updateSpawnPoint(mapController.getCurrentBigOrcSpawn().x, mapController.getCurrentBigOrcSpawn().y);
        }
        player.update(mapController.getCurrentMap());
        bigOrc.update(mapController.getCurrentMap());
    }

    public void handleUserInputKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.jump();
                break;
            case KeyEvent.VK_SHIFT:
                player.attack();
        }
    }

    public void handleUserInputKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
        }
    }

    public void drawEntities(Graphics g, int offset) {
        playerUI.drawAnimations(g, offset);
        finishUI.drawAnimations(g, offset);
        finishUI.drawHitBox(g, offset);
        bigOrcUI.drawAnimations(g, offset);
    }

    public Player getPlayer() {
        return player;
    }
}
