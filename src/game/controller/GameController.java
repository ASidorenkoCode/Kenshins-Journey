package game.controller;

import entities.controller.EntityController;
import entities.logic.Player;
import game.UI.GameView;
import game.logic.GameEngine;
import game.logic.Highscore;
import gameObjects.controller.GameObjectController;
import gameObjects.logic.Finish;
import items.controller.ItemController;
import maps.controller.MapController;
import screens.controller.ScreenController;
import screens.ui.DeathScreen;
import screens.ui.LoadingScreen;

public class GameController {

    private GameEngine gameEngine;
    private GameView gameView;
    private EntityController entityController;
    private GameObjectController gameObjectController;
    private MapController mapController;
    private ScreenController screenController;
    private ItemController itemController;

    private Highscore highscore;

    private boolean showHitbox;

    public GameController(boolean showHitBox) {
        //controller
        this.highscore = Highscore.readHighscore();
        mapController = new MapController(null, highscore);
        entityController = new EntityController(mapController, showHitBox);
        mapController.setEntityController(entityController);
        itemController = new ItemController(mapController, showHitBox);
        gameEngine = new GameEngine( this);
        gameObjectController = new GameObjectController(mapController, showHitBox);
        gameView = new GameView(this, entityController, mapController, itemController, gameObjectController, null);
        screenController = new ScreenController(itemController, gameView.getFrame());
        gameView.setScreenController(screenController);


        //ui
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
        Player player = entityController.getPlayer();

        screenController.displayDeathScreenIfPlayerIsDead(player, highscore, mapController, gameObjectController);
        if (gameObjectController.checkIfPlayerIsInFinish(entityController.getPlayer()) && !entityController.getPlayer().getDeathAnimationFinished()) {
            loadNewMap();
            return;
        }
        entityController.update(mapController, gameObjectController, highscore);
        itemController.update(entityController);
        highscore.decreaseHighScoreAfterOneSecond();
        screenController.update(highscore, entityController.getPlayer(), itemController.getMenu());
    }

    public DeathScreen getDeathScreen() {
        //TODO: do not handle this here
        return screenController.getDeathScreen();
    }

    public void loadNewMap() {
        //highscore updates
        highscore.addCurrentHighscoreToList();
        highscore.writeHighscore();
        highscore.increaseHighscoreForItems(itemController.getMenu());

        Player player = entityController.getPlayer();
        player.setTotalHearts(player.getTotalHearts() + 1);// AMOUNT OF hearts collected
        screenController.displayLoadingScreen();
        mapController.loadCurrentMapIndex(highscore);
        Finish finish = gameObjectController.getFinish();
        finish.updateFinishPoint(mapController.getCurrentFinishSpawn().x, mapController.getCurrentFinishSpawn().y, mapController.getCurrentBossSpawn() == null);
        entityController.initKappas(mapController, showHitbox);
        entityController.initOrUpdatePlayer(mapController, showHitbox);
        itemController.initItems(mapController);
        itemController.deleteAllItemsFromMenu();


    }
}
