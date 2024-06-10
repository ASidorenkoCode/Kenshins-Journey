package network.host;

import network.data.SharedData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class HostChecker {
    public static boolean isServerRunning(String host) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, SharedData.SERVER_PORT), 2000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
