import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ForbiddenValues {
    private final Set<Integer> forbiddenBlueValues;
    private final Set<Integer> forbiddenGreenValues;

    public ForbiddenValues(String filePath) {
        forbiddenBlueValues = new HashSet<>();
        forbiddenGreenValues = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String[] values = parts[1].split(",");
                    Set<Integer> targetSet = parts[0].trim().equals("blue") ? forbiddenBlueValues : forbiddenGreenValues;
                    for (String value : values) {
                        targetSet.add(Integer.parseInt(value.trim()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBlueForbidden(int value) {
        return forbiddenBlueValues.contains(value);
    }

    public boolean isGreenForbidden(int value) {
        return forbiddenGreenValues.contains(value);
    }
}