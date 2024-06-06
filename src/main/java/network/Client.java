package network;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 4711;

    public Client() {
    }

    public void sendDataToServer() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // Create a JSON string
            String json = "{\"name\":\"John Doe\",\"age\":30}";

            // Send the JSON string to the server
            writer.println(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
