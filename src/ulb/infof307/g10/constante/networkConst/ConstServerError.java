package ulb.infof307.g10.constante.networkConst;

public final class ConstServerError {
    /****
     * Write the new types of error you want to add below
     *****/
    private ConstServerError() {
        // restrict instantiation
    }

    public static final String ACCOUNT_EXIST = "This account already existed";
    public static final String ACCOUNT_NOT_EXIST = "This account does not exist";
    public static final String ACCOUNT_DATABASE = "Error in initializing in the database";
    public static final String PASSWORD = "Passwords do not match";

    public static final String CARD_EXIST = "This card already exist";
    public static final String CARD_NOT_EXIST= "This card does not exist";

    public static final String CATEGORY_EXIST = "This category already exist";
    public static final String CATEGORY_NOT_EXIST= "This category does not exist";

    public static final String DECK_EXIST = "This deck already exist";
    public static final String DECK_NOT_EXIST = "This deck does not exist";

    public static final String CONTENT_EMPTY = "Content is empty";
    public static final String REQUEST_FORMAT_INVALID = "The reception of the request is erroneous";
    public static final String CONNECTION_ERROR = "Connection Error";
    public static final String TOKEN_RECEIVED_ERROR = "Unable to get the tokens from database";
    public static final String TOKEN_UPDATED_ERROR = "There has been an error while retrieving the amount of token";
    public static final String STORE_UPDATE_ERROR = "There has been an error while updating the store";
    public static final String SCOREMAX_RECEIVED_ERROR = "Unable to get the scoreMax from database";
    public static final String ERROR_CONTENT_SIZE = "The content size does not match the request";
    public static final String ERROR_DATABASE = "An error occurred with the database";
}