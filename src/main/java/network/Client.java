package network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
    private String serverAdress;

    private long comparingTime = System.currentTimeMillis();

    public Client(String serverAdress) {
        this.serverAdress = serverAdress;
    }

    @Override
    public void run() {
        while (true) {

            if (SharedData.gameToNetworkQueue.isEmpty()) continue;

            try {

                ServerObject currentObject = SharedData.gameToNetworkQueue.take();

                SharedData.networkToGameQueue.put(sendDataToServer(currentObject));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public ArrayList<ServerObject> sendDataToServer(ServerObject object) {
        try {
            Socket socket = new Socket(serverAdress, SharedData.SERVER_PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson = new Gson();


            // Create a JSON string
            String json = gson.toJson(object, ServerObject.class);
            // Send the JSON string to the server
            writer.println(json);

            Type serverObjectListType = new TypeToken<ArrayList<ServerObject>>() {
            }.getType();
            String responseJson = reader.readLine();
            ArrayList<ServerObject> response = gson.fromJson(responseJson, serverObjectListType);

            socket.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public void playerQuitsGame() {
        try {
            System.out.println(true);
            Socket socket = new Socket(serverAdress, SharedData.SERVER_PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("quit");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
