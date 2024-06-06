package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 4711;

    public Client() {
    }

    public void sendDataToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //get hostname for playerID

            String playerId = "";
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                playerId = inetAddress.getHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            // Create a JSON string
            String json = "{\"currentLevel\":2,\"highScore\":\"3250\",\"deathCounter\":5,\"horizontalPlayerPosition\":5,\"playerId\":" + playerId + "}";


            // Send the JSON string to the server
            writer.println(json);

            String responseJson = reader.readLine();
            System.out.println("Response JSON from server: " + responseJson);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
