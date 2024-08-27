package ulb.infof307.g10.network.connection;


import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketClient {
    /**
     * This class represents a client. You can send requests with sendRequest
     * If you want to test this class, you can use "javac Client java" to compile & "java Client"
     * This is a temporary behaviour. The main function of Client will be deleted when the interface is functional
     **/

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private Socket socket;

    public SocketClient(String serverAddress, int serverPort) throws IOException {
        logger.debug("Client()");
        socket = new Socket(serverAddress, serverPort);
    }
    public Socket getSocket() {
        return socket;
    }
}

