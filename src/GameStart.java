import game.UI.GameView;
import game.logic.GameEngine;

import javax.swing.*;

public class GameStart {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameView(new GameEngine(Boolean.parseBoolean(args[0]))));
    }
}
