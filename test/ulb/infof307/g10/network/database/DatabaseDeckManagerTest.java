package ulb.infof307.g10.network.database;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseDeckManagerTest {
    DatabaseDeckManager dbDM = new DatabaseDeckManager();

    @Test
    void createDeckTest1() {
        try {
            boolean res = dbDM.createDeck("0", "deckname1", "0");
            assertTrue(res);
            dbDM.deleteDecks("0", "deckname1");
        }

        catch(SQLException e) {

        }
    }

    @Test
    void createDeckTest2() {
        try {
            dbDM.createDeck("0", "deckname1", "0");
            boolean res = dbDM.createDeck("0", "deckname1", "0");
            assertFalse(res);
            dbDM.deleteDecks("0", "deckname1");
        }

        catch(SQLException e) {

        }
    }

    @Test
    void createCategoryTest() {
        boolean res = dbDM.createCategory("0", "deckname1", "category1");
        assertTrue(res);
        try {
        dbDM.deleteCategory("0", "category1");}
        catch(SQLException ignored) {}
    }

}