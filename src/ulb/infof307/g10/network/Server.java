package ulb.infof307.g10.network;

import java.io.*;
import java.net.*;

import ulb.infof307.g10.network.connection.ServerClientDBConnection;


/**
 * This class creates and launches server activities.
 * The server listens to any new client Connection and allows multiClient to connect via multiThreading
 * WATCH OUT! You cannot receive/send packets in this class, and therefore you cannot manage client interactions.
 * If it is what you are looking for, please scroll below to find the ClientHandler class
 * Write "javac Server.java" to compile and "java Server" to execute the program.
 */
public class Server {
    private ServerSocket serverSocket;
    private final int port;
    private boolean isStopped = false;

    public Server(int port) {
        this.port = port;
    }

    /**
     * This function needs to be called to run the server
     * This function catches client connections and separate every client in a different thread
     */
    public void runServer() {
        openServerSocket();

        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
                String connectionServerMsg = "Client " + clientSocket + " has been connected.";
                System.out.println(connectionServerMsg);

                Thread thread = new Thread(new ServerClientDBConnection(clientSocket));
                thread.start();

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client Connection", e);
            }
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     * Initialise the server
     */
    private void openServerSocket() {

        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.runServer();
    }
}
