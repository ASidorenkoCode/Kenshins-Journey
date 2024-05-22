package gameObjects.controller;

import entities.logic.Player;
import gameObjects.logic.Finish;
import gameObjects.ui.GameObjectUI;
import maps.controller.MapController;

import java.awt.*;

public class GameObjectController {


    //ui
    private GameObjectUI gameObjectUI;

    //objects
    private Finish finish;

    public GameObjectController(MapController mapController, boolean showHitBox) {
        Point finishPoint = mapController.getCurrentFinishSpawn();
        boolean finishIsActive = mapController.getCurrentBossSpawn() == null;
        finish = new Finish(finishPoint.x, finishPoint.y, finishIsActive);
        gameObjectUI = new GameObjectUI(finish, showHitBox);
    }

    public boolean checkIfPlayerIsInFinish(Player player) {
        return finish.checkIfPlayerIsInFinish(player);
    }

    public void updatePoints(MapController mapController) {
        Point finishPoint = mapController.getCurrentFinishSpawn();
        boolean finishIsActive = mapController.getCurrentBossSpawn() == null;
        finish.updateFinishPoint(finishPoint.x, finishPoint.y, finishIsActive);
    }

    public void drawObjects(Graphics g, int offset) {
        gameObjectUI.drawAnimations(g, offset);
    }

    public Finish getFinish() {
        return finish;
    }
}
