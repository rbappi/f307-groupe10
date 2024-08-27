package ulb.infof307.g10.network.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DatabaseAccountManager class manages the account part of the database
 */
public class DatabaseAccountManager {
    DatabaseSQLManager databaseSQLManager;
    public DatabaseAccountManager() {
    this.databaseSQLManager = DatabaseSQLManager.getInstance(); //Create a new instance of the database manager
    try {
        initiateDatabase();}
    catch(SQLException ignored) {}
    }

    /**
     * This function creates the account tables of the database
     */
    private void initiateDatabase() throws SQLException {
        //Create the table for the accounts
        DatabaseSQLManager db = DatabaseSQLManager.getInstance();

        // Create a table of Pseudokeys composed type : id, pseudo, public and private encrypted key (only the user know the key to decrypt the key)
        String[] column = {"_id", "username", "password", "token"};
        String[] values = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT NOT NULL UNIQUE", "TEXT NOT NULL", "INTEGER DEFAULT 50"};
        db.createTable("ACCOUNTS", column, values);
    }

    /**
     * This function verifies if the user's account is in the database
     * @param username Account username
     * @return Returns true if the user is in the database
     */
    public boolean checkAccount(String username) {
        ResultSet result = databaseSQLManager.select("ACCOUNTS" , new String[]{"_id", "username"} , new String[]{"username"}, new String[]{username});
        try {
            return result.next();
        }
        catch (SQLException e) {
            return false;
        }
    }

    /**
     * This function creates a new account in the database
     * @param username Account username
     * @return Returns true if the account is created
     */
    public boolean createAccount(String username, String password) {
        if (!checkAccount(username)) {
            //Create the account
            String[] columns = {"username", "password"};
            String[] values = {username, password};
            databaseSQLManager.insert("ACCOUNTS", columns, values);
            return checkAccount(username);
        }
        return false;
    }

    /**
     * This function checks if the user can be connected to the database and login the user using username and password
     * @param username Account username
     * @param password Account password
     * @return Returns true if the account is connected
     */
    public boolean login(String username, String password) throws SQLException {
        if (checkAccount(username)) {
            try {
                ResultSet result = databaseSQLManager.select("ACCOUNTS", new String[]{"_id", "username", "password"},new String[]{"username"}, new String[]{username});
                if (result.next()) {
                    if (result.getString("password").equals(password)) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        return false;
    }

    /**
     * This function changes the password's user in the database with a new one
     * @param username  Account username
     * @param oldPassword Account old password
     * @param newPassword Account new password
     * @return Returns true if the password is changed
     */
    public boolean changePassword(String username,String oldPassword,String newPassword) throws SQLException {
        if (checkAccount(username)) {
            try {
                ResultSet result = databaseSQLManager.select("ACCOUNTS", new String[]{"_id", "username", "password"}, new String[]{"username"}, new String[]{username});
                if (result.next()) {
                    if (result.getString("password").equals(oldPassword)) {
                        databaseSQLManager.update("ACCOUNTS", new String[]{"password"}, new String[]{newPassword}, "username = '" + username + "'");
                        return true;
                    }
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        return false;
    }

    /**
     * This function sets the new best score of the user's deck
     * @param deckName is the deck name
     * @param username Account username
     * @param scoreMax is the maximum score received
     * @return score is an int that is the max score
     */
   public int manageSetDeckScore(String deckName,String username,String scoreMax) {
       try {
           databaseSQLManager.update("DECKS", new String[]{"scoreMax"}, new String[]{scoreMax}, "deckName = '"+ deckName + "' AND username = '" + username + "'");

           ResultSet result = databaseSQLManager.select("DECKS", new String[]{"scoreMax"}, new String[]{"deckName","username"}, new String[]{deckName,username});
           if (result.next()) {
               return Integer.parseInt(result.getString("scoreMax"));
           }
           else {
               return 0;
           }
       } catch (SQLException e) {
           return -1;
       }
   }

    public int getAmountOfTokens(String username) throws SQLException {
        ResultSet result = databaseSQLManager.select("ACCOUNTS", new String[]{"token"}, new String[]{"username"}, new String[]{username});
        if (result.next()) {
            return Integer.parseInt(result.getString("token"));
        }
        else {
            throw new SQLException("Not enough tokens");
        }
    }

    public void setAmountOfTokens(String username, int newValueOfTaken) {
        databaseSQLManager.update("ACCOUNTS", new String[]{"token"}, new String[]{Integer.toString(newValueOfTaken)}, "username = '" + username + "'");
    }
}
