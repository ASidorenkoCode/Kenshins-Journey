package network;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SharedData {

    public static final int SERVER_PORT = 4711;
    public static final BlockingQueue<ArrayList<ServerObject>> networkToGameQueue = new LinkedBlockingQueue<>();
    public static final BlockingQueue<ServerObject> gameToNetworkQueue = new LinkedBlockingQueue<>();


}
