package ulb.infof307.g10.network.packet;

/**
 * The Packet class is the way the information is stocked to
 * transit between the Client and Server
 * The format is the following: [(int) request, (string) content]
 * If more than one item need to be added in a packet, add ";" in the content to separate them
 * Example: Packet p = new Packet(1, "Jean;12345")
 * If you want to modify the behaviour notably to do it in JSON,
 * feel free to do. Check the class PacketHandler in this case
 */
public class Packet {
    private final int request;
    private final String content;

    public Packet(int request, String content) {
        this.request = request;
        this.content = content;
    }
    public int getRequest() { return request; }
    public String getContent() {return content;}
}
