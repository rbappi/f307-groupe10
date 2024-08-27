package ulb.infof307.g10.app.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ulb.infof307.g10.constante.networkConst.ConstServerSuccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class PacketControllerTest {

    private Socket initSocket() {
        return Mockito.mock(Socket.class);  // to mock a socket without real connection to the server
    }

    @Test
    void checkNewCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacketController pc = new PacketController(initSocket());
        Method packetControllerMethod = PacketController.class.getDeclaredMethod("checkNewCard", String.class);
        packetControllerMethod.setAccessible(true);
        boolean answer = (boolean) packetControllerMethod.invoke(pc, ConstServerSuccess.CARD_CREATED);
        assertTrue(answer);

    }

    @Test
    void checkLoginInfoTest1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacketController pc = new PacketController(initSocket());
        Method packetControllerMethod = PacketController.class.getDeclaredMethod("checkLoginInfo", String.class, String.class);
        packetControllerMethod.setAccessible(true);
        boolean answer = (boolean) packetControllerMethod.invoke(pc, "Bob", ConstServerSuccess.LOGIN_SUCCESS);
        assertTrue(answer);

    }

    @Test
    void checkLoginInfoTest2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacketController pc = new PacketController(initSocket());
        Method packetControllerMethod = PacketController.class.getDeclaredMethod("checkLoginInfo", String.class, String.class);
        packetControllerMethod.setAccessible(true);
        boolean answer = (boolean) packetControllerMethod.invoke(pc, "Bob", ConstServerSuccess.HAS_STORE_UPDATED);
        assertFalse(answer);

    }
}