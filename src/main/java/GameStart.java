import game.controller.GameController;

import javax.swing.*;

public class GameStart {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new GameController(Boolean.parseBoolean(args[0])));
    }
}
