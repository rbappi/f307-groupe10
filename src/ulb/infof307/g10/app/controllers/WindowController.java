package ulb.infof307.g10.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.app.views.Card.*;
import ulb.infof307.g10.app.views.Connection.*;
import ulb.infof307.g10.app.views.Deck.*;
import ulb.infof307.g10.app.views.Store.StoreViewController;
import ulb.infof307.g10.app.views.Util.PopUp;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import ulb.infof307.g10.constante.appConst.ConstAppWindow;

import java.io.IOException;

import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

/**
 * This class controls which window is shown
 */
public class WindowController {
    ConnectionController connectionController;
    DeckController deckController;
    StoreController storeController;
    Stage stage;
    private static final Logger logger = LoggerFactory.getLogger(WindowController.class);

    WindowController(Stage stage) {
        this.stage = stage;
    }

    private void showStage() {
        logger.debug("showStage()");
        this.stage.show();
    }

    /**
     * This method is used to change the view of the application.
     * It takes an int as parameter, which is the index of the view in the views array.
     * It then loads the corresponding view and displays it.
     * @param window A number representing the widow to display
     */
    public void changeView(int window) {

        logger.debug("changeView()");
        try {
            switch (window) {
                case CONNECTION_MENU_WINDOW -> loadWindow(new ConnectionMenuController(), CONNECTION_MENU_PATH, connectionController);//0
                case LOGIN_WINDOW -> loadWindow(new LoginController(), LOGIN_PATH, connectionController);//1
                case CREATE_ACCOUNT_WINDOW -> loadWindow(new CreateAccountController(), CREATE_ACCOUNT_PATH, connectionController);//2
                case CHANGE_PASSWORD_WINDOW -> loadWindow(new ChangePasswordController(), CHANGE_PASSWORD_PATH, connectionController);//3
                case ACCOUNT_OPTIONS_MENU_WINDOW -> loadWindow(new AccountOptionsMenuController(), ACCOUNT_OPTIONS_MENU_PATH, connectionController);//4
                case MENU_DECK_WINDOW -> loadWindow(new MenuDeckController(), MENU_DECK_PATH, deckController);//5
                case CREATE_DECK_WINDOW -> loadWindow(new CreateDeckController(), CREATE_DECK_PATH, deckController);//6
                case ACCOUNT_DECKS_WINDOW -> loadWindow(new AccountDecksController(), ACCOUNT_DECKS_PATH, deckController);//7
                case EVALUATE_CARDS_WINDOW -> loadWindow(new EvaluateCardsController(), EVALUATE_CARDS_PATH, deckController);//8
                case DECK_MODE_WINDOW -> loadWindow(new DeckModeController(), DECK_MODE_PATH, deckController);//9
                case ADD_NORMAL_CARD_WINDOW -> loadWindow(new AddNormalCardController(), ADD_NORMAL_CARD_PATH, deckController);//10
                case CARD_STUDY_MODE_WINDOW -> loadWindow(new CardStudyModeController(), CARD_STUDY_MODE_PATH, deckController);//11
                case ADD_LATEX_HTML_CARD_WINDOW -> loadWindow(new AddLatexHTMLCardController(), ADD_LATEX_HTML_CARD_PATH, deckController);//12
                case ADD_CARD_OPTIONS_WINDOW -> loadWindow(new AddCardOptionController(), ADD_CARD_OPTIONS_PATH, deckController);//13
                case EDIT_CARD_WINDOW -> loadWindow(new EditCardController(), EDIT_CARD_PATH, deckController);//14
                case EXPORT_DECK_WINDOW -> loadWindow(new ExportDeckController(), EXPORT_DECK_PATH, deckController);//15
                case IMPORT_DECK_WINDOW -> loadWindow(new ImportDeckController(), IMPORT_DECK_PATH, deckController);//16
                case STORE_VIEW_WINDOW -> loadWindow(new StoreViewController(), STORE_VIEW_PATH, storeController);//17
                case CARD_FREE_MODE_WINDOW -> loadWindow(new CardFreeModeController(), ConstAppWindow.CARD_FREE_MODE_PATH, deckController);//18
                default -> PopUp.showError(ConstAppError.WINDOW);
            }
        } catch (IOException e) {
            PopUp.showError(ConstAppError.CHANGE_VIEW);
        }
    }

    /**
     * This method is called whenever the window to display changes. It creates a new window and displays it.
     * @param editorController The view controller for the window
     * @param path The path to the FXML window
     * @param mainController The controller that will control button clicks and information
     * @throws IOException An exception is thrown when an error occurs during window creation
     */
    private void loadWindow(AbstractEditorController editorController, String path, AbstractController mainController) throws IOException{
        logger.debug("loadWindow()");
        FXMLLoader fxmlLoader = new FXMLLoader(editorController.getClass().getResource(path));
        Parent root = fxmlLoader.load();
        AbstractEditorController controller = fxmlLoader.getController();
        controller.setController(mainController);
        controller.setDeck(deckController.getCurrentDeck());
        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.showStage();
    }

    /**
     * This method is an overloaded method that will set the controller depending on its type
     * @param deckController The controller that is set
     */
    public void setController(DeckController deckController) {
        this.deckController = deckController;
    }
    /**
     * This method is an overloaded method that will set the controller depending on its type
     * @param connectionController The controller that is set
     */
    public void setController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }
    /**
     * This method is an overloaded method that will set the controller depending on its type
     * @param storeController The controller that is set
     */
    public void setController(StoreController storeController) {
        this.storeController = storeController;
    }
}
