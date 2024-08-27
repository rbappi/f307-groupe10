package ulb.infof307.g10.app.views.Connection;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.ConnectionControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.abstractClass.AbstractEditorController;

/**
 * Controller for the change password window
 */
public class ChangePasswordController extends AbstractEditorController {

    private static final int WINDOW = CHANGE_PASSWORD_WINDOW;
    private ConnectionControllerInterface controller;

    @FXML
    protected TextField usernameField;
    @FXML
    protected PasswordField newPasswordField;
    @FXML
    protected PasswordField oldPasswordField;

    public void setController(AbstractController controller) {
        this.controller = (ConnectionControllerInterface)controller;
    }

    @FXML
    public void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ACCOUNT_OPTIONS_MENU_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    public void onSaveClicked() {
        List<Object> information = new ArrayList<>();
        information.add(usernameField.getText());
        information.add(oldPasswordField.getText());
        information.add(newPasswordField.getText());
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SAVE, information);
    }
}
