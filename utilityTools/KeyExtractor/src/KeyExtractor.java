import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class KeyExtractor {
    private static final int KEY_FIRST = 0;
    private static final int KEY_LAST = 600;

    public void extractKeysToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = KEY_FIRST; i < KEY_LAST; i++) {
                String keyName = KeyEvent.getKeyText(i).toUpperCase();
                if (!keyName.contains("UNBEKANNT")) {
                    writer.write(keyName + ".png");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Main {
        public static void main(String[] args) {
            KeyExtractor keyExtractor = new KeyExtractor();
            keyExtractor.extractKeysToFile("utilityTools/KeyExtractor/output/keys.txt");
        }
    }
}