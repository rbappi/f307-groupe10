package ulb.infof307.g10.network.database;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseCardManagerTest {
    DatabaseCardManager dbCM = new DatabaseCardManager();

    @Test
    void getCardFromCategoryTest() throws SQLException {
        String[][] cardList = dbCM.getCardFromCategory("0", "category1");
        assertEquals(0, cardList.length);
    }

    @Test
    void createCardTest() throws SQLException {
        String[] card = {"user", "deckName", "0", "0", "0", "0", "0"};
        boolean create = dbCM.createCard(card[0], card[1], card[2], card[3], card[4], card[5], card[6]);
        assertTrue(create);
        String[][] cards = dbCM.getCards("user", "deckName");
        boolean delete = dbCM.deleteCards(Integer.parseInt(cards[0][0]));
        assertTrue(delete);
    }

    @Test
    void updateCardTest() throws SQLException {
        String[] card = {"user", "deckName", "0", "0", "0", "0", "0"};
        String[] newCard = {"user", "deckName", "1", "1", "1", "1", "1"};
        dbCM.createCard(card[0], card[1], card[2], card[3], card[4], card[5], card[6]);
        String[][] cards = dbCM.getCards("user", "deckName");
        boolean update = dbCM.updateCard(Integer.parseInt(cards[0][0]), newCard[1], newCard[2], newCard[3], newCard[4], newCard[5]);
        assertTrue(update);
        boolean delete = dbCM.deleteCards(Integer.parseInt(cards[0][0]));
        assertTrue(delete);
    }
}