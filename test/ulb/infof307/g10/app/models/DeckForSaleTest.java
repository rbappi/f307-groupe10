package ulb.infof307.g10.app.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeckForSaleTest {

    @Test
    void testSetId() {
        DeckForSale deckForSale = new DeckForSale(1, "deckName", "sellerUsername", 10, true);
        deckForSale.setId(2);
        Assertions.assertEquals(2, deckForSale.getId());
    }
    @Test
    void testGetId() {
        DeckForSale deckForSale = new DeckForSale(1, "deckName", "sellerUsername", 10, true);
        Assertions.assertEquals(1, deckForSale.getId());
    }
    @Test
    void testGetDeckName() {
        DeckForSale deckForSale = new DeckForSale(1, "deckName", "sellerUsername", 10, true);
        Assertions.assertEquals("deckName", deckForSale.getDeckName());
    }
    @Test
    void testGetSellerUsername() {
        DeckForSale deckForSale = new DeckForSale(1, "deckName", "sellerUsername", 10, true);
        Assertions.assertEquals("sellerUsername", deckForSale.getSellerUsername());
    }
    @Test
    void testGetPrice() {
        DeckForSale deckForSale = new DeckForSale(1, "deckName", "sellerUsername", 10, true);
        Assertions.assertEquals(10, deckForSale.getPrice());
    }
    @Test
    void testGetInStore() {
        DeckForSale deckForSale = new DeckForSale(1, "deckName", "sellerUsername", 10, true);
        Assertions.assertTrue(deckForSale.isInStore());
    }
}
