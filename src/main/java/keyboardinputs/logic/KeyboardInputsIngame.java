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
    private GameView gameView;
    private Map<GameControls, Runnable> controlActionsOnPress = new HashMap<>();
    private Map<GameControls, Runnable> controlActionsOnRelease = new HashMap<>();
    private boolean isChangingControl = false;
    private GameController gameController;


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

        if (isChangingControl) {
            switch (e.getKeyCode()) {
                default:
                    ControlScreen controlScreen = gameController.getScreenController().getControlScreen();
                    if (controlScreen.getSelectedControl() != null && controlScreen.getSubtitle().startsWith("Please change the control of")) {
                        String keyText = KeyEvent.getKeyText(e.getKeyCode());
                        boolean keyExistsInFile = controlScreen.getKeyCodeToFileList().stream().anyMatch(map -> map.get("keyCodeName").equals(keyText));
                        if (keyExistsInFile && !controlScreen.getControls().containsValue(e.getKeyCode()) && !keyText.equals("Eingabe")) {
                            controlScreen.getControls().put(controlScreen.getSelectedControl(), e.getKeyCode());
                            gameController.saveControlsToJson("res/configsAndSaves/controls.json");


                            // Convert the controls map to a list of Control objects
                            List<Control> controlsList = new ArrayList<>();
                            for (Map.Entry<String, Integer> entry : controlScreen.getControls().entrySet()) {
                                controlsList.add(new Control(entry.getKey(), entry.getValue()));
                            }

                            // Serialize the controlsList to JSON and write it to the file
                            try (Writer writer = new FileWriter("res/configsAndSaves/controls.json")) {
                                Gson gson = new GsonBuilder().create();
                                gson.toJson(controlsList, writer);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            // Load the new image and store it in the controlImages map
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
        GameControls control = GameControls.getControlByCode(e.getKeyCode());
        if (control != null) {
            Runnable action = controlActionsOnPress.get(control);
            if (action != null) {
                action.run();
            }
        }
    }

    public void handleUserInputKeyRelease(KeyEvent e) {
        GameControls control = GameControls.getControlByCode(e.getKeyCode());
        if (control != null) {
            Runnable action = controlActionsOnRelease.get(control);
            if (action != null) {
                action.run();
            }
        }
    }

    public void changeControl(GameControls control, int newKeyCode) {
        if (control.isChangeable()) {
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
        controlActionsOnPress.put(GameControls.NAVIGATE_DOWN, () -> {
            switch (gameController.getCurrentGameState()) {
                case START:
                    screenController.getStartScreen().handleDownKey();
                    break;
                case CONTROLS:
                    if (!isChangingControl) {
                        int newIndex = screenController.getControlScreen().getSelectedControlIndex();
                        newIndex++;
                        if (newIndex >= screenController.getControlScreen().getControlNames().size()) {
                            newIndex = 0;
                        }
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
                        int newIndex = screenController.getControlScreen().getSelectedControlIndex();
                        newIndex--;
                        if (newIndex < 0) {
                            newIndex = screenController.getControlScreen().getControlNames().size() - 1;
                        }
                        screenController.getControlScreen().setSelectedControlIndex(newIndex);
                        screenController.getControlScreen().setSelectedControl(screenController.getControlScreen().getControlNames().get(newIndex));
                    }
                    break;
            }
        });
        controlActionsOnPress.put(GameControls.OPEN_CONTROLS, () -> {
            if (gameController.getCurrentGameState() == GameState.START) {
                gameController.setCurrentGameState(GameState.CONTROLS);
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
                            System.exit(0);
                            break;
                    }
                    break;
                case CONTROLS:
                    if (!screenController.getControlScreen().isChangingControl()) {
                        String selectedControl = screenController.getControlScreen().getSelectedControl();
                        isChangingControl = true;
                        screenController.getControlScreen().setChangingControl(isChangingControl);
                        screenController.getControlScreen().setSubtitle("Please change the control of " + selectedControl + " or abort with escape");
                        break;
                    }
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
