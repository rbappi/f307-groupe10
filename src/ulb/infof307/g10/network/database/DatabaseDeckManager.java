package ulb.infof307.g10.network.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DatabaseDeckManager class manages decks information in the database
 * @see ulb.infof307.g10.network.database.DatabaseManager
 */
public class DatabaseDeckManager extends DatabaseManager{
    DatabaseSQLManager databaseSqlManager =  this.databaseSQLManager;

    /**
     * This function deletes the given deck
     * @param username Account username
     * @param deckName Deck name
     * @return Returns true if the deck is correctly deleted
     */
    public boolean deleteDecks(String username,String deckName) throws SQLException {
        databaseSqlManager.delete("DECKS", new String[]{"username","deckName"},new String[]{username,deckName});
        return !checkDeck(username, deckName);

    }

    /**
     * This function checks if the deck user has at least a one category
     * @param username Account username
     * @param categoryName Category name
     * @return Returns true if there is category for the user's deck
     */
    public boolean checkCategory(String username, String categoryName) {
        ResultSet result = databaseSqlManager.select(
                "CATEGORY",
                new String[]{"_id", "deckName", "categoryName", "username"},
                new String[]{"username", "categoryName"},
                new String[]{username, categoryName}
        );
        try {
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This function deletes a category from the user's deck
     * @param username Account username
     * @param categoryName Category name
     * @return Returns false if there is category for the deck
     */
    public boolean deleteCategory(String username, String categoryName) throws SQLException{
        databaseSqlManager.delete(
                "CATEGORY",
                new String[]{"username", "categoryName"},
                new String[]{username, categoryName}
        );
        return !checkCategory(username, categoryName);
    }

    public String[][] getDeck(String username) throws SQLException {
        ResultSet result = databaseSqlManager.select(
                "DECKS",
                new String[]{"_id", "username", "deckName", "scoreMax"},
                new String[]{"username"},
                new String[]{username}
        );
        String[][] deckInfo = new String[0][0];
        while (result.next()) {
            deckInfo = addListOfElement(deckInfo, new String[]{result.getString("_id"), result.getString("deckName"), result.getString("scoreMax")});
        }
        return deckInfo;
    }

    /**
     * This function creates a new category for a deck
     * @param username Account username
     * @param categoryName Category name
     * @param deckName Deck name
     * @return Returns true if there is a new category created
     */
    public boolean createCategory(String username, String deckName, String categoryName) {
        if (!checkCategory(username, deckName, categoryName)) {
            String[] columns = {"username", "deckName", "categoryName"};
            String[] values = {username, deckName, categoryName};
            databaseSqlManager.insert("CATEGORY", columns, values);
            return checkCategory(username, deckName, categoryName);
        }
        return false;
    }

    /**
     * This function checks if the deck user has at least a one category
     * @param username Account username
     * @param categoryName Category name
     * @param deckName Deck name
     * @return Returns true if there is category for the deck and false otherwise or error
     */
    public boolean checkCategory(String username, String deckName, String categoryName) {
        ResultSet result = databaseSqlManager.select("CATEGORY", new String[]{"_id", "username", "deckName", "categoryName"}, new String[]{"username","deckName","categoryName"}, new String[]{username,deckName,categoryName});
        try {
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This function creates a new deck for the user
     * @param username Account username
     * @param deckName Deck name
     * @param score is the score of the deck
     * @return Returns true if the deck is correctly created for the user
     */
    public boolean createDeck(String username, String deckName,String score) throws SQLException {
        if (!checkDeck(username, deckName)) {
            String[] columns = {"username", "deckName","scoreMax"};
            String[] values = {username, deckName,score};
            databaseSqlManager.insert("DECKS", columns, values);
            return checkDeck(username, deckName);
        }
        return false;
    }

    public int getAmountOfCardsInDeck(String deckName, String username) throws SQLException {
        ResultSet result = databaseSqlManager.select("CARDS", new String[]{"_id","deckName","username"}, new String[]{"username","deckName"},new String[]{username,deckName});
        int count = 0;
        try {
            while (result.next()) {
                count++;
            }
        } catch (SQLException e) {
            throw e;
        }
        return count;
    }
}
