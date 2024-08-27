package ulb.infof307.g10.network.connection;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;
import ulb.infof307.g10.network.packet.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerSocketClientDBConnectionTest {
    /*****************
     * to initialise the class server client interaction you need to use a socket
     * since we are in a test class we can not lunch the server
     * in consequence we use the class "mockito" to mock an object socket without real connection
     * .
     * to test private methods we need to use the class Method
     ****************/
    private Socket InitSocket() {
        //return new Socket("localhost",8081);
        return Mockito.mock(Socket.class);  // to mock a socket without real connection to the server

    }
    @Test
    void requestHandlertest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        ServerClientDBConnection sci = new ServerClientDBConnection(InitSocket());
        Packet packet = new Packet(901, "Shan");
        Method requestHandlerMethod = ServerClientDBConnection.class.getDeclaredMethod("requestHandler", Packet.class);
        requestHandlerMethod.setAccessible(true);
        Packet newpacket = (Packet) requestHandlerMethod.invoke(sci, packet);
        assertEquals(0, newpacket.getRequest());
        assertEquals("Your request has not been recognised", newpacket.getContent());
    }

    @Test
    void requestHandlertest2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        ServerClientDBConnection sci = new ServerClientDBConnection(InitSocket());
        Packet packet = new Packet(ConstServerRequest.CHANGING_PASSWORD, "Shan");
        Method requestHandlerMethod = ServerClientDBConnection.class.getDeclaredMethod("requestHandler", Packet.class);
        requestHandlerMethod.setAccessible(true);
        Packet newpacket = (Packet) requestHandlerMethod.invoke(sci, packet);
        assertEquals(ConstServerRequest.FAILURE, newpacket.getRequest());
        assertEquals(ConstServerError.ERROR_CONTENT_SIZE, newpacket.getContent());
    }
}