import game.controller.GameController;

import javax.swing.*;
import java.io.IOException;

public class GameStart {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new GameController(Boolean.parseBoolean(args[0]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
