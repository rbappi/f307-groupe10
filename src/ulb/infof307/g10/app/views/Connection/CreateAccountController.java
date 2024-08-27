package ulb.infof307.g10.app.views.Connection;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

import ulb.infof307.g10.app.controllers.ConnectionControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;

/**
 * Controller for the create account window
 */
public class CreateAccountController extends AbstractEditorController {

    private static final int WINDOW = CREATE_ACCOUNT_WINDOW;
    private ConnectionControllerInterface controller;

    @FXML
    protected TextField usernameField;
    @FXML
    protected PasswordField passwordField;

    public void setController(AbstractController controller){
        this.controller = (ConnectionControllerInterface)controller;
    }

    @FXML
    protected void onCreateAccountClicked() {
        List<Object> information = new ArrayList<>(CONNECTION_MENU_WINDOW);
        information.add(usernameField.getText());
        information.add(passwordField.getText());
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SAVE, information);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(CONNECTION_MENU_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }
}
