package ulb.infof307.g10.app.views.Connection;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;

import ulb.infof307.g10.abstractClass.AbstractController;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

import ulb.infof307.g10.app.controllers.ConnectionControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;


/**
 * Controller for the account options menu
 */
public class AccountOptionsMenuController extends AbstractEditorController {

    private static final int WINDOW = ACCOUNT_OPTIONS_MENU_WINDOW;
    private ConnectionControllerInterface controller;

    public void setController(AbstractController controller) {
        this.controller = (ConnectionControllerInterface)controller;
    }

    @FXML
    protected void onDisconnectClick() {
        List<Object> information = new ArrayList<>();
        information.add(CONNECTION_MENU_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onMyDeckClick() {
        List<Object> information = new ArrayList<>();
        information.add(MENU_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onChangePasswordClick() {
        List<Object> information = new ArrayList<>();
        information.add(CHANGE_PASSWORD_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onOpenStoreClick() {
        List<Object> information = new ArrayList<>();
        information.add(STORE_VIEW_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }
}
