package game.controller;

import entities.controller.EntityController;
import entities.logic.Finish;
import entities.logic.Player;
import game.UI.GameView;
import game.logic.GameEngine;
import items.controller.ItemController;
import maps.controller.MapController;
import screens.DeathScreen;
import screens.InterfaceGame;
import screens.LoadingScreen;
import screens.OptionScreen;

public class GameController implements ReloadGame {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private MapController mapController;
    private LoadingScreen loadingScreen;
    private OptionScreen optionScreen;
    private InterfaceGame interfaceGame;
    private ItemController itemController;
    private DeathScreen deathScreen;

    private boolean showHitbox;

    public GameController(boolean showFPS_UPS, boolean showHitBox) {
        mapController = new MapController(null);
        entityController = new EntityController(mapController, showHitBox,
                mapController.getCurrentPlayerSpawn(),
                mapController.getCurrentFinishSpawn());
        mapController.setEntityController(entityController);
        itemController = new ItemController(mapController, showHitBox);
        gameEngine = new GameEngine(showFPS_UPS, this);
        gameView = new GameView(this, entityController, mapController, itemController);
        this.interfaceGame = new InterfaceGame(entityController.getPlayer());
        this.deathScreen = new DeathScreen(gameView.getFrame());
        this.loadingScreen = new LoadingScreen(gameView.getFrame());
        this.optionScreen = new OptionScreen(gameView, gameEngine);
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
        entityController.update(this, mapController, loadingScreen, interfaceGame, deathScreen);
        if (entityController.getKappaAmount() > 0) entityController.handleKappa(mapController, interfaceGame);
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
        player.updateSpawnPoint(mapController.getCurrentPlayerSpawn().x, mapController.getCurrentPlayerSpawn().y);
        Finish finish = entityController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y);
        entityController.initKappas(mapController, showHitbox);
        itemController.initItems(mapController);
    }
}
