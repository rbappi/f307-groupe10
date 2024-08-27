package ulb.infof307.g10.app.views.Deck;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.FileUtil;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;


/**
 * Controller for import deck window
 */
public class ImportDeckController extends AbstractEditorController{

    private DeckControllerInterface controller;
    private static final int WINDOW = IMPORT_DECK_WINDOW;

    @FXML
    private TextField pathField;

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface)controller;
        FileUtil fileUtil = new FileUtil();
        this.pathField.setText(System.getProperty("user.dir")+fileUtil.getFileSeparator());
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(MENU_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onSaveClicked() {
        List<Object> information = new ArrayList<>();
        information.add(this.pathField.getText());
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.IMPORT, information);
    }
}
