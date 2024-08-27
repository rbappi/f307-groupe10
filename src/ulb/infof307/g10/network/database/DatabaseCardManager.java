package ulb.infof307.g10.network.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DatabaseCardManager class manages cards information in the database
 * @see ulb.infof307.g10.network.database.DatabaseManager
 */
public class DatabaseCardManager extends DatabaseManager{
    DatabaseSQLManager databaseSqlManager =  this.databaseSQLManager;


    /**
     * This function deletes the card with _id
     * @param _id is the id of a card
     * @return Return true if the card is correctly deleted
     */
    public boolean deleteCards(int _id) throws SQLException{
        databaseSqlManager.delete(
                "CARDS",
                new String[]{"_id"},
                new String[]{String.valueOf(_id)}
        );
        return !checkCard(_id);
    }

    /**
     * This function checks if there is a card in database with _id
     * @param _id is the id of a card
     * @return Return true if the card is checked
     */
    public boolean checkCard(int _id) {
        ResultSet result = databaseSqlManager.select(
                "CARDS",
                new String[]{"_id"},
                new String[]{"_id"},
                new String[]{String.valueOf(_id)}
        );
        try {
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This function returns a list of cards information and their position by using categories
     * @param username Account username
     * @param categoryName Category name
     * @return Returns list of cards information and their position
     */
    public String[][] getCardFromCategory(String username, String categoryName) throws SQLException {
        ResultSet result = databaseSqlManager.select(
                "CATEGORY",
                new String[]{"_id", "deckName", "categoryName", "username"},
                new String[]{"username", "categoryName"},
                new String[]{username, categoryName}
        );
        String[][] cardInfo = new String[0][0];
        try {
            while (result.next()) {
                cardInfo = appendPositionAndListElement(cardInfo, getCards(username, result.getString("deckName")));
            }
        } catch (SQLException e) {
            throw e;
        }
        return cardInfo;
    }

    /**
     * This function creates a card based on given parameters
     * @param username Account username
     * @param deckName Deck name
     * @param category Category name
     * @param question Question posed
     * @param response Answer get it
     * @param evaluation Score received
     * @param typeOfCard Type of card (normal, LaTeX, HTML)
     * @return Return true if the card is correctly created
     */
    public boolean createCard(String username, String deckName, String category, String question, String response, String evaluation, String typeOfCard) {
        if (!checkCard(username, deckName, category, question, response, evaluation, typeOfCard)) {
            String[] columns = {"username", "deckName", "category", "question", "response","evaluation", "typeOfCard"};
            String[] values = {username, deckName, category, question, response, evaluation, typeOfCard};
            databaseSqlManager.insert("CARDS", columns, values);
            return checkCard(username, deckName, category, question, response, evaluation, typeOfCard);
        }
        return false;
    }

    /**
     * This function checks a card based on given parameters
     * @param username Account username
     * @param deckName Deck name
     * @param category Category name
     * @param question Question asked
     * @param response Answer received
     * @param evaluation Score received
     * @param typeOfCard Type of card (normal, LaTeX, HTML)
     * @return Return true if the card exists in the database
     */
    public boolean checkCard(String username, String deckName, String category, String question, String response, String evaluation, String typeOfCard) {
        ResultSet result = databaseSqlManager.select(
                "CARDS",
                new String[]{"_id", "username", "deckName", "category", "question", "response", "evaluation", "typeOfCard"},
                new String[]{"username", "deckName", "category", "question", "response", "evaluation", "typeOfCard"},
                new String[]{username, deckName, category, question, response, evaluation, typeOfCard}
        );

        try {
            return result != null && result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * @param _id is the id of a card
     * @param deckName Deck name
     * @param category Category name
     * @param newQuestion New question asked
     * @param newResponse New answer received
     * @param newEvaluation New score received
     * @return Return true if the card has been updated in the database
     */
    public boolean updateCard(int _id, String deckName, String category, String newQuestion, String newResponse, String newEvaluation) {
        if (checkCard(_id)) {
            String[] columns = {"deckName", "category", "question", "response", "evaluation"};
            String[] values = {deckName, category, newQuestion, newResponse, newEvaluation};
            databaseSqlManager.update("CARDS", columns, values, new String[]{"_id"}, new String[]{Integer.toString(_id)});
            return true;
        }
        return false;
    }

}
