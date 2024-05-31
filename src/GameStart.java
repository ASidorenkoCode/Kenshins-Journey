import game.UI.GameView;
import game.controller.GameController;
import game.logic.GameEngine;

import javax.swing.*;

public class GameStart {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new GameController(Boolean.parseBoolean(args[0])));
    }
}
