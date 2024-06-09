package network.host;

import network.client.ClientHandler;
import network.data.ServerObject;
import network.data.SharedData;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Host extends Thread {

    private static ArrayList<ServerObject> serverObjects = new ArrayList<>();


    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(SharedData.SERVER_PORT)) {
            System.out.println("Server is listening on port " + SharedData.SERVER_PORT);

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


