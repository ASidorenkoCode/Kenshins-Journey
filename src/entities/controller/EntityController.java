package entities.controller;

import entities.logic.Finish;
import entities.logic.Player;
import entities.ui.FinishUI;
import entities.ui.PlayerUI;
import maps.controller.MapController;
import screens.LoadingScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    private FinishUI finishUI;
    private Finish finish;

    public EntityController(boolean showHitBox, Point PlayerPoint, Point FinishPoint) {
        player = new Player(PlayerPoint.x, PlayerPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
        finish = new Finish(FinishPoint.x, FinishPoint.y);
        finishUI = new FinishUI(finish, showHitBox);
    }

    public void update(MapController mapController, LoadingScreen loadingScreen) {
        if (finish.checkIfPlayerIsInFinish(player)) {
            mapController.loadNextMap();
            loadingScreen.displayLoadingScreen(); // This will blur the window
            player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
            finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
        }
        player.update(mapController.getCurrentMap());
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
        finishUI.drawHitBox(g, offset);
    }

    public Player getPlayer() {
        return player;
    }
}
