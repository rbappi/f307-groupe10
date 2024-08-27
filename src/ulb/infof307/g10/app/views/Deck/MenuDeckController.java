package ulb.infof307.g10.app.views.Deck;

import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.abstractClass.AbstractEditorController;

/**
 * Controller for the menu to manage decks
 */
public class MenuDeckController extends AbstractEditorController {
    private DeckControllerInterface controller;
    private static final int WINDOW = MENU_DECK_WINDOW;

    public void setController(AbstractController controller){
        this.controller = (DeckControllerInterface) controller;
    }

    @FXML
    protected void onCreateDeckClicked() {
        List<Object> information = new ArrayList<>();
        information.add(CREATE_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onToMyDeckClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ACCOUNT_DECKS_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SET, information);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ACCOUNT_OPTIONS_MENU_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }
}
