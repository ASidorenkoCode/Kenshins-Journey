package network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;

    private ArrayList<ServerObject> serverObjects;

    public ClientHandler(Socket socket, ArrayList<ServerObject> serverObjects) {
        this.socket = socket;
        this.serverObjects = serverObjects;
    }

    @Override
    public void run() {
        try {
            System.out.println("New client connected");
            Gson gson = new Gson();

            // Create input and output streams to read from and write to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read a message sent by the client
            String request = in.readLine();
            ServerObject serverObject = gson.fromJson(request, ServerObject.class);
            addOrUpdateServerObject(serverObject);

            System.out.println("Received from client: " + serverObject);

            // Send a response to the client
            String response = gson.toJson(serverObjects);
            out.println(response);
            System.out.println("Sent to client: " + request);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addOrUpdateServerObject(ServerObject currentObject) {
        for (ServerObject object : serverObjects) {
            if (object.getPlayerId().equals(currentObject.getPlayerId())) {
                object.updateObject(currentObject);
                return;
            }
        }
        serverObjects.add(currentObject);
    }
}
