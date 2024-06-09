package network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class GitHubClient {
    private static final String REPO_OWNER = "comhendrik";
    private static final String REPO_NAME = "Kenshins-Journey-IP_Address";
    private static final String FILE_PATH = "data.txt";
    private static final String GITHUB_TOKEN = ""; // Replace with your actual GitHub token
    private static final String API_URL = "https://api.github.com/repos/" + REPO_OWNER + "/" + REPO_NAME + "/contents/" + FILE_PATH;
    private static final Gson gson = new Gson();

    public static boolean hostIsRunning() {
        try {
            String content = readFile();
            System.out.println(content);
            return !content.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readFile() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JsonObject json = gson.fromJson(content.toString(), JsonObject.class);
            String base64Content = json.get("content").getAsString().replaceAll("\n", "").replaceAll("\r", "");

            // Decode Base64 content
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            String fileContent = new String(decodedBytes);
            return fileContent.trim();
        } else {
            connection.disconnect();
            System.out.println("GET request failed. Response Code: " + responseCode);
            return "";
        }
    }

    public static void writeFile(String content) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection getConnection = (HttpURLConnection) url.openConnection();
        getConnection.setRequestMethod("GET");
        getConnection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
        getConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int getResponseCode = getConnection.getResponseCode();
        if (getResponseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
            StringBuilder getResponse = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                getResponse.append(inputLine);
            }
            in.close();
            JsonObject getJson = gson.fromJson(getResponse.toString(), JsonObject.class);
            String sha = getJson.get("sha").getAsString();

            getConnection.disconnect();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("Content-Type", "application/json");

            JsonObject json = new JsonObject();
            json.addProperty("message", "Updating data.txt");
            json.addProperty("content", Base64.getEncoder().encodeToString(content.getBytes()));
            json.addProperty("sha", sha);

            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(gson.toJson(json).getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader inPut = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLinePut;
                StringBuilder responsePut = new StringBuilder();
                while ((inputLinePut = inPut.readLine()) != null) {
                    responsePut.append(inputLinePut);
                }
                inPut.close();
                System.out.println("Response: " + responsePut.toString());
            } else {
                System.out.println("PUT request failed. Response Code: " + responseCode);
            }

            connection.disconnect();
        } else {
            getConnection.disconnect();
            System.out.println("GET request failed. Response Code: " + getResponseCode);
        }
    }

    private static void deleteFileContent() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection getConnection = (HttpURLConnection) url.openConnection();
        getConnection.setRequestMethod("GET");
        getConnection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
        getConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int getResponseCode = getConnection.getResponseCode();
        if (getResponseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
            StringBuilder getResponse = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                getResponse.append(inputLine);
            }
            in.close();
            JsonObject getJson = gson.fromJson(getResponse.toString(), JsonObject.class);
            String sha = getJson.get("sha").getAsString();

            getConnection.disconnect();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("Content-Type", "application/json");

            JsonObject json = new JsonObject();
            json.addProperty("message", "Deleting content of data.txt");
            json.addProperty("content", Base64.getEncoder().encodeToString("".getBytes()));
            json.addProperty("sha", sha);

            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(gson.toJson(json).getBytes());
            os.flush();
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader inPut = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLinePut;
                StringBuilder responsePut = new StringBuilder();
                while ((inputLinePut = inPut.readLine()) != null) {
                    responsePut.append(inputLinePut);
                }
                inPut.close();
                System.out.println("Response: " + responsePut.toString());
            } else {
                System.out.println("PUT request failed. Response Code: " + responseCode);
            }

            connection.disconnect();
        } else {
            getConnection.disconnect();
            System.out.println("GET request failed. Response Code: " + getResponseCode);
        }
    }
}


