package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.logic.Player;
import game.logic.Highscore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 4711;

    public Client() {
    }

    public ArrayList<ServerObject> sendDataToServer(Highscore highscore, Player player, String playerId) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson = new Gson();


            // Create a JSON string
            String json = STR."{\"currentLevel\":\{highscore.getAllHighscores().size() + 1},\"highScore\":\"\{highscore.getCurrentHighscore()}\",\"deathCounter\":\{highscore.getDeathCounter()},\"horizontalPlayerPosition\":\{player.getX()},\"playerId\":\{playerId}}";


            // Send the JSON string to the server
            writer.println(json);

            Type serverObjectListType = new TypeToken<ArrayList<ServerObject>>() {
            }.getType();
            String responseJson = reader.readLine();
            ArrayList<ServerObject> response = gson.fromJson(responseJson, serverObjectListType);
            System.out.println("Response JSON from server: " + responseJson);

            socket.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}