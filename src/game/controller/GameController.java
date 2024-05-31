package game.controller;

import boss.controller.BossController;
import entities.controller.EntityController;
import entities.logic.Player;
import game.UI.GameView;
import game.logic.GameEngine;
import gameObjects.controller.GameObjectController;
import gameObjects.logic.Finish;
import items.controller.ItemController;
import maps.controller.MapController;
import screens.DeathScreen;
import screens.InterfaceGame;
import screens.LoadingScreen;
import screens.StartScreen;

public class GameController implements ReloadGame {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private GameObjectController gameObjectController;
    private MapController mapController;
    private LoadingScreen loadingScreen;
    private InterfaceGame interfaceGame;
    private ItemController itemController;
    private DeathScreen deathScreen;

    private boolean showHitbox;

    public GameController(boolean showHitBox) {
        mapController = new MapController(null);
        entityController = new EntityController(mapController, showHitBox);
        mapController.setEntityController(entityController);
        itemController = new ItemController(mapController, showHitBox);
        gameEngine = new GameEngine( this);
        gameObjectController = new GameObjectController(mapController, showHitBox);
        gameView = new GameView(this, entityController, mapController, itemController, gameObjectController);
        this.interfaceGame = new InterfaceGame(entityController.getPlayer(), itemController);
        this.deathScreen = new DeathScreen(gameView.getFrame());
        this.loadingScreen = new LoadingScreen(gameView.getFrame());
        gameView.gameWindow();
        gameEngine.startGameLoop();
        this.showHitbox = showHitBox;
    }



    public void showFPS_UPS() {
        gameView.showFPS_UPS(gameEngine.getFrames(), gameEngine.getUpdates());
    }

    public void callRepaint() {
        gameView.repaint();
    }

    public void update() {
        entityController.update(this, mapController, gameObjectController, bossController, loadingScreen, interfaceGame, deathScreen);
        itemController.update(entityController);
    }

    public InterfaceGame getInterfaceGame() {
        return interfaceGame;
    }

    public DeathScreen getDeathScreen() {
        return deathScreen;
    }

    @Override
    public void loadNewMap() {
        Player player = entityController.getPlayer();
        player.setTotalHearts(player.getTotalHearts() + 1);  // AMOUNT OF hearts collected
        interfaceGame.setTotalHearts(player.getTotalHearts());
        loadingScreen.displayLoadingScreen();
        loadingScreen.updateScore(interfaceGame.getScore());
        deathScreen.updateScore(interfaceGame.getScore());
        mapController.loadNextMap();
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        itemController.initItems(mapController);
    }
}
