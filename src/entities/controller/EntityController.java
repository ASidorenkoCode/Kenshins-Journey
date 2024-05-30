package entities.controller;
import entities.logic.Boss;
import entities.logic.Entity;
import entities.ui.BossUI;
import gameObjects.controller.GameObjectController;
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
import java.awt.geom.Rectangle2D;
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

    public void update(ReloadGame reloadGame, MapController mapController, GameObjectController gameObjectController, LoadingScreen loadingScreen, InterfaceGame interfaceGame, DeathScreen deathScreen) {
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

        player.update(mapController.getCurrentMap(), currentBoss, kappas);
        if (!kappas.isEmpty()) handleKappas(mapController, interfaceGame);
        if(currentBoss != null) {
            if(currentBoss.getIsDead()) {
                gameObjectController.getFinish().setIsActive(true);
                return;
            }
            currentBoss.update(mapController.getMapOffset());
            handlePlayerAttacksBoss();
            handleBossAttacksPlayer();
        }
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

    public boolean isEntityHitboxNextToOtherEntity(Entity e1, Entity e2) {

        Rectangle2D.Float e1Hitbox = e1.getHitbox();
        Rectangle2D.Float e2Hitbox = e2.getHitbox();

        Rectangle2D.Float e1HitboxBuffered = new Rectangle2D.Float(e1Hitbox.x - 1, e1Hitbox.y - 1, e1Hitbox.width + 2, e1Hitbox.height + 2);
        Rectangle2D.Float e2HitboxBuffered = new Rectangle2D.Float(e2Hitbox.x - 1, e2Hitbox.y - 1, e2Hitbox.width + 2, e2Hitbox.height + 2);

        return e2HitboxBuffered.intersects(e1HitboxBuffered);
    }


    //functions for checking attacks between player and entities

    private void handlePlayerAttacksBoss() {
        if(!player.getHasAttacked()) {
            //only attack if attack hitbox is active
            if(!player.getAttackHitBoxIsActive()) return;

            Rectangle2D.Float bossHitbox = currentBoss.getHitbox();
            if(player.getIsFacingRight())
                if(!bossHitbox.intersects(player.getRightAttackHitBox())) return;
            else if(bossHitbox.intersects(player.getLeftAttackHitBox())) return;

            currentBoss.decreaseHealth(player.getCurrentDamagePerAttack());
            player.setHasAttacked(true);
        }
    }

    public void handleKappas(MapController mapController, InterfaceGame interfaceGame) {
        for (Kappa kap : kappas) {


            if (kap.isDead() && !kap.isScoreIncreased()) {
                if(!kap.isScoreIncreased()) {
                    interfaceGame.increaseScore(300);
                    kap.setScoreIncreased(true);
                }
                return;
            }

            if(isEntityHitboxNextToOtherEntity(kap, player)) {
                kap.updateAttackHitbox();
            }

            if (kap.isEntityInsideChecker(player) ) {
                kap.setMoveRight(kap.getX() < player.getX());
                if(!kap.hasAttacked()) handleKappaAttacksPlayer(kap);
            }


            handlePlayerAttacksKappa(kap);

            kap.update(mapController.getCurrentMap(), !isEntityHitboxNextToOtherEntity(kap, player));

        }
    }

    private void handlePlayerAttacksKappa(Kappa kappa) {
        if(!player.getHasAttacked()) {

            if(!player.getAttackHitBoxIsActive()) return;

            Rectangle2D.Float kappaHitbox = kappa.getHitbox();
            if(player.getIsFacingRight())
                if(!kappaHitbox.intersects(player.getRightAttackHitBox())) return;
                else if(kappaHitbox.intersects(player.getLeftAttackHitBox())) return;

            kappa.decreaseHealth(player.getCurrentDamagePerAttack());
            player.setHasAttacked(true);
        }
    }

    private void handleKappaAttacksPlayer(Kappa kappa) {
        if(kappa.getAttackHitbox().intersects(player.getHitbox())) {
            kappa.setIsAttacking(true);
            player.decreaseHealth(1);
            kappa.setHasAttacked(true);
        }
    }

    private void handleBossAttacksPlayer() {
        if(currentBoss.getIsUsingBigProjectile()) {
            Rectangle2D.Float bigProjectileHitbox = currentBoss.getProjectileHitbox();
            if(player.getHitbox().intersects(bigProjectileHitbox)) {
                player.decreaseHealth(1);
                currentBoss.resetProjectile();
            }
        } else {
            for(Rectangle2D.Float hitbox: currentBoss.getMiniProjectileHitboxes()) {
                if(player.getHitbox().intersects(hitbox)) {
                    currentBoss.resetAllMiniProjectiles();
                    player.decreaseHealth(1);
                    return;
                }
            }
        }
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

    public void drawEntities(Graphics g, int offset) {
        playerUI.drawAnimations(g, offset);
        for (KappaUI kappaUI : kappaUIS) {
            kappaUI.drawAnimations(g, offset);
        }
        if(currentBoss != null) bossUI.drawAnimations(g, offset);
    }

    public Player getPlayer() {
        return player;
    }

}

