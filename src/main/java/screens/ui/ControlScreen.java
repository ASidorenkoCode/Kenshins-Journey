package screens.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.UI.GameView;
import game.controller.GameControls;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ControlScreen {
    private final Map<String, Integer> controls = new HashMap<>();
    private Map<String, BufferedImage> controlImages = new HashMap<>();
    private String selectedControl;
    private final String title;
    private String subtitle;
    private boolean isChangingControl = false;
    private int selectedControlIndex = 0;
    private List<String> controlNames;
    private List<Map<String, String>> keyCodeToFileList;
    private static final List<String> DISPLAYED_CONTROLS = Arrays.asList(
            "MOVE_LEFT",
            "MOVE_RIGHT",
            "ATTACK",
            "JUMP",
            "DASH",
            "REST",
            "RESTART_AFTER_DEATH",
            "OPEN_HIGHSCORE_TABLE",
            "ACTIVATE_MULTIPLAYER",
            "ITEM_1",
            "ITEM_2",
            "ITEM_3",
            "ITEM_4",
            "ITEM_5"
    );


    public ControlScreen() throws IOException {
        title = "Control Settings";
        subtitle = "Select a key you want to change with arrow keys and press enter if you want to change this key";

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> controlsList = gson.fromJson(new FileReader("res/configsAndSaves/controls.json"), listType);

        Map<Integer, String> keyCodeToNameMap = new HashMap<>();
        for (Map<String, Object> map : controlsList) {
            Double keyCodeDouble = (Double) map.get("keyCode");
            int keyCode = keyCodeDouble.intValue();
            String name = (String) map.get("name");
            keyCodeToNameMap.put(keyCode, name);
        }

        listType = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        keyCodeToFileList = gson.fromJson(new FileReader("res/configsAndSaves/KeyEventToFileName.json"), listType);

        Map<String, String> keyCodeNameToFileMap = new HashMap<>();
        for (Map<String, String> map : keyCodeToFileList) {
            String keyCodeName = map.get("keyCodeName");
            String fileName = map.get("fileName");
            keyCodeNameToFileMap.put(keyCodeName, fileName);
        }

        for (Map<String, Object> control : controlsList) {
            String controlName = (String) control.get("name");
            Double keyCodeDouble = (Double) control.get("keyCode");
            int keyCode = keyCodeDouble.intValue();
            controls.put(controlName, keyCode);
            String keyText = KeyEvent.getKeyText(keyCode);
            if (controlName != null) {
                controls.put(controlName, keyCode);
                try {
                    String fileName = getKeyCodeToFileList().stream()
                            .filter(map -> map.get("keyCodeName").equals(keyText))
                            .findFirst()
                            .map(map -> map.get("fileName"))
                            .orElse(null);
                    if (fileName != null) {
                        BufferedImage img = ImageIO.read(new File("res/controlButtons/" + fileName));
                        controlImages.put(controlName, img); // Store the image in the map
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (String control : controls.keySet()) {
            try {
                String fileName = keyCodeNameToFileMap.get(KeyEvent.getKeyText(controls.get(control)));
                if (fileName != null) {
                    BufferedImage img = ImageIO.read(new File("res/controlButtons/" + fileName));
                    controlImages.put(control, img); // Store the image in the map
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Set the first control as the selected control
        if (!controls.isEmpty()) {
            selectedControl = DISPLAYED_CONTROLS.getFirst();

        }

        controlNames = new ArrayList<>();
        for (String control : DISPLAYED_CONTROLS) {
            if (controls.containsKey(control)) {
                controlNames.add(control);
            }
        }
    }

    public void drawControls(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        g2d.setFont(new Font("default", Font.BOLD, 20));

        g2d.setColor(Color.WHITE);
        int titleWidth = g.getFontMetrics().stringWidth(title);
        int titleX = (GameView.GAME_WIDTH - titleWidth) / 2;
        g2d.drawString(title, titleX, 50); // Decreased from 20 to 10

        int subtitleWidth = g.getFontMetrics().stringWidth(subtitle);
        int subtitleX = (GameView.GAME_WIDTH - subtitleWidth) / 2;
        g2d.drawString(subtitle, subtitleX, 100); // Decreased from 40 to 30

        g2d.setFont(new Font("default", Font.BOLD, 12));

        int maxWidth = 0;
        int totalHeight = 0;
        for (String control : DISPLAYED_CONTROLS) {
            BufferedImage img = controlImages.get(control);
            if (img != null) {
                String formattedControl = control.replace("_", " ").toLowerCase();
                formattedControl = Character.toUpperCase(formattedControl.charAt(0)) + formattedControl.substring(1);
                int controlWidth = img.getWidth() + g.getFontMetrics().stringWidth(formattedControl) + 10;
                maxWidth = Math.max(maxWidth, controlWidth);
                totalHeight += img.getHeight() * 2 + 20;
            }
        }

        int controlsPerColumn = (int) Math.ceil((double) DISPLAYED_CONTROLS.size() / 3);

        int controlCount = 0;
        int x = (GameView.GAME_WIDTH - maxWidth * 3) / 2;
        int y = (GameView.GAME_HEIGHT - totalHeight / 3) / 2;

        for (String control : DISPLAYED_CONTROLS) {
            BufferedImage img = controlImages.get(control);
            if (img != null) {
                int newWidth = img.getWidth() * 2;
                int newHeight = img.getHeight() * 2;
                Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                if (control.equals(selectedControl)) {
                    if (isChangingControl) {
                        g2d.setColor(Color.RED);
                    } else {
                        g2d.setColor(Color.GREEN);
                    }
                } else {
                    g2d.setColor(Color.WHITE);
                }

                String formattedControl = control.replace("_", " ").toLowerCase();
                formattedControl = Character.toUpperCase(formattedControl.charAt(0)) + formattedControl.substring(1);
                int textWidth = g.getFontMetrics().stringWidth(formattedControl);
                g2d.drawRect(x, y, maxWidth + 20, newHeight);

                g.drawImage(scaledImg, x, y, null);
                g.setColor(Color.WHITE);
                g.drawString(formattedControl, x + maxWidth - textWidth + 10, y + newHeight / 2);

                controlCount++;
                if (controlCount % controlsPerColumn == 0 && controlCount / controlsPerColumn < 3) {
                    x += maxWidth + 50;
                    y = (GameView.GAME_HEIGHT - totalHeight / 3) / 2; // Reset y to the center of the screen for the new column
                } else {
                    y += newHeight + 20;
                }
            }
        }
    }

    public List<String> getChangeableControlNames() {
        return controlNames.stream()
                .filter(name -> GameControls.valueOf(name).isChangeable())
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getControls() {
        return controls;
    }

    public String getSelectedControl() {
        return selectedControl;
    }

    public void setSelectedControl(String selectedControl) {
        this.selectedControl = selectedControl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean isChangingControl() {
        return isChangingControl;
    }

    public void setChangingControl(boolean changingControl) {
        isChangingControl = changingControl;
    }

    public int getSelectedControlIndex() {
        return selectedControlIndex;
    }

    public void setSelectedControlIndex(int selectedControlIndex) {
        this.selectedControlIndex = selectedControlIndex;
    }

    public List<String> getControlNames() {
        return controlNames;
    }

    public void setControlNames(List<String> controlNames) {
        this.controlNames = controlNames;
    }

    public List<Map<String, String>> getKeyCodeToFileList() {
        return keyCodeToFileList;
    }

    public void setKeyCodeToFileList(List<Map<String, String>> keyCodeToFileList) {
        this.keyCodeToFileList = keyCodeToFileList;
    }

    public Map<String, BufferedImage> getControlImages() {
        return controlImages;
    }

    public void setControlImages(Map<String, BufferedImage> controlImages) {
        this.controlImages = controlImages;
    }


}