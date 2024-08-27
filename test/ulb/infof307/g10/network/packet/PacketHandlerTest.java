package ulb.infof307.g10.network.packet;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;
import java.net.Socket;
import org.mockito.Mockito;
import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;

class PacketHandlerTest {
    private Socket InitSocket(){
        return Mockito.mock(Socket.class);
    }

    @Test
    void testInputToPacket1 () throws NoSuchMethodException{
        Method requestHandlerMethod = PacketHandler.class.getDeclaredMethod("inputToPacket", String.class);
        requestHandlerMethod.setAccessible(true);
        String input = "1;Shan;123";
        PacketHandler packetHandler = new PacketHandler(InitSocket());
        Packet packet = packetHandler.inputToPacket(input);
        assertEquals(1, packet.getRequest());
        assertEquals("Shan;123", packet.getContent());
    }

    @Test
    void testInputToPacket2 () throws NoSuchMethodException{
        Method requestHandlerMethod = PacketHandler.class.getDeclaredMethod("inputToPacket", String.class);
        requestHandlerMethod.setAccessible(true);
        String input = "Hello;Shan;123";
        PacketHandler packetHandler = new PacketHandler(InitSocket());
        Packet packet = packetHandler.inputToPacket(input);
        assertEquals(ConstServerRequest.FAILURE, packet.getRequest());
        assertEquals(ConstServerError.REQUEST_FORMAT_INVALID, packet.getContent());
    }

    @Test
    void testInputToPacket3 () throws NoSuchMethodException {
        Method requestHandlerMethod = PacketHandler.class.getDeclaredMethod("inputToPacket", String.class);
        requestHandlerMethod.setAccessible(true);
        String input = "2;";
        PacketHandler packetHandler = new PacketHandler(InitSocket());
        Packet packet = packetHandler.inputToPacket(input);
        assertEquals(2, packet.getRequest());
        assertEquals(ConstServerError.CONTENT_EMPTY, packet.getContent());
    }
}