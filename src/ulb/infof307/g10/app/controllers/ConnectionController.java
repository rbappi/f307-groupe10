package ulb.infof307.g10.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.app.views.Util.PopUp;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;

/**
 * The purpose of this class is to control the connections in views.
 **/
public class ConnectionController extends AbstractController implements ConnectionControllerInterface {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionController.class);

    ConnectionController(PacketController packetController, WindowController windowController) {
        super(packetController, windowController);
    }

    /**
     * This method is called whenever a button is clicked. It redirects the call to the correct method to execute.
     * @param window Number representing the window that called this function
     * @param methodeName Name of the method attached to the button that was clicked
     * @param information A list with the information required to execute the function
     */
    public void buttonHandler(int window, String methodeName, List<Object> information) {
        switch (methodeName) {
            case ConstAppButtonAction.CHANGE_VIEW -> this.windowController.changeView((int)information.get(0));
            case ConstAppButtonAction.SAVE -> actionButtonSave(window, information);
            default -> throw new IllegalStateException("Unexpected value for the buttonHandler: " + window);
        }
    }

    /**
     * This method is called when the save button is clicked
     * @param window Number representing the window that called this function
     * @param information A list with the information required to execute the function
     */
    private void actionButtonSave(int window, List<Object> information){
        switch (window) {
            case CHANGE_PASSWORD_WINDOW -> saveNewPassword(information);
            case CREATE_ACCOUNT_WINDOW -> createAccount(information);
            case LOGIN_WINDOW -> login(information);
            default -> throw new IllegalStateException("Unexpected value for the connection part: " + window);
        }
    }

    /**
     * This method is called when the user sets a new password
     * @param information A list with the information required to execute the function
     */
    private void saveNewPassword(List<Object> information) {
        logger.debug("saveNewPassword()");

        String username = (String) information.get(0);
        String oldPassword = (String) information.get(1);
        String newPassword = (String) information.get(2);

        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            PopUp.showError(ConstAppError.EDIT_PASSWORD_EMPTY);
        } else {
            if (packetController.sendNewPasswordInfo(username, oldPassword, newPassword, ConstServerRequest.CHANGING_PASSWORD)){
                this.windowController.changeView(ACCOUNT_OPTIONS_MENU_WINDOW);
            }
        }
    }

    /**
     * This method is called when a new account is created
     * @param information A list with the information required to execute the function
     */
    private void createAccount(List<Object> information) {
        logger.debug("createAccount()");

        String username = (String) information.get(0);
        String password = (String) information.get(1);

        if(packetController.sendAccountInfo(username, password, ConstServerRequest.ACCOUNT_CREATION)){
            windowController.changeView(CONNECTION_MENU_WINDOW);
        }
    }

    /**
     * This method is called when a user logs into an account
     * @param information A list with the information required to execute the function
     */
    private void login(List<Object> information) {
        logger.debug("loginToServer()");

        String username = (String) information.get(0);
        String password = (String) information.get(1);

        if(packetController.sendAccountInfo(username, password, ConstServerRequest.LOGIN)){
            this.windowController.changeView(ACCOUNT_OPTIONS_MENU_WINDOW);
        }
    }
}
