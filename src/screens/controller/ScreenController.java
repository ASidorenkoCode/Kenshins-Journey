package screens.controller;

import entities.logic.Player;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import items.controller.ItemController;
import items.logic.Item;
import maps.controller.MapController;
import screens.ui.DeathScreen;
import screens.ui.InterfaceGame;
import screens.ui.LoadingScreen;

import javax.swing.*;
import java.awt.*;

public class ScreenController {
    private InterfaceGame interfaceGame;

    private DeathScreen deathScreen;
    private LoadingScreen loadingScreen;

    public ScreenController(ItemController itemController, JFrame frame) {
        this.interfaceGame = new InterfaceGame(itemController);
        this.deathScreen = new DeathScreen(frame);
        this.loadingScreen = new LoadingScreen(frame);
    }

    public void update(Highscore highscore, Player player, Item[] menu) {
        interfaceGame.update(highscore, player, menu);
        loadingScreen.update(highscore);
        deathScreen.update(highscore);
    }

    public void displayDeathScreenIfPlayerIsDead(Player player, Highscore highscore, MapController mapController, GameObjectController gameObjectController) {
        if (player.isDead() && player.getDeathAnimationFinished()) {

            if (!deathScreen.isPlayerContinuesGame() && !deathScreen.isDisplayDeathScreenOnlyOnce()) {
                deathScreen.displayDeathScreen();
            }
            if (deathScreen.isPlayerContinuesGame() && deathScreen.isDisplayDeathScreenOnlyOnce()) {
                //TODO: no game logic in screen controller
                loadingScreen.displayLoadingScreen();
                player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
                gameObjectController.updatePoints(mapController);
                player.resetHealth();
                player.resetDeath();
                deathScreen.setDisplayDeathScreenOnlyOnce(false);
                highscore.decreaseHighscoreForDeath();
                highscore.increaseDeathCounter();
            }
        }
    }

    public void draw(Graphics g) {
        interfaceGame.draw(g);
    }

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    public void displayLoadingScreen() {
        loadingScreen.displayLoadingScreen();
    }
}
