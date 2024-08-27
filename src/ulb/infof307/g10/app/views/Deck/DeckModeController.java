package ulb.infof307.g10.app.views.Deck;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;


/**
 * Controller for the deck mode menu
 */
public class DeckModeController extends AbstractEditorController {

    private DeckControllerInterface controller;
    private static final int WINDOW = DECK_MODE_WINDOW;
    private final Hashtable<String, String> buttonDict = new Hashtable<>();

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface)controller;
        setButtonDict();
    }

    /**
     * Set the button dictionary to reference the correct window when a button is clicked
     */
    private void setButtonDict() {
        String[] buttonName = {"Study Mode",
                                "Free Mode",
                                "Evaluate Cards",
                                "Edit Card",
                                "Export"};
        int[] window = {CARD_STUDY_MODE_WINDOW,
                        CARD_FREE_MODE_WINDOW,
                        EVALUATE_CARDS_WINDOW,
                        EDIT_CARD_WINDOW,
                        EXPORT_DECK_WINDOW};
        int index = 0;
        for(String content : buttonName){
            buttonDict.put(content, Integer.toString(window[index]));
            index++;
        }
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(MENU_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    /**
     * Handle the click on a button of the menu
     * @param event The object of the action event
     */
    @FXML
    protected void onCommonClicked(ActionEvent event) {
        Button buttonClicked = (Button) event.getTarget(); // Get the button clicked
        List<Object> information = new ArrayList<>();
        int nextWindow = Integer.parseInt(buttonDict.get(buttonClicked.getText())); // Get the window to open
        information.add(nextWindow);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SET, information); // Open the window
    }

    @FXML
    protected void onAddCardClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ADD_CARD_OPTIONS_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onDeleteClicked() {
        List<Object> information = new ArrayList<>();
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.EDIT, information);
    }
}

