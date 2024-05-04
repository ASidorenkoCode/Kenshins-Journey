package entities.controller;

import entities.logic.BigOrc;
import entities.logic.Finish;
import entities.logic.Player;
import entities.ui.BigOrcUI;
import entities.ui.FinishUI;
import entities.ui.PlayerUI;
import maps.controller.MapController;
import screens.InterfaceGame;
import screens.LoadingScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;

    private FinishUI finishUI;
    private Finish finish;

    private List<BigOrc> bigOrcs = new ArrayList<>();
    private List<BigOrcUI> bigOrcUIs = new ArrayList<>();

    public EntityController(boolean showHitBox, Point PlayerPoint, Point FinishPoint, Point BigOrcPoint, Point BigOrcRoutePoint) {
        player = new Player(PlayerPoint.x, PlayerPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
        finish = new Finish(FinishPoint.x, FinishPoint.y);
        finishUI = new FinishUI(finish, showHitBox);
    }

    public void update(MapController mapController, LoadingScreen loadingScreen, InterfaceGame interfaceGame) {
        if (finish.checkIfPlayerIsInFinish(player) && !player.isDead()) {
            loadingScreen.displayLoadingScreen();
            mapController.loadNextMap();
            player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
            finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
        }

        if (interfaceGame.getScore() == 0) {
            player.setPlayerHealth(0);
        }

        if (player.isDead() && player.getDeathAnimationFinished()) {
            loadingScreen.displayLoadingScreen();
            player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
            finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
            player.resetHealth();
            player.setDeathAnimationFinished(false);
            interfaceGame.setScore(5000);
            interfaceGame.setTotalHearts(player.getPlayerHealth() / 2);
        }
        player.update(mapController.getCurrentMap());
    }

    public void handleBigOrc(MapController mapController, boolean showHitBox, Point BigOrcPoint, Point BigOrcRoutePoint, InterfaceGame interfaceGame) {
        Point currentBigOrcSpawn = mapController.getCurrentBigOrcSpawn();
        if (currentBigOrcSpawn != null) {
            int orcCount = mapController.getOrcSpawnCount();
            if (bigOrcs.size() < orcCount) {
                BigOrc bigOrc = new BigOrc(BigOrcPoint.x, BigOrcPoint.y, BigOrcPoint.x, BigOrcRoutePoint.x, 1);
                BigOrcUI bigOrcUI = new BigOrcUI(bigOrc, showHitBox);
                bigOrc.resetHealth();
                bigOrcs.add(bigOrc);
                bigOrcUIs.add(bigOrcUI);
                System.out.println(bigOrcs.size());
            }

            for (BigOrc orc : bigOrcs) {
                orc.update(mapController.getCurrentMap());
                player.collisionWithEntity(orc, playerUI);
            }
        }

            for (BigOrc bigOrc : bigOrcs) {
                if (bigOrc.getHealth() == 0 && !bigOrc.isScoreIncreased()) {
                    interfaceGame.increaseScore(300);
                    bigOrc.setScoreIncreased(true);
                }
            }
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
        for (BigOrcUI bigOrcUI : bigOrcUIs) {
            bigOrcUI.drawAnimations(g, offset);
        }
    }

    public Player getPlayer() {
        return player;
    }
}

