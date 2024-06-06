package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("New client connected");

            // Create input and output streams to read from and write to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read a message sent by the client
            String request = in.readLine();
            System.out.println("Received from client: " + request);

            // Send a response to the client
            String response = "Hello from the server!";
            out.println(response);
            System.out.println("Sent to client: " + response);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
