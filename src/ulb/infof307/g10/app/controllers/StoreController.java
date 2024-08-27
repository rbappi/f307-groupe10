package ulb.infof307.g10.app.controllers;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.app.models.DeckForSale;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller passed to store view
 */
public class StoreController extends AbstractController implements StoreControllerInterface{

    private List<DeckForSale> decksInStore = new ArrayList<>(); // list of decks in store


    public StoreController(PacketController packetController, WindowController windowController){
        super(packetController, windowController);

    }

    /**
     * This method is called whenever a button is pressed. It redirects the call to the correct method
     * @param window Number representing the window that called this method
     * @param action Name of the button function that was called in the window
     * @param information A list containing the information required to execute the function
     */
    public void buttonHandler(int window, String action, List<Object> information) {
        switch (action) {
            case ConstAppButtonAction.CHANGE_VIEW -> this.windowController.changeView((int)information.get(0));
            case ConstAppButtonAction.BUY -> buyDeck(information);
            case ConstAppButtonAction.SELL -> setStoreDeckInfo(information);
            default -> throw new IllegalStateException("Unexpected value for the buttonHandler: " + window);
        }
    }

    /**
     * This method gets the decks of the user
     * @return A list containing the decks of the user
     */
    public List<Deck> getDecks() {
        return packetController.askDecks();
    }

    /**
     * This method gets the name of the current user
     * @return A string containing a username
     */
    public String getUsername() {
        return packetController.getUsername();
    }

    /**
     * This method gets the tokens of the current user
     * @return An int containing the number of tokens
     */
    public int getTokens(){
        return packetController.getAmount();
    }

    /**
     * This method updates the decks for sale displayed in the store
     */
    private void updateStore() {
        String[][] decks = packetController.askDecksInStore();
        this.decksInStore = completeDeckToBuy(decks);
    }

    /**
     * This method get the decks for sale in the store
     * @return A list of the decks for sale
     */
    public List<DeckForSale> getDecksInStore() {
        updateStore();
        return decksInStore;
    }

    /**
     * This method creates a list of display decks to put in the store
     * @param deckInStore Deck information for each display deck
     * @return List of decks for sale
     */
    private List<DeckForSale> completeDeckToBuy(String[][] deckInStore) {
        List<DeckForSale> decks = new ArrayList<>();

        for (String[] deck : deckInStore) {
            int id = Integer.parseInt(deck[0]);
            String deckName = deck[1];
            String sellerUsername = deck[2];
            int price = Integer.parseInt(deck[3]);
            boolean inStore = Boolean.parseBoolean(deck[4]);
            decks.add(new DeckForSale(id, deckName, sellerUsername, price, inStore));
        }
        return decks;
    }

    /**
     * This method buys a deck adding it to th decks of the user
     *
     * @param information Contains name of the bought deck and name of the user selling the deck
     */
    private void buyDeck(List<Object> information){
        String deckName = (String)information.get(0);
        String sellerUsername = (String)information.get(1);
        packetController.buyDeck(deckName, sellerUsername);
    }

    /**
     * This method gives the number of cards present in a deck
     * @param deckName Name of the deck
     * @param username Name of the user the deck belongs to
     * @return An in t containing the number of cards
     */
    public int getCardCountInDeck(String deckName, String username){
        return packetController.getCardCountInDeck(deckName,username);
    }

    /**
     * This method updates the price and status(if it's for sale or not of a deck)
     * @param information List containing information required for the update in this case [deckName, price, if it's for sale]
     */
    private void setStoreDeckInfo(List<Object> information){
        String deckName = (String) information.get(0);
        int price = (int)information.get(1);
        boolean inStore = (boolean)information.get(2);
        packetController.setStoreDeckInfo(deckName, price, inStore);
    }
}
