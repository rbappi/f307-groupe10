package ulb.infof307.g10.network.connection;

import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;
import ulb.infof307.g10.constante.networkConst.ConstServerSuccess;
import ulb.infof307.g10.network.database.DatabaseAccountManager;
import ulb.infof307.g10.network.packet.Packet;

import java.sql.SQLException;

/**
 * This class provides methods for interacting with the account database.
 * It handles actions such as account creation, login, password changes and managing tokens.
 */
public class DBAccountInteraction extends DBInteraction{
    private final DatabaseAccountManager databaseAccountManager = new DatabaseAccountManager();
    /**
     * Constructor for the DBAccountInteraction class.
     *
     * @param content The data that will be processed by the parser of DBAccountInteraction.
     */
    DBAccountInteraction(String content) {
        this.content = parser.parse(content);
    }
    /**
     * Manages the account creation request from the client.
     *
     * @return Packet Contains the result of the account creation attempt.
     */
    public Packet manageAccountCreation() {
        String username = content[0];
        String password = content[1];
        if (databaseAccountManager.checkAccount(username)) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ACCOUNT_EXIST);
        }
        boolean stateResult = databaseAccountManager.createAccount(username, password);
        return getStatePacket(stateResult, ConstServerSuccess.ACCOUNT_CREATED, ConstServerError.ACCOUNT_DATABASE);
    }
    /**
     * Manages the login request from the client.
     *
     * @return Packet Contains the result of the login attempt.
     */
    public Packet manageLogin() {
        String username = content[0];
        String password = content[1];
        if(!databaseAccountManager.checkAccount(username)) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ACCOUNT_NOT_EXIST);
        }
        try {
            boolean stateResult = databaseAccountManager.login(username, password);
            return getStatePacket(stateResult, ConstServerSuccess.LOGIN_SUCCESS, ConstServerError.PASSWORD);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }
    /**
     * Manages the password change request from the client.
     *
     * @return Packet Contains the result of the password change attempt.
     */
    public Packet managePassword() {
        String username = content[0];
        String oldPassword = content[1];
        String newPassword = content[2];
        try {
            boolean stateResult = databaseAccountManager.changePassword(username, oldPassword, newPassword);
            return getStatePacket(stateResult, ConstServerSuccess.PASSWORD_MODIFIED, ConstServerError.PASSWORD);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the request from the client to get the amount of tokens.
     *
     * @return Packet Contains the amount of tokens or an error message.
     */
    public Packet manageGetAmountTokens() {
        String username = content[0];
        try {
            int tokenAmount = databaseAccountManager.getAmountOfTokens(username);
            return new Packet(ConstServerRequest.SUCCESS, Integer.toString(tokenAmount));
        } catch(Exception e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.TOKEN_RECEIVED_ERROR);
        }
    }
    /**
     * Manages the score setting for a given deck.
     *
     * @return Packet Contains the received score or an error message.
     */
    public Packet manageSetDeckScore(){
        String deckName = content[0];
        String username = content[1];
        String scoreMax  = content[2];
        try {
            int scoreReceived = databaseAccountManager.manageSetDeckScore(deckName, username,scoreMax);
            return new Packet(ConstServerRequest.SUCCESS, Integer.toString(scoreReceived));
        }
        catch(Exception e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.SCOREMAX_RECEIVED_ERROR);
        }


    }
    /**
     * Manages the update of the amount of tokens for a user.
     *
     * @return Packet Contains a success message or an error message.
     */
    public Packet manageUpdateAmountTokens() {
        String username = content[0];
        int newTokenAmount = Integer.parseInt(content[1]);
        try {
            databaseAccountManager.setAmountOfTokens(username, newTokenAmount);
            return new Packet(ConstServerRequest.SUCCESS, ConstServerSuccess.TOKEN_UPDATED);
        } catch(Exception e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.TOKEN_UPDATED_ERROR);
        }
    }
}