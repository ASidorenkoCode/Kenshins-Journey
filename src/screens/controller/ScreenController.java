package screens.controller;

import entities.logic.Player;
import game.logic.Highscore;
import items.controller.ItemController;
import items.logic.Item;
import screens.ui.InterfaceGame;

import java.awt.*;

public class ScreenController {
    private InterfaceGame interfaceGame;

    public ScreenController(ItemController itemController) {
        this.interfaceGame = new InterfaceGame(itemController);
    }

    public void update(Highscore highscore, Player player, Item[] menu) {
        interfaceGame.update(highscore, player, menu);
    }

    public void draw(Graphics g) {
        interfaceGame.draw(g);
    }
}
