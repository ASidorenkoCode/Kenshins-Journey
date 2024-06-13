package network.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GitHubClient {
    private static final String REPO_OWNER = "DEIN-BENUTZERNAME";
    private static final String REPO_NAME = "NAME-DER-REPOSITORY";
    private static final String FILE_PATH = "data.txt";
    private static final String GITHUB_TOKEN = "DEINEN-KLASSISCHEN-GITHUB-TOKEN";
    private static final String API_URL = "https://api.github.com/repos/" + REPO_OWNER + "/" + REPO_NAME + "/contents/" + FILE_PATH;
    private static final Gson gson = new Gson();


    public static String readFile() throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .header("Authorization", "token " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseCode = response.statusCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            String base64Content = json.get("content").getAsString().replaceAll("\n", "").replaceAll("\r", "");

            // Decode Base64 content
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            String fileContent = new String(decodedBytes);
            return fileContent.trim();
        } else {
            System.out.println("GET request failed. Response Code: " + responseCode);
            return "";
        }
    }

    public static void writeFile(String content) throws Exception {
        String sha = fetchSha();

        if (sha != null) {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject json = new JsonObject();
            json.addProperty("message", "Updating data.txt");
            json.addProperty("content", Base64.getEncoder().encodeToString(content.getBytes()));
            json.addProperty("sha", sha);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
                    .header("Authorization", "token " + GITHUB_TOKEN)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("PUT request failed. Response Code: " + responseCode);
            }
        }
    }

    public static void deleteFileContent() throws Exception {
        String sha = fetchSha();

        if (sha != null) {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject json = new JsonObject();
            json.addProperty("message", "Deleting content of data.txt");
            json.addProperty("content", Base64.getEncoder().encodeToString("".getBytes()));
            json.addProperty("sha", sha);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
                    .header("Authorization", "token " + GITHUB_TOKEN)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("PUT request failed. Response Code: " + responseCode);
            }
        }
    }

    private static String fetchSha() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .header("Authorization", "token " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int responseCode = response.statusCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            return json.get("sha").getAsString();
        } else {
            System.out.println("GET request failed. Response Code: " + responseCode);
            return null;
        }
    }
}


