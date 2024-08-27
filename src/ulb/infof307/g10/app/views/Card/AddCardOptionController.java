package ulb.infof307.g10.app.views.Card;

import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;

import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;


/**
 * This class is the FXML controller of the intermediate window between the deck mode and the add card window
 */
public class AddCardOptionController extends AbstractEditorController {

    private DeckControllerInterface controller;
    private static final int WINDOW = ADD_CARD_OPTIONS_WINDOW;

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface) controller;
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(DECK_MODE_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onNormalClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ADD_NORMAL_CARD_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onLatexHTMLClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ADD_LATEX_HTML_CARD_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

}
