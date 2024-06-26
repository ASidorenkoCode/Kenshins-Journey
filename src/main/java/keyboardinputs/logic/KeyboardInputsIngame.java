package keyboardinputs.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.controller.EntityController;
import game.UI.GameView;
import game.controller.GameController;
import game.controller.GameControls;
import game.controller.GameState;
import items.controller.ItemController;
import screens.controller.ScreenController;
import screens.ui.ControlScreen;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardInputsIngame implements KeyListener {
    private final GameView gameView;
    private final Map<GameControls, Runnable> controlActionsOnPress = new HashMap<>();
    private final Map<GameControls, Runnable> controlActionsOnRelease = new HashMap<>();
    private boolean isChangingControl = false;
    private final GameController gameController;
    private GameControls gameControls;



    public KeyboardInputsIngame(GameView gameView, GameController gameController) {
        this.gameView = gameView;
        this.gameController = gameController;
        initializeControlActionsOnPress(gameController.getEntityController(), gameController.getItemController(), gameController, gameController.getScreenController());
        initializeControlActionsOnRelease(gameController.getEntityController(), gameController);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleUserInputKeyPressed(e);
        ControlScreen controlScreen = gameController.getScreenController().getControlScreen();
        String keyText = KeyEvent.getKeyText(e.getKeyCode());

        if (isChangingControl) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    controlScreen.setSubtitle("You can't change the key from " + controlScreen.getSelectedControl() + " to " + keyText + "! Select a key and press enter if you want to change this key.");
                    isChangingControl = false;
                    controlScreen.setChangingControl(isChangingControl);
                    break;
                default:
                    if (controlScreen.getSelectedControl() != null && controlScreen.getSubtitle().startsWith("Please change the control of")) {
                        boolean keyExistsInFile = controlScreen.getKeyCodeToFileList().stream().anyMatch(map -> map.get("keyCodeName").equals(keyText));
                        if (keyExistsInFile && !controlScreen.getControls().containsValue(e.getKeyCode()) && !keyText.equals("Eingabe")) {
                            controlScreen.getControls().put(controlScreen.getSelectedControl(), e.getKeyCode());
                            gameController.saveControlsToJson("res/configsAndSaves/controls.json");

                            List<Control> controlsList = new ArrayList<>();
                            for (Map.Entry<String, Integer> entry : controlScreen.getControls().entrySet()) {
                                controlsList.add(new Control(entry.getKey(), entry.getValue()));
                            }

                            try (Writer writer = new FileWriter("res/configsAndSaves/controls.json")) {
                                Gson gson = new GsonBuilder().create();
                                gson.toJson(controlsList, writer);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            String fileName = controlScreen.getKeyCodeToFileList().stream()
                                    .filter(map -> map.get("keyCodeName").equals(keyText))
                                    .findFirst()
                                    .map(map -> map.get("fileName"))
                                    .orElse(null);
                            if (fileName != null) {
                                try {
                                    BufferedImage img = ImageIO.read(new File("res/controlButtons/" + fileName));
                                    controlScreen.getControlImages().put(controlScreen.getSelectedControl(), img);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }

                            controlScreen.setChangingControl(false);
                            isChangingControl = false;
                            controlScreen.drawControls(gameView.getGraphics());
                            controlScreen.setSubtitle("You changed the key of " + controlScreen.getSelectedControl() + " to " + keyText + "! If you want to change another key select it with arrow keys and press enter.");
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleUserInputKeyRelease(e);
    }

    public void handleUserInputKeyPressed(KeyEvent e) {
        gameControls = GameControls.getControlByCode(e.getKeyCode());
        if (gameControls != null) {
            Runnable action = controlActionsOnPress.get(gameControls);
            if (action != null) {
                action.run();
            }
        }
    }

    public void handleUserInputKeyRelease(KeyEvent e) {
        gameControls = GameControls.getControlByCode(e.getKeyCode());
        if (gameControls != null) {
            Runnable action = controlActionsOnRelease.get(gameControls);
            if (action != null) {
                action.run();
            }
        }
    }

    public void changeControlKey(GameControls control, int newKeyCode) {
        if (control.isChangeable() && control.isKeyAllowed(newKeyCode)) {
            control.setKeyCode(newKeyCode);
        }
    }

    private void initializeControlActionsOnPress(EntityController entityController, ItemController itemController, GameController gameController, ScreenController screenController) {
        controlActionsOnPress.put(GameControls.MOVE_RIGHT, () -> entityController.getPlayer().setRight(true));
        controlActionsOnPress.put(GameControls.MOVE_LEFT, () -> entityController.getPlayer().setLeft(true));
        controlActionsOnPress.put(GameControls.JUMP, () -> entityController.getPlayer().jump());
        controlActionsOnPress.put(GameControls.ATTACK, () -> entityController.getPlayer().attack());
        controlActionsOnPress.put(GameControls.DASH, () -> entityController.getPlayer().setIsDashing(true));
        controlActionsOnPress.put(GameControls.REST, () -> entityController.getPlayer().setIsRestingIfNotInAir(true));
        controlActionsOnPress.put(GameControls.ITEM_1, () -> itemController.selectItem(entityController.getPlayer(), 1));
        controlActionsOnPress.put(GameControls.ITEM_2, () -> itemController.selectItem(entityController.getPlayer(), 2));
        controlActionsOnPress.put(GameControls.ITEM_3, () -> itemController.selectItem(entityController.getPlayer(), 3));
        controlActionsOnPress.put(GameControls.ITEM_4, () -> itemController.selectItem(entityController.getPlayer(), 4));
        controlActionsOnPress.put(GameControls.ITEM_5, () -> itemController.selectItem(entityController.getPlayer(), 5));
        controlActionsOnPress.put(GameControls.OPEN_HIGHSCORE_TABLE, () -> gameController.setIsDrawingListOfCurrentPlayersForInterfaceGame(true));
        controlActionsOnPress.put(GameControls.ACTIVATE_MULTIPLAYER, gameController::useMultiplayer);
        controlActionsOnPress.put(GameControls.RESTART_AFTER_DEATH, gameController::restartLevelAfterDeath);
        controlActionsOnPress.put(GameControls.NAVIGATE_LEFT, () -> {
            switch (gameController.getCurrentGameState()) {
                case CONTROLS:
                    if (!isChangingControl) {
                        int currentIndex = screenController.getControlScreen().getSelectedControlIndex();
                        int controlsPerColumn = (int) Math.ceil((double) screenController.getControlScreen().getControlNames().size() / 3);
                        int currentRow = currentIndex % controlsPerColumn;
                        int currentColumn = currentIndex / controlsPerColumn;

                        // Move to the previous column, wrapping around if necessary
                        currentColumn--;
                        if (currentColumn < 0) {
                            currentColumn = 2; // Wrap around to the last column
                        }

                        // Calculate the new index
                        int newIndex = currentRow + currentColumn * controlsPerColumn;
                        if (newIndex >= screenController.getControlScreen().getControlNames().size()) {
                            currentColumn--; // Move to the previous column
                            if (currentColumn < 0) {
                                currentColumn = 2; // Wrap around to the last column
                            }
                            newIndex = currentRow + currentColumn * controlsPerColumn;
                        }
                        screenController.getControlScreen().setSelectedControlIndex(newIndex);
                        screenController.getControlScreen().setSelectedControl(screenController.getControlScreen().getControlNames().get(newIndex));
                    }
                    break;
            }
        });
        controlActionsOnPress.put(GameControls.NAVIGATE_RIGHT, () -> {
            switch (gameController.getCurrentGameState()) {
                case CONTROLS:
                    if (!isChangingControl) {
                        int currentIndex = screenController.getControlScreen().getSelectedControlIndex();
                        int controlsPerColumn = (int) Math.ceil((double) screenController.getControlScreen().getControlNames().size() / 3);
                        int currentRow = currentIndex % controlsPerColumn;
                        int currentColumn = currentIndex / controlsPerColumn;

                        // Move to the next column, wrapping around if necessary
                        currentColumn++;
                        if (currentColumn > 2) {
                            currentColumn = 0; // Wrap around to the first column
                        }

                        // Calculate the new index
                        int newIndex = currentRow + currentColumn * controlsPerColumn;
                        if (newIndex >= screenController.getControlScreen().getControlNames().size()) {
                            currentColumn++; // Move to the next column
                            if (currentColumn > 2) {
                                currentColumn = 0; // Wrap around to the first column
                            }
                            newIndex = currentRow + currentColumn * controlsPerColumn;
                        }
                        screenController.getControlScreen().setSelectedControlIndex(newIndex);
                        screenController.getControlScreen().setSelectedControl(screenController.getControlScreen().getControlNames().get(newIndex));
                    }
                    break;
            }
        });
        controlActionsOnPress.put(GameControls.NAVIGATE_DOWN, () -> {
            switch (gameController.getCurrentGameState()) {
                case START:
                    screenController.getStartScreen().handleDownKey();
                    break;
                case CONTROLS:
                    if (!isChangingControl) {
                        int currentIndex = screenController.getControlScreen().getSelectedControlIndex();
                        int controlsPerColumn = (int) Math.ceil((double) screenController.getControlScreen().getControlNames().size() / 3);
                        int currentRow = currentIndex % controlsPerColumn;
                        int currentColumn = currentIndex / controlsPerColumn;

                        // Move to the next row, wrapping around if necessary
                        currentRow++;
                        if (currentRow >= controlsPerColumn) currentRow = 0;
                        if (currentColumn == 2 && currentRow + 1 >= controlsPerColumn) currentRow = 0;

                        // Calculate the new index
                        int newIndex = currentRow + currentColumn * controlsPerColumn;
                        screenController.getControlScreen().setSelectedControlIndex(newIndex);
                        screenController.getControlScreen().setSelectedControl(screenController.getControlScreen().getControlNames().get(newIndex));
                    }
                    break;
            }
        });
        controlActionsOnPress.put(GameControls.NAVIGATE_UP, () -> {
            switch (gameController.getCurrentGameState()) {
                case START:
                    screenController.getStartScreen().handleUpKey();
                    break;
                case CONTROLS:
                    if (!isChangingControl) {
                        int currentIndex = screenController.getControlScreen().getSelectedControlIndex();
                        int controlsPerColumn = (int) Math.ceil((double) screenController.getControlScreen().getControlNames().size() / 3);
                        int currentRow = currentIndex % controlsPerColumn;
                        int currentColumn = currentIndex / controlsPerColumn;

                        // Move to the previous row, wrapping around if necessary
                        currentRow--;
                        if (currentRow < 0) {
                            currentRow = controlsPerColumn - 1; // Wrap around to the last row
                            if (currentColumn == 2) currentRow = controlsPerColumn - 2;
                        }

                        // Calculate the new index
                        int newIndex = currentRow + currentColumn * controlsPerColumn;
                        screenController.getControlScreen().setSelectedControlIndex(newIndex);
                        screenController.getControlScreen().setSelectedControl(screenController.getControlScreen().getControlNames().get(newIndex));
                    }
                    break;
            }
        });
        controlActionsOnPress.put(GameControls.CONFIRM, () -> {
            switch (gameController.getCurrentGameState()) {
                case END:
                    try {
                        gameController.resetGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case HIGHSCORE:
                    gameController.setCurrentGameState(GameState.START);
                    break;
                case START:
                    switch (screenController.getStartScreen().getSelectedButton()) {
                        case 0:
                            try {
                                gameController.newGame();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case 1:
                            try {
                                gameController.startGame();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case 2:
                            gameController.setCurrentGameState(GameState.HIGHSCORE);
                            break;
                        case 3:
                            gameController.setCurrentGameState(GameState.CONTROLS);
                            break;
                        case 4:
                            System.exit(0);
                            break;
                    }
                    break;
                case CONTROLS:
                    if (!screenController.getControlScreen().isChangingControl()) {
                        String selectedControl = screenController.getControlScreen().getSelectedControl();
                        isChangingControl = true;
                        screenController.getControlScreen().setChangingControl(isChangingControl);
                        screenController.getControlScreen().setSubtitle("Please change the control of " + selectedControl + " or abort with escape.");
                        break;
                    }
            }
        });
        controlActionsOnPress.put(GameControls.ESCAPE, () -> {
            switch (gameController.getCurrentGameState()) {
                case CONTROLS:
                    if (isChangingControl) {
                        isChangingControl = false;
                        screenController.getControlScreen().setChangingControl(isChangingControl);
                        screenController.getControlScreen().setSubtitle("Select a key you want to change with arrow keys and press enter if you want to change this key.");
                    } else {
                        gameController.loadAndInitializeControls();
                        gameController.setCurrentGameState(GameState.START);
                    }
                    break;
                case HIGHSCORE:
                    gameController.setCurrentGameState(GameState.START);
                    break;
                case END:
                    System.exit(0);
                    break;
            }
        });

    }

    private void initializeControlActionsOnRelease(EntityController entityController, GameController gameController) {
        controlActionsOnRelease.put(GameControls.MOVE_RIGHT, () -> entityController.getPlayer().setRight(false));
        controlActionsOnRelease.put(GameControls.MOVE_LEFT, () -> entityController.getPlayer().setLeft(false));
        controlActionsOnRelease.put(GameControls.REST, () -> entityController.getPlayer().setIsRestingIfNotInAir(false));
        controlActionsOnRelease.put(GameControls.OPEN_HIGHSCORE_TABLE, () -> gameController.setIsDrawingListOfCurrentPlayersForInterfaceGame(false));
    }
}
