package ulb.infof307.g10.app.views.Connection;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.app.controllers.ConnectionControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.abstractClass.AbstractEditorController;

/**
 * Controller for the connection menu view
 */
public class ConnectionMenuController extends AbstractEditorController {

    private static final int WINDOW = CONNECTION_MENU_WINDOW;
    private ConnectionControllerInterface controller;

    @FXML
    protected Button loginButton;
    @FXML
    protected Button createAccountButton;

    public void setController(AbstractController controller){
        this.controller = (ConnectionControllerInterface)controller;
    }

    @FXML
    protected void onLoginClicked() {
        List<Object> information = new ArrayList<>();
        information.add(LOGIN_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onNewAccountClicked() {
        List<Object> information = new ArrayList<>();
        information.add(CREATE_ACCOUNT_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }
}
