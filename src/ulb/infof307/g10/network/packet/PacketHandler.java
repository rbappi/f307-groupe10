package ulb.infof307.g10.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;

/**
 * The constructor of this class needs to be called when you want to send or receive Packets
 *  If you want to modify the behaviour notably to do it in JSON, feel free to do.
 *  In this case, you might also need to change the Packet class.
 */
public class PacketHandler {
    Parse parser = new Parse();
    Socket socketClient;

    public PacketHandler(Socket socket) {
        socketClient = socket;

    }

    /**
     * This function sends messages to users and transform them into packet
     * Currently, the format of a packet is [(int) request, (string) content]
     * We have different requests type
     * Example: Packet p = new Packet(LOGIN, "Jean;12345")
     * with LOGIN is a ConstServerRequest that equals 3
     * If more than one item need to be added in a packet, add ";" in the content to separate them
     * Example: Packet p = new Packet(1, "Jean;12345")
     * @param packet is the data that is sent from client to server or the opposite
     * @return Returns true if the packet is correctly sent
     */
    public boolean sendPacket(Packet packet){
        try {
            // Send the packet to the server
            DataOutputStream outputStream = new DataOutputStream(socketClient.getOutputStream());
            String messageToSend = packet.getRequest() + ";" + packet.getContent();
            outputStream.writeUTF(messageToSend);
            outputStream.flush();
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    /**
     * This function receive messages from users and transform them into packet
     * Currently, the format of a packet is [(int) request, (string) content]
     * If more than one item need to be added in a packet, add ";" in the content to separate them
     * Example: Packet p = new Packet(1, "Jean;12345")
     * @return packet is the data that is sent from client to server or the opposite
     */
    public Packet receivePacket(){
        try {
            DataInputStream message = new DataInputStream(socketClient.getInputStream());
            return inputToPacket(message.readUTF());
        } catch (IOException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.CONNECTION_ERROR);
        }
    }

    /**
     * This function transforms a DataInputStream object to a Packet Object
     * Watch out, the message received needs to be "RequestContent". Ex: "2;Jean;12345"
     * @param message is a str message that is sent from client to server or the opposite
     * @return Packet is the data that is sent from client to server or the opposite
     */
    Packet inputToPacket(String message){
        String[] parts = message.split(";", 2);
        int request;

        try {
            request = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            request = ConstServerRequest.FAILURE;
            return new Packet(request, ConstServerError.REQUEST_FORMAT_INVALID);
        }

        String content;
        if (parts.length < 2 || parts[1].isEmpty()) {
            content = ConstServerError.CONTENT_EMPTY;
        } else {
            content = parts[1];
        }
        return new Packet(request, content);
    }

    public String[] parsePacket(Packet packet) {
        return parser.parse(packet.getContent());
    }
}

