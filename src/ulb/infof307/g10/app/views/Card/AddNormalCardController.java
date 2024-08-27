package ulb.infof307.g10.app.views.Card;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.constante.appConst.ConstCardType;

/**
 * Controller for the add normal card window
 */
public class AddNormalCardController extends AbstractEditorController {

    private DeckControllerInterface controller;
    private static final int WINDOW = ADD_NORMAL_CARD_WINDOW;
    public static final int NOT_EVALUATED = 0;

    @FXML
    public TextField questionField;
    @FXML
    public TextField answerField;
    @FXML
    public ComboBox<String> categoryBox;

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface) controller;
    }

    @Override
    public void setDeck(Deck deck) {
        List<String> categories = deck.getCategoryList();
        ObservableList<String> evaluation = FXCollections.observableArrayList();
        evaluation.addAll(categories);
        this.categoryBox.setItems(evaluation);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(DECK_MODE_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onSaveClicked() {
        List<Object> information = new ArrayList<>();
        information.add(this.categoryBox.getValue());
        information.add(this.questionField.getText());
        information.add(this.answerField.getText());
        information.add(Integer.toString(NOT_EVALUATED));
        information.add(Integer.toString(ConstCardType.NORMAL_TYPE));
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SAVE, information);
    }
}


