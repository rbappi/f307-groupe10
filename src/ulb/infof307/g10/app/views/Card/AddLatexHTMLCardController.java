package ulb.infof307.g10.app.views.Card;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.constante.appConst.ConstCardType;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.FileUtil;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.WebviewHandler;
import ulb.infof307.g10.app.views.Util.PopUp;

/**
 * This class is the FXML controller of the add special card such as latex and html
 */
public class AddLatexHTMLCardController extends AbstractEditorController {

    public TextArea questionTextArea;
    public WebView questionwebView;
    public Button saveButton;
    public Button returnButton;
    private DeckControllerInterface controller;
    public TextArea categoryTextArea;
    private int actualMode; // 0: no type, 1: normal, 2: latex, 3: html
    private static final int WINDOW = ADD_LATEX_HTML_CARD_WINDOW;
    @FXML
    protected ComboBox<String> typeOfCardCombo;
    @FXML
    protected TextArea answerTextArea;
    @FXML
    protected WebView answerWebView;

    public void setController(AbstractController controller){
        this.controller = (DeckControllerInterface) controller;
    }

    public void typeOfCardChanged() {
        String choice = typeOfCardCombo.getValue();
        this.actualMode = this.changeMode(choice);
        setListeners();
    }

    public int changeMode(String choice) {
        return switch (choice) {
            case "Normal" -> ConstCardType.NORMAL_TYPE;
            case "LaTex(only math)" -> ConstCardType.LATEX_TYPE;
            case "HTML" -> ConstCardType.HTML_TYPE;
            default -> ConstCardType.NO_TYPE; // To show that there is no type selected
        };
    }

    public void initialize() {
        setListeners();
    }

    /**
     * This method will set the listeners for the question and answer text area
     */
    private void setListeners() {
        WebviewHandler.setListener(this.questionTextArea, this.questionwebView, this.actualMode);
        WebviewHandler.setListener(this.answerTextArea, this.answerWebView, this.actualMode);
    }

    public void returnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ADD_CARD_OPTIONS_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    /**
     * This method is called when the user clicks on the save button
     * It will check if the fields are filled and if the card is saved successfully
     */
    public void onSaveButtonClicked() {
        if (checkValidity()) return;
        try{
            FileUtil fileUtil = new FileUtil();
            String questionPath = fileUtil.mainFileOperation(this.questionTextArea.getText(), this.actualMode, true);
            String answerPath = fileUtil.mainFileOperation(this.answerTextArea.getText(), this.actualMode, false);
            List<Object> information = new ArrayList<>();
            information.add(this.categoryTextArea.getText());
            information.add(questionPath);
            information.add(answerPath);
            information.add("0");
            information.add(Integer.toString(this.actualMode));
            this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SAVE, information);
        }
        catch (Exception e) {
            PopUp.showError("There was an error while saving your card", "Error", "Error in " + this.getClass().getName());
        }
    }

    private boolean checkValidity() {
        if (this.categoryTextArea.getText().equals("") || this.questionTextArea.getText().equals("") || this.answerTextArea.getText().equals("")) {
            PopUp.showError("Please fill all the fields", "Error", "Error in " + this.getClass().getName());
            return true;
        }
        if (this.actualMode == ConstCardType.NO_TYPE) {
            PopUp.showError("Please select a type of card", "Error", "Error in " + this.getClass().getName());
            return true;
        }
        return false;
    }

}

