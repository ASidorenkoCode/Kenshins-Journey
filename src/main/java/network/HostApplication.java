package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostApplication {
    private static final int PORT = 4711;

    private static ArrayList<ServerObject> serverObjects = new ArrayList<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, serverObjects).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


