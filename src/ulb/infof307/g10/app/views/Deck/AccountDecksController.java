package ulb.infof307.g10.app.views.Deck;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.CheckComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.app.controllers.DeckController;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.abstractClass.AbstractEditorController;


/**
 * Controller for the account decks view
 */
public class AccountDecksController extends AbstractEditorController {

    private static final int layoutX = 13;
    private static final int layoutY = 15;
    private static final int WINDOW = ACCOUNT_DECKS_WINDOW;
    private DeckControllerInterface controller;
    private List<Button> buttons = new ArrayList<>();
    private List<Deck> decks;
    private List<String> wantedCategories = new ArrayList<>();
    private CheckComboBox<String> categoryCheckComboBox;
    private int categoryLineNumber = 1;

    @FXML
    private AnchorPane buttonField;

    public void setController(AbstractController controller) {
        this.controller = (DeckController)controller;
        this.decks = ((DeckControllerInterface)controller).getDecks();
        this.updateWindow();
    }

    /**
     * Initialize the page with the decks of the user
     */
    private void updateWindow(){
        buttonField.getChildren().clear();
        makeCategoryCheckBoxes();
        makeButton();
    }

    @FXML
    protected void onDeckClicked(ActionEvent event) {
        Button buttonClicked = (Button) event.getTarget();
        List<Object> information = new ArrayList<>();
        information.add(buttonClicked.getText());
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SET, information);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(MENU_DECK_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onFilterClicked(){
        this.wantedCategories.clear();
        this.wantedCategories.addAll(categoryCheckComboBox.getCheckModel().getCheckedItems());
        updateWindow();
    }

    /**
     * Make the category checkboxes to filter the decks following the categories
     */
    @FXML
    protected void makeCategoryCheckBoxes() {
        List<String> previouslyAddedCategories = new ArrayList<>();
        ObservableList<String> categoryItems = FXCollections.observableArrayList();
        for (Deck deck : decks) {
            for (String category : deck.getCategoryList()) {
                if (!previouslyAddedCategories.contains(category)) { // if this is the chosen category, show the deck that has been filtered
                    categoryItems.add(category);
                    previouslyAddedCategories.add(category);
                }
            }
        }
        this.categoryCheckComboBox = new CheckComboBox<>(categoryItems);
        this.categoryCheckComboBox.setLayoutX(layoutX);
        this.categoryCheckComboBox.setLayoutY(layoutY);
        this.buttonField.getChildren().add(categoryCheckComboBox);
    }

    /**
     * Make the buttons to display the decks following a certain pattern
     */
    @FXML
    protected void makeButton() {
        buttons.clear();
        double x = layoutX;
        double y = 85.0 +((categoryLineNumber-1) * 25.0);
        for (Deck deck : decks) {
            List<String> deckCategories = deck.getCategoryList();
            if (new HashSet<>(deckCategories).containsAll(wantedCategories) || wantedCategories.isEmpty()) {
                if (x > 312.0) {
                    y += 85.0;
                    x = 13.0;
                }
                String deckName = deck.getDeckName();
                Button button = new Button(deckName);
                button.setLayoutX(x);
                button.setLayoutY(y);
                button.setStyle("-fx-background-color: #8fbc8f;");
                button.setOnAction(this::onDeckClicked);

                buttons.add(button);
                x += 112.0;
            }
        }
        buttonField.getChildren().addAll(buttons);
    }
}
