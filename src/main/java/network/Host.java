package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Host {
    private static final int PORT = 4711;

    private static ArrayList<ServerObject> serverObjects = new ArrayList<>();


    public void runApplication() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                System.out.println(serverObjects.size());
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, this).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshServerObjects() {
        serverObjects.clear();
    }

    public void addOrUpdateServerObject(ServerObject currentObject) {
        for (ServerObject object : serverObjects) {
            if (object.getPlayerId().equals(currentObject.getPlayerId())) {
                object.updateObject(currentObject);
                return;
            }
        }
        serverObjects.add(currentObject);
    }

    public ArrayList<ServerObject> getServerObjects() {
        return serverObjects;
    }
}


