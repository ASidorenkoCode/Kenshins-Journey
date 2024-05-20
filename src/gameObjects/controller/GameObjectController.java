package gameObjects.controller;

import entities.logic.Player;
import gameObjects.logic.Finish;
import gameObjects.ui.GameObjectUI;

import java.awt.*;

public class GameObjectController {


    //ui
    private GameObjectUI gameObjectUI;

    //objects
    private Finish finish;

    public GameObjectController(Point finishPoint, boolean showHitBox) {
        finish = new Finish(finishPoint.x, finishPoint.y);
        gameObjectUI = new GameObjectUI(finish, showHitBox);
    }

    public boolean checkIfPlayerIsInFinish(Player player) {
        return finish.checkIfPlayerIsInFinish(player);
    }

    public void updatePoints(Point newFinishSpawn) {
        finish.updateFinishPoint(newFinishSpawn.x, newFinishSpawn.y);
    }

    public void drawObjects(Graphics g, int offset) {
        gameObjectUI.drawAnimations(g, offset);
    }

    public Finish getFinish() {
        return finish;
    }
}
