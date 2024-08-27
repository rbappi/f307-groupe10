package ulb.infof307.g10.constante.networkConst;

public final class ConstServerRequest {
    /****
     * Write the new types of request you want to add below
     *****/
    private ConstServerRequest() {
        // restrict instantiation
    }

    public static final int FAILURE = -1;
    public static final int EMPTY_REQUEST = 0;
    public static final int SUCCESS = 1;
    public static final int ACCOUNT_CREATION = 2;
    public static final int LOGIN = 3;
    public static final int CHANGING_PASSWORD = 4;
    public static final int CREATE_DECK = 5;
    public static final int CREATE_CATEGORY = 6;
    public static final int CREATE_CARD = 7;
    public static final int DELETE_DECK = 8;
    public static final int DELETE_CATEGORY = 9;
    public static final int DELETE_CARD = 10;
    public static final int GET_DECK = 11;
    public static final int GET_CARD = 12;
    public static final int GET_CATEGORIES_DECK = 13;
    public static final int GET_CATEGORIES_CARD = 14;
    public static final int MODIFIED_CARD = 15;
    public static final int GET_AMOUNT_TOKENS = 16;
    public static final int UPDATE_AMOUNT_TOKENS = 17;
    public static final int GET_DECK_IN_STORE = 18;
    public static final int BUY_DECK = 19;
    public static final int GET_CARD_COUNT_IN_DECK = 21;
    public static final int UPDATE_STORE = 23;
    public static final int SET_USER_DECK_SCORE = 24;


}