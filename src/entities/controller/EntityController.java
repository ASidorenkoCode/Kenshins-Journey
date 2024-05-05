package entities.controller;

import entities.logic.kappa;
import entities.logic.Finish;
import entities.logic.Player;
import entities.ui.kappaUI;
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

    private List<kappa> kappas = new ArrayList<>();
    private List<kappaUI> kappaUIs = new ArrayList<>();

    public EntityController(MapController mapController, boolean showHitBox, Point playerSpawnPoint, Point finishPoint, Point kappaSpawnPoint, Point kappaRoutePoint) {
        player = new Player(playerSpawnPoint.x, playerSpawnPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
        finish = new Finish(finishPoint.x, finishPoint.y);
        finishUI = new FinishUI(finish, showHitBox);
        initKappas(mapController, showHitBox, kappaSpawnPoint, kappaRoutePoint);
    }

    public void update(MapController mapController, LoadingScreen loadingScreen, InterfaceGame interfaceGame) {
        if (finish.checkIfPlayerIsInFinish(player) && !player.isDead()) {
            player.setTotalHearts(player.getTotalHearts() + 1);  // AMOUNT OF hearts collected
            interfaceGame.setTotalHearts(player.getTotalHearts());
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
            interfaceGame.setTotalHearts(player.getTotalHearts());
        }
        player.update(mapController.getCurrentMap());
    }

    public void handleKappa(MapController mapController, InterfaceGame interfaceGame) {
        for (kappa orc : kappas) {
            orc.update(mapController.getCurrentMap());
            player.collisionWithEntity(orc, playerUI);
        }

        for (kappa kappa : kappas) {
            if (kappa.getHealth() == 0 && !kappa.isScoreIncreased()) {
                interfaceGame.increaseScore(300);
                kappa.setScoreIncreased(true);
            }
        }
    }

    public void initKappas(MapController mapController, boolean showHitBox, Point kappaSpawnPoint, Point kappaRoutePoint) {
        Point currentKappaSpawn = mapController.getCurrentKappaSpawn();
        if (currentKappaSpawn != null) {
            int orcCount = mapController.getKappaSpawnCount();
            if (kappas.size() < orcCount) {
                kappa kappa = new kappa(kappaSpawnPoint.x, kappaSpawnPoint.y, kappaSpawnPoint.x, kappaRoutePoint.x, 0.6f);
                kappaUI kappaUI = new kappaUI(kappa, showHitBox);
                kappa.resetHealth();
                kappas.add(kappa);
                kappaUIs.add(kappaUI);
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
        for (kappaUI kappaUI : kappaUIs) {
            kappaUI.drawAnimations(g, offset);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getKappaAmount() {
        return kappas.size();
    }
}

