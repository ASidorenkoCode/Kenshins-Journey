package network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private Host hostApplication;

    public ClientHandler(Socket socket, Host hostApplication) {
        this.socket = socket;
        this.hostApplication = hostApplication;
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

            if (request.equals("quit")) {
                hostApplication.refreshServerObjects();
                return;
            }


            ServerObject serverObject = gson.fromJson(request, ServerObject.class);
            hostApplication.addOrUpdateServerObject(serverObject);


            // Send a response to the client
            String response = gson.toJson(hostApplication.getServerObjects());
            out.println(response);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}