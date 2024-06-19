package entities.controller;

import entities.logic.Boss;
import entities.logic.Enemy;
import entities.logic.Player;
import entities.ui.BossUI;
import entities.ui.EnemyUI;
import entities.ui.PlayerUI;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import maps.controller.MapController;
import screens.ui.DeathScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class EntityController {

    private PlayerUI playerUI;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<EnemyUI> enemyUIS;
    private Boss currentBoss;
    private BossUI bossUI;

    public EntityController(MapController mapController, boolean showHitBox) throws IOException {
        initOrUpdatePlayer(mapController, showHitBox);
        initEnemies(mapController, showHitBox);
        initBoss(mapController, showHitBox);
    }

    public void update(MapController mapController, GameObjectController gameObjectController, Highscore highscore) {
        player.update(mapController.getCurrentMap(), currentBoss, enemies, highscore);
        for (Enemy kap : enemies) kap.update(mapController.getCurrentMap(), player, highscore);

        if(currentBoss != null) currentBoss.update(mapController.getMapOffsetX(), player, highscore, gameObjectController.getFinish());
    }



    //init functions for init instances and updating instances
    public void initOrUpdatePlayer(MapController mapController, boolean showHitBox) {
        Point playerSpawnPoint = mapController.getCurrentPlayerSpawn();
        if (player != null) {
            player.updateSpawnPoint(playerSpawnPoint.x, playerSpawnPoint.y);
            return;
        }
        player = new Player(playerSpawnPoint.x, playerSpawnPoint.y);
        playerUI = new PlayerUI(player, showHitBox);
    }

    public void initEnemies(MapController mapController, boolean showHitBox) {
        enemies = new ArrayList<>();
        enemyUIS = new ArrayList<>();
        for (Point p : mapController.getCurrentEnemySpawns()) {
            Enemy enemy = new Enemy(p.x, p.y, 0.6f);
            EnemyUI enemyUI = new EnemyUI(enemy, showHitBox);
            enemy.resetHealth();
            enemies.add(enemy);
            enemyUIS.add(enemyUI);
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
            case KeyEvent.VK_R:
                player.setIsRestingIfNotInAir(true);
                break;
            case KeyEvent.VK_S:
                player.setIsDashing(true);
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
        for (EnemyUI enemyUI : enemyUIS) {
            enemyUI.drawAnimations(g, offsetX, offsetY);
        }
        if(currentBoss != null) bossUI.drawAnimations(g, offsetX, offsetY);
    }

    public Player getPlayer() {
        return player;
    }

    public Boss getCurrentBoss() {
        return currentBoss;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerUI getPlayerUI() {
        return playerUI;
    }
}

