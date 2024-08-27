package ulb.infof307.g10.app.controllers;

import org.junit.jupiter.api.Test;
import ulb.infof307.g10.app.models.DeckForSale;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreControllerTest {

    private Socket initSocket() {
        return Mockito.mock(Socket.class);  // to mock a socket without real connection to the server
    }

    @Test
    void completeDeckToBuyTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacketController pc = new PacketController(initSocket());
        WindowController wc = new WindowController(null);
        StoreController storeController = new StoreController(pc, wc);
        Method completeDeckToBuyMethod = StoreController.class.getDeclaredMethod("completeDeckToBuy", String[][].class);
        completeDeckToBuyMethod.setAccessible(true);

        String[][] deckToSendStr = {{"0", "deckname1", "Shan", "1", "true"}, {"1", "deckname2", "Tiago", "1", "true"}};
        List<DeckForSale> dfs = (List<DeckForSale>) completeDeckToBuyMethod.invoke(storeController, (Object) deckToSendStr);
        int count = 0;
        for(DeckForSale df : dfs) {
            if(count == 0) {
                assertEquals(0, df.getId());
                assertEquals("deckname1", df.getDeckName());
                assertEquals("Shan", df.getSellerUsername());
                assertEquals(1, df.getPrice());
            }
            else {
                assertEquals(1, df.getId());
                assertEquals("deckname2", df.getDeckName());
                assertEquals("Tiago", df.getSellerUsername());
                assertEquals(1, df.getPrice());

            }
            count += 1;
        }
    }
}