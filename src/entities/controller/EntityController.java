package entities.controller;

import boss.controller.BossController;
import gameObjects.controller.GameObjectController;
import gameObjects.logic.Finish;
import entities.logic.Kappa;
import entities.logic.Player;
import entities.ui.KappaUI;
import entities.ui.PlayerUI;
import game.controller.ReloadGame;
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
    private List<Kappa> kappas;
    private List<KappaUI> kappaUIS;
    private boolean showHitBox;

    public EntityController(MapController mapController, boolean showHitBox, Point playerSpawnPoint) {
        player = new Player(playerSpawnPoint.x, playerSpawnPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
        initKappas(mapController, showHitBox);
        this.showHitBox = showHitBox;
    }

    public void update(ReloadGame reloadGame, MapController mapController, GameObjectController gameObjectController, BossController bossController, LoadingScreen loadingScreen, InterfaceGame interfaceGame, DeathScreen deathScreen) {


        if (gameObjectController.checkIfPlayerIsInFinish(player) && !player.isDead()) {
            reloadGame.loadNewMap();
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
                gameObjectController.updatePoints(mapController);
                player.resetHealth();
                player.setDeathAnimationFinished(false);
                interfaceGame.setScore(5000);
                interfaceGame.setTotalHearts(player.getTotalHearts());
                deathScreen.setDisplayDeathScreenOnlyOnce(false);
            }
        }

        player.update(mapController.getCurrentMap(), bossController.getBoss());
        if (kappas.size() > 0) handleKappa(mapController, interfaceGame);
    }

    public void handleKappa(MapController mapController, InterfaceGame interfaceGame) {
        for (Kappa kap : kappas) {
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

    public void initKappas(MapController mapController, boolean showHitBox) {
        kappas = new ArrayList<>();
        kappaUIS = new ArrayList<>();
        for(Point p: mapController.getCurrentKappaSpawns()) {
            Kappa kappa = new Kappa(p.x, p.y, 0.6f);
            KappaUI kappaUI = new KappaUI(kappa, showHitBox);
            kappa.resetHealth();
            kappas.add(kappa);
            kappaUIS.add(kappaUI);
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
            case KeyEvent.VK_R:
                player.setIsRestingIfNotInAir(true);
                break;
            case KeyEvent.VK_S:
                player.setIsDashing(true);
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
            case KeyEvent.VK_R:
                player.setIsRestingIfNotInAir(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
        }
    }

    public void drawEntities(Graphics g, int offsetX, int offsetY) {
        playerUI.drawAnimations(g, offsetX, offsetY);
        for (KappaUI kappaUI : kappaUIS) {
            kappaUI.drawAnimations(g, offsetX, offsetY);
        }
    }

    public Player getPlayer() {
        return player;
    }
}

