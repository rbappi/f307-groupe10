package ulb.infof307.g10.network.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * DatabaseManager is used to create the database used by our application
 * This class implements DatabaseDeckManager, DatabaseCardManager, DatabaseStoreManager
 */
public class DatabaseManager {

    protected final DatabaseSQLManager databaseSQLManager;

    public DatabaseManager() {
        this.databaseSQLManager = DatabaseSQLManager.getInstance();
        try {
           initiateDatabase();}
        catch(SQLException ignored) {

        }

    }

    /**
     * This function creates the tables of the database
     */
    private void initiateDatabase() throws SQLException {
        databaseSQLManager.createTable("CARDS",new String[]{"_id","deckName","username","category","question","response","evaluation","typeOfCard"},new String[]{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT","TEXT","TEXT","TEXT"});
        databaseSQLManager.createTable("DECKS",new String[]{"_id","deckName","username","scoreMax"},new String[]{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT"});
        databaseSQLManager.createTable("CATEGORY",new String[]{"_id","deckName","categoryName","username"},new String[]{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT"});
        databaseSQLManager.createTable("STORE",new String[]{"_id", "deckName", "username", "price", "inStore"},new String[]{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT"});
        databaseSQLManager.createTable("DECKPURCHASEHISTORY",new String[]{"_id","deckName","sellerUsername","buyerUsername","price","timestamp"},new String[]{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","TEXT","TEXT","TEXT","TEXT"});
    }

    /**
     * This function is used to add element to a vector
     * @param vector is a list of data
     * @param element is string of information
     * @return vector is a list of data
     */
    public String[] appendOneElement(String[] vector, String element){
        String[] newResult = Arrays.copyOf(vector, vector.length + 1);
        newResult[newResult.length - 1] = element;
        return newResult;
    }

    /**
     * This function is used to add a list of elements to a table
     * @param table is a table of information in the database
     * @param element is a list of information
     * @return newTable is the modified table
     */
    public  String[][] addListOfElement(String[][] table, String[] element) {
        String[][] newTable = Arrays.copyOf(table, table.length + 1);
        newTable[newTable.length - 1] = element;
        return newTable;
    }

    /**
     * This function is used to add a position and a list of elements to a table
     * @param table is a table of information in the database
     * @param elements is the position and a list of information
     * @return newTable is the modified table
     */
    public static String[][] appendPositionAndListElement(String[][] table, String[][] elements) {
        String[][] newTable = Arrays.copyOf(table, table.length + elements.length);
        System.arraycopy(elements, 0, newTable, table.length, elements.length);
        return newTable;
    }

    /**
     * This function verifies if the user possesses the deck in his database
     * @param username Account username
     * @param deckName Name of the deck
     * @return Return true if the selected deck is in the database
     */
    public boolean checkDeck(String username, String deckName) throws SQLException {
        ResultSet result = databaseSQLManager.select("DECKS", new String[]{"_id", "username", "deckName"}, new String[]{"username","deckName"},new String[]{username,deckName});
        try {
            return result.next();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * This function is used to get card(s) from a user's deck
     * @param username Account username
     * @param deckName Name of the deck
     * @return cardsInfo is a list of card(s) in the deck
     */
    public String[][] getCards(String username, String deckName) throws SQLException {
        ResultSet result = databaseSQLManager.select(
                "CARDS",
                new String[]{"_id", "deckName", "username", "category", "question", "response", "evaluation", "typeOfCard"},
                new String[]{"username", "deckName"},
                new String[]{username, deckName}
        );
        String[][] cardsInfo = new String[0][0];
        try {
            while (result.next()) {
                cardsInfo = addListOfElement(cardsInfo, new String[]{result.getString("_id"), result.getString("category"), result.getString("question"), result.getString("response"), result.getString("evaluation"), result.getString("typeOfCard")});
            }
        } catch (SQLException e) {
            throw e;
        }
        return cardsInfo;
    }

    /**
     * This function is used to get category(ies) from a user's deck
     * @param username Account username
     * @param deckName Name of the deck
     * @return categoryInfo is a list of categories
     */
    public String[] getCategory(String username, String deckName) throws SQLException {
        String[] categoryInfo = new String[0];
        try {
            ResultSet resultSet = databaseSQLManager.select(
                    "CATEGORY",
                    new String[]{"_id", "deckName", "categoryName", "username"},
                    new String[]{"username", "deckName"},
                    new String[]{username, deckName}
            );
            while (resultSet.next()) {
                categoryInfo = appendOneElement(categoryInfo, resultSet.getString("categoryName"));
            }
        } catch (SQLException e) {
            throw e;
        }
        return categoryInfo;
    }
}
