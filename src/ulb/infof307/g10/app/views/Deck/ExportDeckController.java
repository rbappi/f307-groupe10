package ulb.infof307.g10.app.views.Deck;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;


/**
 * Controller for the export deck window
 */
public class ExportDeckController extends AbstractEditorController {

    private DeckControllerInterface controller;
    private static final int WINDOW = EXPORT_DECK_WINDOW;

    @FXML
    private TextField pathField;

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface)controller;
        this.pathField.setText(System.getProperty("user.dir"));
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(MENU_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    public void onPDFClicked() {
        List<Object> information = new ArrayList<>();
        information.add(this.pathField.getText());
        information.add("pdf");
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.EXPORT, information);
    }

    @FXML
    public void onCSVClicked() {
        List<Object> information = new ArrayList<>();
        information.add(this.pathField.getText());
        information.add("csv");
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.EXPORT, information);
    }
}
