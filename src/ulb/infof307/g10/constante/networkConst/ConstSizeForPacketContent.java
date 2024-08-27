package ulb.infof307.g10.constante.networkConst;

public final class ConstSizeForPacketContent {
    private ConstSizeForPacketContent() {
        // restrict instantiation
    }
    public static final int ACCOUNT_CREATION_SIZE = 2;
    public static final int LOGIN_SIZE = 2;
    public static final int CHANGING_PASSWORD_SIZE = 3;
    public static final int CREATE_DECK_SIZE = 3;
    public static final int CREATE_CATEGORY_SIZE = 3;
    public static final int CREATE_CARD_SIZE = 7;
    public static final int DELETE_DECK_SIZE = 2;
    public static final int DELETE_CATEGORY_SIZE = 2;
    public static final int DELETE_CARD_SIZE = 1;
    public static final int GET_DECK_SIZE = 1;
    public static final int GET_CARD_SIZE = 2;
    public static final int GET_CATEGORIES_DECK_SIZE = 2;
    public static final int GET_CATEGORIES_CARD_SIZE = 2;
    public static final int MODIFIED_CARD_SIZE = 6;
    public static final int GET_AMOUNT_TOKENS_SIZE = 1;
    public static final int UPDATE_AMOUNT_TOKENS_SIZE = 2;
    public static final int GET_DECK_IN_STORE_SIZE = 1;
    public static final int BUY_DECK_SIZE = 3;
    public static final int GET_CARD_COUNT_IN_DECK_SIZE = 2;
    public static final int UPDATE_STORE_SIZE = 4;
    public static final int SET_USER_DECK_SCORE_SIZE = 3;

}
