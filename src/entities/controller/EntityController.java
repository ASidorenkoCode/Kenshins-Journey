package entities.controller;
import entities.logic.Boss;
import entities.ui.BossUI;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import entities.logic.Kappa;
import entities.logic.Player;
import entities.ui.KappaUI;
import entities.ui.PlayerUI;
import maps.controller.MapController;
import screens.ui.DeathScreen;
import screens.ui.LoadingScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;
    private ArrayList<Kappa> kappas;
    private ArrayList<KappaUI> kappaUIS;
    private Boss currentBoss;
    private BossUI bossUI;

    public EntityController(MapController mapController, boolean showHitBox) {
        initOrUpdatePlayer(mapController, showHitBox);
        initKappas(mapController, showHitBox);
        initBoss(mapController, showHitBox);
    }

    public void update(MapController mapController, GameObjectController gameObjectController, Highscore highscore) {
        player.update(mapController.getCurrentMap(), currentBoss, kappas, highscore);
        for (Kappa kap : kappas) kap.update(mapController.getCurrentMap(), player, highscore);

        if(currentBoss != null) currentBoss.update(mapController.getMapOffsetX(), player, highscore, gameObjectController.getFinish());
    }



    //init functions for init instances and updating instances
    public void initOrUpdatePlayer(MapController mapController, boolean showHitBox) {
        Point playerSpawnPoint = mapController.getCurrentPlayerSpawn();
        if (player != null) {
            player.updateSpawnPoint(playerSpawnPoint.x, playerSpawnPoint.y);
        }
        player = new Player(playerSpawnPoint.x, playerSpawnPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
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

    public void initBoss(MapController mapController, boolean showHitBox) {
        Point bossSpawn = mapController.getCurrentBossSpawn();
        if(bossSpawn == null) {
            currentBoss = null;
            return;
        }
        currentBoss = new Boss(bossSpawn.x,bossSpawn.y);
        bossUI = new BossUI(currentBoss, showHitBox);

    }

    //handle user input
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


    //handle ui

    public void drawEntities(Graphics g, int offsetX, int offsetY) {
        playerUI.drawAnimations(g, offsetX, offsetY);
        for (KappaUI kappaUI : kappaUIS) {
            kappaUI.drawAnimations(g, offsetX, offsetY);
        }
        if(currentBoss != null) bossUI.drawAnimations(g, offsetX, offsetY);
    }

    public Player getPlayer() {
        return player;
    }

    public Boss getCurrentBoss() {
        return currentBoss;
    }
}

