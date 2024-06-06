package network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Read the JSON data from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String json = jsonBuilder.toString();
            System.out.println("Received JSON: " + json);

            // Parse the JSON data
            Gson gson = new Gson();
            ServerObject myObject = gson.fromJson(json, ServerObject.class);

            // Process the received object
            System.out.println("Received object: " + myObject);


            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
