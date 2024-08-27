package ulb.infof307.g10.network.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DatabaseStoreManager class manages store information in the database
 * @see ulb.infof307.g10.network.database.DatabaseManager
 */
public class DatabaseStoreManager extends DatabaseManager{

    DatabaseSQLManager databaseSqlManager =  this.databaseSQLManager;

    /**
     * This function returns decks of other users in store
     * @param username Account username
     * @return deckStoreInfo the information about the deck(s) in the store
     */
    public String[][] getDeckInStore(String username) {
        ResultSet result = databaseSqlManager.select(
                "STORE",
                new String[]{"_id", "deckName", "username", "price", "inStore"},
                new String[]{"inStore"},
                new String[]{"true"}
        );
        String[][] deckStoreInfo = new String[0][0];
        try {
            while (result.next()) {
                if (!result.getString("username").equals(username)) {
                    deckStoreInfo = addListOfElement(deckStoreInfo, new String[]{result.getString("_id"), result.getString("deckName"), result.getString("username"), result.getString("price"), result.getString("inStore")});
                }            }
        } catch (SQLException e) {
            e.printStackTrace();
            return deckStoreInfo;
        }
        return deckStoreInfo;
    }

    /**
     * This function checks if the user can buy a deck and buys it
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @param buyerUsername Buyer account username
     * @return Returns true if the deck is bought
     */
    public boolean buyDeck(String deckName,String sellerUsername,String buyerUsername) throws SQLException {

        if (!checkDeck(sellerUsername,deckName)) {
            return false;
        }
        if (isDeckPurchasedByUser(deckName,sellerUsername,buyerUsername)) {
            return false;
        }
        if (!deckInStore(deckName,sellerUsername)) {
            return false;
        }

        int price = getDeckPrice(deckName,sellerUsername);
        if (price < 0) {
            return false;
        }
        if (!purchaseWithTokens(buyerUsername,price,sellerUsername)) {
            return false;
        }

        if (!copyPurchasedDeck(deckName,sellerUsername,buyerUsername)) {
            // we refund if copy failed
            if ((!updateTokens(buyerUsername,price)) || (!updateTokens(sellerUsername,-price)))
                return false;
        }

        addDeckPurchaseToHistory(deckName, sellerUsername, buyerUsername, price);
        return true;
    }

    /**
     * This function updates purchase history in the database
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @param buyerUsername Buyer account username
     * @param price Price of the deck in the store
     */
    void addDeckPurchaseToHistory(String deckName, String sellerUsername, String buyerUsername, int price) throws SQLException{
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String[] columns = {"deckName", "sellerUsername", "buyerUsername", "price", "timestamp"};
        String[] values = {deckName, sellerUsername, buyerUsername, Integer.toString(price), timestamp};
        databaseSqlManager.insert("DECKPURCHASEHISTORY", columns, values);
        isDeckPurchasedByUser(deckName, sellerUsername, buyerUsername);
    }

    /**
     * This function copies a purchased deck.
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @param buyerUsername Buyer account username
     * @return Returns true if a copy of the deck can be purchased
     */
    boolean copyPurchasedDeck(String deckName,String sellerUsername,String buyerUsername) throws SQLException {
        String renamedDeckName = deckName + "-copyFrom-" + sellerUsername;
        String[][] cards = getCards(sellerUsername,deckName);
        for (String[] card : cards) {
            String[] columns = {"deckName", "username", "category", "question", "response", "evaluation", "typeOfCard"};
            String[] values = {renamedDeckName, buyerUsername, card[1], card[2], card[3], "0", card[5]};
            databaseSqlManager.insert("CARDS", columns, values);
        }
        String[] categories = getCategory(sellerUsername,deckName);
        for (String category : categories) {
            String[] columns = {"deckName","categoryName","username"};
            String[] values = {renamedDeckName,category,buyerUsername};
            databaseSqlManager.insert("CATEGORY", columns, values);
        }
        String[] columns = {"deckName", "username", "scoreMax"};
        String[] values = {renamedDeckName, buyerUsername, "0"};
        databaseSqlManager.insert("DECKS", columns, values);
        return true;
    }

    /**
     * This function refunds tokens to the user
     * @param username Account username
     * @param tokens Account number of token(s)
     * @return Returns true if the user's number of token has been updated
     */
    boolean updateTokens(String username, int tokens) throws SQLException {
        int currentTokens;
        ResultSet result = databaseSqlManager.select(
                "ACCOUNTS",
                new String[] {"token"},
                new String[] {"username"},
                new String[] {username}
        );
        try {
            if (result.next()) {
                currentTokens = result.getInt("token");
                databaseSqlManager.update(
                        "ACCOUNTS",
                        new String[] {"token"},
                        new String[] {String.valueOf(currentTokens + tokens)},
                        new String[] {"username"},
                        new String[] {username}
                );
                return true;
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    /**
     * This function performs the transaction from buyer to seller
     * @param sellerUsername Seller account username
     * @param buyerUsername Buyer account username
     * @param price Price of the deck in the store
     * @return Returns true if the transaction has been performed
     */
    boolean purchaseWithTokens(String buyerUsername, int price, String sellerUsername) throws SQLException {
        ResultSet result = databaseSqlManager.select(
                "ACCOUNTS",
                new String[] {"token"},
                new String[] {"username"},
                new String[] {buyerUsername}
        );
        if (result.next()) {
            int token = result.getInt("token");
            if (token >= price) {
                updateTokens(buyerUsername, -price);
                updateTokens(sellerUsername, price);
                return true;
            }
        }
        return false;
    }

    int getDeckPrice(String deckName, String sellerUsername) {
        ResultSet result = databaseSqlManager.select(
                "STORE",
                new String[] {"price"},
                new String[] {"deckName", "username"},
                new String[] {deckName, sellerUsername}
        );
        try {
            if (result.next()) {
                return result.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    /**
     * This function checks if the deck is purchased by the user
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @param buyerUsername Buyer account username
     * @return Returns true if the deck correctly purchased by the user
     */
    public boolean isDeckPurchasedByUser(String deckName, String sellerUsername, String buyerUsername) throws SQLException {
        ResultSet result = databaseSqlManager.select(
                "DECKPURCHASEHISTORY",
                new String[] {"_id", "deckName", "sellerUsername", "buyerUsername"},
                new String[] {"deckName", "sellerUsername", "buyerUsername"},
                new String[] {deckName, sellerUsername, buyerUsername}
        );
        return result.next();
    }

    /**
     * This function add status (inStore) of the deck in the store
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @return Returns true if the status (inStore) is added to the deck in the store
     */
    public boolean deckInStore(String deckName, String sellerUsername) {
        ResultSet result = databaseSqlManager.select(
                "STORE",
                new String[] {"_id", "deckName", "username", "inStore"},
                new String[] {"deckName", "username", "inStore"},
                new String[] {deckName, sellerUsername, "true"}
        );
        try {
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This function checks if the deck of the seller is in the store
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @param inStore Status of the deck in the store
     * @return Returns true if the deck of the seller is in the store
     */
    public boolean isDeckInStore(String deckName, String sellerUsername, boolean inStore){
        ResultSet result = databaseSqlManager.select("STORE",
                new String[]{"_id", "deckName", "username","inStore"},
                new String[]{"deckName","username","inStore"},
                new String[]{deckName, sellerUsername, Boolean.toString(inStore)});
        try {
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * This function checks if deck is in the store with a price
     * @param deckName Deck name
     * @param sellerUsername Seller account username
     * @param price Price of the deck in the store
     * @param inStore Status of the deck in the store
     * @return Returns true if the deck has a price
     */
    public boolean hasDeckInStorePrice(String deckName, String sellerUsername, int price, boolean inStore){
        ResultSet result = databaseSqlManager.select("STORE",
                new String[]{"_id", "deckName", "username","inStore","price"},
                new String[]{"deckName","username","inStore","price"},
                new String[]{deckName,sellerUsername,Boolean.toString(inStore), Integer.toString(price)} );
        try {
            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }


    /**
     * This function updates the STORE table
     * @param deckName Deck name
     * @param username Account username
     * @param price Price of the deck in the store
     * @param inStore Status of the deck in the store
     * @return Returns true if the STORE table has correctly been updated
     */
    public boolean hasStoreUpdated(String deckName, String username, int price, boolean inStore){
        String[] columns = {"deckName", "username", "price", "inStore"};
        String[] values = {deckName,username, Integer.toString(price), Boolean.toString(inStore)};
        if(!(isDeckInStore(deckName,username, inStore))){
            databaseSqlManager.insert("STORE", columns, values);
            return (isDeckInStore(deckName,username,inStore));
        }
        databaseSqlManager.update("STORE", columns, values, new String[]{"deckName", "username"}, new String[]{deckName, username});
        return (hasDeckInStorePrice(deckName,username,price,inStore));
    }
}
