package screens.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.UI.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlScreen {
    private Map<String, Integer> controls = new HashMap<>();
    private Map<String, BufferedImage> controlImages = new HashMap<>();
    private String selectedControl;
    private String title;
    private String subtitle;
    private boolean isChangingControl = false;
    private int selectedControlIndex = 0;
    private List<String> controlNames;
    private List<Map<String, String>> keyCodeToFileList;


    public ControlScreen() throws IOException {
        title = "Control Settings";
        subtitle = "\"Select a key you want to change with arrow keys and press enter if you want to change this key";

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
            selectedControl = controls.keySet().iterator().next();
        }

        controlNames = new ArrayList<>(controls.keySet());
    }

    public void drawControls(Graphics g) {

        int y = 50;
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, GameView.GAME_WIDTH, GameView.GAME_HEIGHT);

        // Draw title and subtitle
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, 50, 20);
        g2d.drawString(subtitle, 50, 40);

        for (String control : controls.keySet()) {
            BufferedImage img = controlImages.get(control);
            if (img != null) {
                if (control.equals(selectedControl)) {
                    if (isChangingControl) {
                        g2d.setColor(Color.RED);
                    } else {
                        g2d.setColor(Color.GREEN);
                    }
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.drawRect(50, y, img.getWidth(), img.getHeight());

                g.drawImage(img, 50, y, null);
                g.setColor(Color.WHITE); // Set the color for the text

                String formattedControl = control.replace("_", " ").toLowerCase();
                formattedControl = Character.toUpperCase(formattedControl.charAt(0)) + formattedControl.substring(1);

                g.drawString(formattedControl, 50 + img.getWidth() + 10, y + img.getHeight() / 2); // Draw the control name next to the image
            }
            y += img.getHeight() + 10; // Adjust y coordinate for next image
        }
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