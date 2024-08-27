package ulb.infof307.g10.constante.appConst;

public final class ConstAppError {

    private ConstAppError() {
        // restrict instantiation
    }

    public static final  String DECK_EMPTY = "Cannot create a deck with empty name or category.";
    public static final  String CARD_EMPTY_FIELDS = "Cannot create a card with empty category, question or answer.  ";
    public static final  String LOGIN_EMPTY = "Cannot login with empty information.";
    public static final  String EDIT_PASSWORD_EMPTY = "All fields must be filled.";
    public static final  String EDIT_PASSWORD = "An error occurred while modifying your password.";
    public static final String CHANGE_VIEW = "An error occurred while loading a new window. You can check if there is no error in the FXML file.";
    public static final String CARD_CATEGORY = "The card category doesn't match one of the category of the deck";
    public static final String CARD_LOADING = "Cannot load your cards";
    public static final String CARD_EMPTY = "Cannot load your cards because you have no cards";
    public static final String CATEGORY_ALREADY_ADDED = "This category has been already added";
    public static final String EXPORT_DECK_PATH = "To export, field Path must be filled.";
    public static final String IMPORT_DECK = "To import, field Path must be filled.";
    public static final String WINDOW = "This window does not exist";
    public static final String CSV_ERROR = "Whoops, a problem occurred during the import or export";
    public static final String EXPORT_DECK = "A problem occurred during the export";
    public static final String FILE_CREATION = "An error occurred while writing into file";
    public static final String WEBVIEW = "Error while loading the webview";
    public static final String READ_FILE = "An error occurred while reading a file";
}


