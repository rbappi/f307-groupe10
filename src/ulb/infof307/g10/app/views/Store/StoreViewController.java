package ulb.infof307.g10.app.views.Store;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.StoreControllerInterface;
import ulb.infof307.g10.app.views.Util.PopUp;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.app.models.DeckForSale;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

/**
 * Controller for the store view
 */
public class StoreViewController extends AbstractEditorController {

    List<DeckForSale> decksInStore = new ArrayList<>();
    List<Deck> myDecks = new ArrayList<>();
    private String username;
    private ObservableList<String> categoryItemsStore;
    private ObservableList<String> categoryItemsUsers;
    private StoreControllerInterface controller;
    private static final int WINDOW = STORE_VIEW_WINDOW;
    int token = 0;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label money;
    @FXML
    private Button buyButton;
    @FXML
    protected ComboBox<String> listMyDecks;
    @FXML
    protected ComboBox<String> listStore;
    @FXML
    protected Label author;
    @FXML
    protected Label priceBuy;
    @FXML
    protected Spinner<Integer> priceSell;
    @FXML
    protected Label amountCard;
    @FXML
    protected Label titleSell;
    @FXML
    protected Label titleBuy;

    /**
     * Initialize the list of decks in the store
     */
    @Override
    public void setController(AbstractController controller) {
        this.controller = (StoreControllerInterface) controller;
        init();
    }

    public void init() {
        this.myDecks = controller.getDecks();
        this.username = controller.getUsername();
        this.updateTokens();
        this.updateStoreTokens();
        this.decksInStore = controller.getDecksInStore();
        this.categoryItemsUsers = FXCollections.observableArrayList();
        this.categoryItemsStore = FXCollections.observableArrayList();
        this.initializeListOfInfo();
        this.initializeStoreItemInfo();
        this.priceSell.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
    }


    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(ACCOUNT_OPTIONS_MENU_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }


    public void buyDeck(String deckName,String sellerUsername) {
        List<Object> information = new ArrayList<>();
        information.add(deckName);
        information.add(sellerUsername);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.BUY, information);
        updateStoreTokens();
        updateTokens();
    }

    public void updateStoreTokens(){
        token = this.controller.getTokens();
    }

    public void updateTokens(){
        token = this.controller.getTokens();
        this.money.setText(String.valueOf(token));
    }

    /**
     * Initialize the list of decks that are used to initialize the list internally (not the GUI)
     */
    public void initializeListOfInfo() {
        for (DeckForSale deck : this.decksInStore) {
            if (!deck.getSellerUsername().equals(this.username)) { // We don't want to see our own decks in the store
                this.categoryItemsStore.add(deck.getDeckName() + " - " + deck.getSellerUsername());
            }
        }
        for (Deck deck : this.myDecks) {
            this.categoryItemsUsers.add(deck.getDeckName());
        }
    }

    /**
     * Initialize the list of decks that are used to initialize for the GUI
     */
    @FXML
    protected void initializeStoreItemInfo() {
        for (String item: this.categoryItemsStore){
            this.listStore.getItems().add(item);
        }
        for (String item: this.categoryItemsUsers){
            this.listMyDecks.getItems().add(item);
        }
    }

    @FXML
    protected void onBuyClicked(){
        int selectedIndex = this.listStore.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            DeckForSale deckToBuy = decksInStore.get(selectedIndex);
            this.buyDeck(deckToBuy.getDeckName(),deckToBuy.getSellerUsername());
            buyButton.setDisable(true);
            PopUp.showSuccess("You have successfully bought a deck");
        }
    }

    /**
     * Check if a deck is already in the store
     */
    private Boolean isDeckInStore(String deckName, String sellerUsername){
        for (DeckForSale deck : this.decksInStore) {
            if (deck.getDeckName().equals(deckName) && deck.getSellerUsername().equals(sellerUsername)){
                return true;
            }
        }
        return false;
    }

    @FXML
    protected void onSellClicked(){
        int selectedIndex = this.listMyDecks.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) { // If a deck is selected
            Deck selectedDeck = this.myDecks.get(selectedIndex);
            int priceToBeSold = this.priceSell.getValue();
            if (Boolean.TRUE.equals(isDeckInStore(selectedDeck.getDeckName(), this.username))){
                PopUp.showError("This deck is already in the store");
                return;
            }
            List<Object> information = new ArrayList<>();
            information.add(selectedDeck.getDeckName());
            information.add(priceToBeSold);
            information.add(true);
            this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SELL, information);
            PopUp.showSuccess("You have successfully put your deck on sale");
            this.initializeListOfInfo();
            this.initializeStoreItemInfo();
        }
        else {
            PopUp.showError("You need to select a deck to sell ");
        }
    }

    /**
     * Update the information about the deck that is selected in the store
     */
    @FXML
    protected void onDeckChanged(){
        int selectedIndex = this.listStore.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            DeckForSale deckToBuy = decksInStore.get(selectedIndex);
            String author = deckToBuy.getSellerUsername();
            String price = String.valueOf(deckToBuy.getPrice());
            String title = deckToBuy.getDeckName();
            int amount = this.controller.getCardCountInDeck(title, author);
            this.author.setText(author);
            this.priceBuy.setText(price);
            this.amountCard.setText(String.valueOf(amount));
            this.titleBuy.setText(title);

        }
    }

    /**
     * Update the information about the deck that is selected in the list of decks of the user
     */
    @FXML
    protected void onMyDeckChanged(){
        int selectedIndex = this.listMyDecks.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Deck deckToSell = myDecks.get(selectedIndex);
            String title = deckToSell.getDeckName();
            this.titleSell.setText(title);
        }
    }
}

