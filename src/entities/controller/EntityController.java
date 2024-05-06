package entities.controller;

import entities.logic.Finish;
import entities.logic.Kappa;
import entities.logic.Player;
import entities.ui.FinishUI;
import entities.ui.KappaUI;
import entities.ui.PlayerUI;
import maps.controller.MapController;
import screens.DeathScreen;
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

    private List<Kappa> Kappas = new ArrayList<>();
    private List<KappaUI> KappaUIS = new ArrayList<>();

    public EntityController(MapController mapController, boolean showHitBox, Point playerSpawnPoint, Point finishPoint, Point kappaSpawnPoint, Point kappaRoutePoint) {
        player = new Player(playerSpawnPoint.x, playerSpawnPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
        finish = new Finish(finishPoint.x, finishPoint.y);
        finishUI = new FinishUI(finish, showHitBox);
        initKappas(mapController, showHitBox, kappaSpawnPoint, kappaRoutePoint);
    }

    public void update(MapController mapController, LoadingScreen loadingScreen, InterfaceGame interfaceGame, DeathScreen deathScreen) {
        if (finish.checkIfPlayerIsInFinish(player) && !player.isDead()) {
            player.setTotalHearts(player.getTotalHearts() + 1);  // AMOUNT OF hearts collected
            interfaceGame.setTotalHearts(player.getTotalHearts());
            loadingScreen.displayLoadingScreen();
            loadingScreen.updateScore(interfaceGame.getScore());
            deathScreen.updateScore(interfaceGame.getScore());
            mapController.loadNextMap();
            player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
            finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
        }

        if (interfaceGame.getScore() == 0) {
            player.setPlayerHealth(0);
        }

        if (player.isDead() && player.getDeathAnimationFinished()) {
            if(deathScreen.getTotalScore() > interfaceGame.getScore()) deathScreen.updateScore(interfaceGame.getScore());
            if (!deathScreen.isPlayerContinuesGame() && !deathScreen.isDisplayDeathScreenOnlyOnce()) {
                deathScreen.displayDeathScreen();
            }
            if (deathScreen.isPlayerContinuesGame() && deathScreen.isDisplayDeathScreenOnlyOnce()) {
                loadingScreen.displayLoadingScreen();
                player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
                finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
                player.resetHealth();
                player.setDeathAnimationFinished(false);
                interfaceGame.setScore(5000);
                interfaceGame.setTotalHearts(player.getTotalHearts());
                deathScreen.setDisplayDeathScreenOnlyOnce(false);
            }
        }

        if (player.getAttackCooldown() > interfaceGame.getAttackCooldown())
            interfaceGame.setAttackCooldown(player.getAttackCooldown());
        if (interfaceGame.getAttackCooldown() == 0) player.setAttackCooldown(0);

        player.update(mapController.getCurrentMap());
    }

    public void handleKappa(MapController mapController, InterfaceGame interfaceGame) {
        for (Kappa kap : Kappas) {
            kap.update(mapController.getCurrentMap(), player);
            player.collisionWithEntity(kap, playerUI);

            if (kap.getHealth() == 0 && !kap.isScoreIncreased()) {
                interfaceGame.increaseScore(300);
                kap.setScoreIncreased(true);
            }

            if (kap.isPlayerNearChecker(player) && !kap.isAttacking() && !kap.hasAttacked() && !player.isDead() && !kap.isDead()) {
                kap.startAttacking(player);
            }
        }
    }

    public void initKappas(MapController mapController, boolean showHitBox, Point kappaSpawnPoint, Point kappaRoutePoint) {
        Point currentKappaSpawn = mapController.getCurrentKappaSpawn();
        if (currentKappaSpawn != null) {
            int kappaCount = mapController.getKappaSpawnCount();
            if (Kappas.size() < kappaCount) {
                Kappa kappa = new Kappa(kappaSpawnPoint.x, kappaSpawnPoint.y, kappaSpawnPoint.x, kappaRoutePoint.x, 0.6f);
                KappaUI kappaUI = new KappaUI(kappa, showHitBox);
                kappa.resetHealth();
                Kappas.add(kappa);
                KappaUIS.add(kappaUI);
            }
        }
    }

    public void handleUserInputKeyPressed(KeyEvent e, DeathScreen deathScreen) {
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
                break;
            case KeyEvent.VK_I:
                player.decreaseHealth(1);
                break;
            case KeyEvent.VK_ENTER:
                if (player.isDead()) deathScreen.removeDeathScreen();
                break;
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
        for (KappaUI kappaUI : KappaUIS) {
            kappaUI.drawAnimations(g, offset);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getKappaAmount() {
        return Kappas.size();
    }
}

