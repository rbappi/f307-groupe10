package ulb.infof307.g10.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;

import ulb.infof307.g10.app.models.*;
import ulb.infof307.g10.app.views.Util.PopUp;
import ulb.infof307.g10.constante.appConst.ConstAppSuccess;
import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.network.packet.Parse;
import ulb.infof307.g10.util.DeckExporter;
import ulb.infof307.g10.util.DeckImporter;

/**
 * This class controls the decks
 */

public class DeckController extends AbstractController implements DeckControllerInterface {

    private static final Logger logger = LoggerFactory.getLogger(DeckController.class);
    private List<Deck> decks = new ArrayList<>(); // list of decks
    private Deck currentDeck;
    private final Parse parser = new Parse();
    private final DeckImporter deckImporter = new DeckImporter();

    /**
     * @param packetController Manages the packets sent between server and client
     * @param windowController Controls the display of windows as well as some user info
     */
    public DeckController(PacketController packetController, WindowController windowController){
        super(packetController, windowController);
    }

    /**
     * Receives information from view and applies actions according to the information received
     * @param window Number representing the window that called this method
     * @param action Name of the button function that was called in the window
     * @param information A list containing the information required to execute the function
     */
    public void buttonHandler(int window, String action, List<Object> information) {
        switch (action) {
            case ConstAppButtonAction.CHANGE_VIEW -> this.windowController.changeView((int)information.get(0));
            case ConstAppButtonAction.SAVE -> actionButtonSave(window, information);
            case ConstAppButtonAction.SET -> actionButtonSet(window, information);
            case ConstAppButtonAction.EDIT -> actionButtonEdit(window);
            case ConstAppButtonAction.EXPORT -> {
                exportDeck(information);
                this.windowController.changeView(MENU_DECK_WINDOW);
            }
            case ConstAppButtonAction.IMPORT -> {
                importDeck(information);
                this.windowController.changeView(MENU_DECK_WINDOW);
            }
            default -> throw new IllegalStateException("Unexpected value for the buttonHandler: " + window);
        }
    }

    /**
     * This method is executed by the controller when edit button is pressed
     * @param window Number representing the window that call this method
     */
    private void actionButtonEdit(int window) {
        if (window == DECK_MODE_WINDOW) {
            setCurrentDeckCards();
            deleteCurrentDeck();
            this.windowController.changeView(MENU_DECK_WINDOW);
        } else {
            throw new IllegalStateException("Unexpected value for the actionButtonEdit: " + window);
        }
    }

    /**
     * This method is executed by the controller when the save button is pressed
     * @param window Number representing the window that call this method
     * @param information Information required to execute the function
     */
    private void actionButtonSave(int window, List<Object> information){
        switch (window) {
            case CREATE_DECK_WINDOW -> {
                if(saveNewDeck(information)) {
                    this.windowController.changeView(ACCOUNT_DECKS_WINDOW);
                }
            }
            case ADD_LATEX_HTML_CARD_WINDOW, ADD_NORMAL_CARD_WINDOW -> {
                if(saveNewCard(information))
                    this.windowController.changeView(DECK_MODE_WINDOW);
            }
            case EDIT_CARD_WINDOW -> {
                deleteCard(information.get(0));
                setModifiedDeck(information.get(1));
                this.windowController.changeView(DECK_MODE_WINDOW);
            }
            default -> throw new IllegalStateException("Unexpected value for the actionButtonSave: " + window);
        }
    }

    /**
     * This method is called when a button that sets information is called
     * @param window Number representing the window that call this method
     * @param information List containing the information required to execute the function
     */
    private void actionButtonSet(int window, List<Object> information){
        switch (window) {
            case ACCOUNT_DECKS_WINDOW -> {
                setCurrentDeck(information);
                this.windowController.changeView(DECK_MODE_WINDOW);
            }
            case ADD_NORMAL_CARD_WINDOW -> saveNewCard(information);
            case DECK_MODE_WINDOW -> {
                setCurrentDeckCards();
                if(!isDeckEmpty())this.windowController.changeView((int) information.get(0));
                else PopUp.showError(ConstAppError.CARD_EMPTY);
            }
            case MENU_DECK_WINDOW -> {
                getDecks();
                this.windowController.changeView((int) information.get(0));
            }
            case CARD_STUDY_MODE_WINDOW -> {
                updateToken(information);
                this.windowController.changeView(DECK_MODE_WINDOW);
            }
            case EVALUATE_CARDS_WINDOW -> {
                setModifiedDeck(information.get(0));
                this.windowController.changeView(DECK_MODE_WINDOW);
            }
            case CARD_FREE_MODE_WINDOW -> {
                updateScore(information);
                updateToken(information);
                this.windowController.changeView(DECK_MODE_WINDOW);
            }
            default -> throw new IllegalStateException("Unexpected value for the actionButtonSet: " + window);
        }
    }

    /**
     * This method updates the score of the current deck
     * @param information  List containing the score
     */
    private void updateScore(List<Object> information) {
        this.packetController.setScore(this.currentDeck.getDeckName(), (String)information.get(1));
    }

    /**
     * This method updates the decks of the user
     */
    private void updateDecks() {
        this.decks = packetController.askDecks();
    }

    /**
     * This method sets the deck currently in use
     * @param information List containing the name of the deck
     */
    private void setCurrentDeck(List<Object> information) {
        String currentDeckName = (String)information.get(0);
        this.currentDeck = getCurrentDeckObjet(currentDeckName);
    }

    /**
     * This method returns the current deck
     * @return deck stored in currentDeck
     */
    public Deck getCurrentDeck(){ return this.currentDeck; }

    /**
     * This method is called to determine the currentDecks name
     * @return returns the currentDecks name
     */
    private String getCurrentDeckName(){ return this.currentDeck.getDeckName(); }

    /**
     * Method to determine if currentDeck is empty
     * @return returns True if currentDeck as no cards
     */
    private boolean isDeckEmpty() {
        return this.currentDeck.isEmpty();
    }

    /**
     * This method updates the list of decks then return this list
     * @return List of decks
     */
    public List<Deck> getDecks(){
        updateDecks();
        return decks;
    }

    /**
     * This method receives a modified Deck from the user and sends it to the server/database
     * @param information A deck
     */
    private void setModifiedDeck(Object information){
        Deck deck = (Deck) information;
        int deckIndex = getIndexDeck(this.currentDeck);
        decks.set(deckIndex, deck);
        List<Card> listOfCards = deck.getListOfCards();
        String deckName = deck.getDeckName();
        for (Card card:listOfCards){
            String id = String.valueOf(card.getId());
            String category = card.getCategory();
            String newQuestion = card.getQuestion();
            String newAnswer = card.getAnswer();
            String newEvaluation = String.valueOf(card.getEvaluation());
            packetController.sendModifiedCard(id, deckName, category,newQuestion, newAnswer, newEvaluation);
        }
    }

    /**
     * This method receives every card of a deck from the server and fills the deck with these cards.
     */
    private void setCurrentDeckCards() {
        String content = packetController.askDeckCard(getCurrentDeckName());
        String deckId = this.currentDeck.getId();
        Deck newDeck = new Deck(deckId, this.currentDeck.getDeckName(), this.currentDeck.getCategoryList(),this.currentDeck.getScoreMax());
        if(!content.isEmpty()){
            String[][] cards = parser.parse(content, 6);
            int index = getIndexDeck(this.currentDeck);
            for (String[] card : cards) {
                newDeck.addCard(new Card(Integer.parseInt(card[0]), card[1], card[2], card[3], Integer.parseInt(card[4]), Integer.parseInt(card[5])));
            }
            this.decks.set(index, newDeck);
        }
        this.currentDeck = newDeck;
    }

    /**
     * This method gets all the categories of a deck
     * @param deckName The name of the deck
     * @return String list containing the categories of the deck
     */
    private List<String> getDeckCategory(String deckName){
        List<String> deckCategory = new ArrayList<>();
        for (Deck deck: decks){
            if (Objects.equals(deck.getDeckName(), deckName)){
                deckCategory = deck.getCategoryList();
            }
        }
        return deckCategory;
    }

    /**
     * Function used to acquire the currentDeck object
     * @param currentDeckName Name of currentDeck
     * @return The deck stored in currentDeck
     */
    private Deck getCurrentDeckObjet(String currentDeckName){
        for (Deck deck : decks) {
            if (Objects.equals(deck.getDeckName(), currentDeckName)) {
                return deck;
            }
        }
        return null;
    }

    /**
     * Function that finds the placement, in the list decks, of the deck given
     * @param deck a deck
     * @return Index of deck in the list decks
     */
    private int getIndexDeck(Deck deck){
        for(int i = 0; i < decks.size(); i++){
            if(Objects.equals(deck.getId(), decks.get(i).getId())){
                return i;
            }
        }
        return -1;
    }

    /**
     * Deletes the deck in currentDeck from the database
     */
    private void deleteCurrentDeck(){
        List<Card> cards = this.currentDeck.getListOfCards();
        for(Card card : cards){
            if(packetController.deleteCard(card.getId())){
                return;
            }
        }
        packetController.deleteDeck(getCurrentDeckName());
    }
    /**
     * Deletes a list of cards from the database
     */
    private void deleteCard(Object information){
        List<Card> deletedCards = (List<Card>) information;
        for(Card card : deletedCards){
            if(packetController.deleteCard(card.getId())){
                return;
            }
        }
    }

    /**
     * From the input of user, create a csv file where deck data is written
     * @param information path and extension
     */
    private void exportDeck(List<Object> information) {
        logger.info("Exporting deck");
        String path = (String) information.get(0);

        if(path.isEmpty()){
            PopUp.showError(ConstAppError.EXPORT_DECK_PATH);
            return;
        }

        String extension = (String) information.get(1);
        List<String[]> deckData = new ArrayList<>();
        List<Card> cards = this.currentDeck.getListOfCards();
        String deckName = this.currentDeck.getDeckName();

        for(Card card : cards){
            String[] cardData = new String[5];
            cardData[0] = card.getQuestion();
            cardData[1] = card.getAnswer();
            cardData[2] = card.getCategory();
            cardData[3] = String.valueOf(card.getEvaluation());
            cardData[4] = String.valueOf(card.getTypeOfCard());
            deckData.add(cardData);
        }
        DeckExporter deckExporter = new DeckExporter();
        if(deckExporter.export(deckName, deckData, path, extension)){
            PopUp.showSuccess(ConstAppSuccess.EXPORT_DECK);
        }
        else{
            PopUp.showError(ConstAppError.EXPORT_DECK);
        }
    }

    /**
     * import a csv file. Save the deck from this file in database
     * @param information path
     */
    private void importDeck(List<Object> information) {
        logger.info("Importing deck");
        String path = (String) information.get(0);
        if(path.isEmpty()){
            PopUp.showError(ConstAppError.IMPORT_DECK);
            return;
        }
        try {
            List<String[]> deckData = deckImporter.importFromCsv(path);
            deckData.remove(0); //remove an unwanted line (the column names)
            int index = deckData.size();
            String deckName = saveImportDeck(deckData, index);
            deckData.remove(index - 1); //remove an unwanted line (empty line?)
            if (!deckName.isEmpty()) {
                for (String[] cardData : deckData) {
                    String question = cardData[0];
                    String answer = cardData[1];
                    String category = cardData[2];
                    String evaluation = cardData[3];
                    String typeOfCard = cardData[4];
                    packetController.sendNewCard(deckName, category, question, answer, evaluation, typeOfCard);
                }
            } else PopUp.showError(ConstServerError.DECK_EXIST);
        }
        catch(IOException e) {
            PopUp.showError(ConstAppError.CSV_ERROR);
        }
    }

    /**
     * Save a deck in the server from data received from an import.
     * deckData was created
     * @param deckData vector of vector. Data received from csv file. String[] were the lines of the file
     * @param index number of lines of the csv files
     * @return deckname
     */
    private String saveImportDeck(List<String[]> deckData, int index) {
        String deckName = deckData.get(index - 1)[0];//get name of the deck
        List<String> list = new ArrayList<>(List.of(deckData.get(index - 1)));
        list.remove(deckName);
        String[] categories = list.toArray(new String[0]); //convert a list to an array to send it to server
        String deckCategories = parser.inverseParse(categories);
        if(packetController.sendNewDeck(deckName, deckCategories)){//create deck
            return deckName;
        }
        return "";
    }

    /**
     * Function saves a newly created deck in the database
     * @param information Name of the deck and its categories
     */
    private boolean saveNewDeck(List<Object> information) {
        logger.debug("saveNewDeck()");

        String deckName = (String) information.get(0);
        String category = (String) information.get(1);

        if(packetController.sendNewDeck(deckName, category)){
            updateDecks();
            return true;
        }else{
            PopUp.showError(ConstServerError.DECK_EXIST);
            return false;
        }
    }

    /**
     * This method is called when a card is created and added to a deck
     * @param information Contains name of the deck, category, question, answer, evaluation, typeOfCard
     * @return Returns true if the card was successfully created and added to a deck
     */
    private boolean saveNewCard(List<Object> information) {
        logger.debug("saveNewCard()");

        String currentDeckName = getCurrentDeckName();
        String category = (String)information.get(0);
        String question = (String)information.get(1);
        String answer = (String)information.get(2);
        String evaluation = (String)information.get(3);
        String typeOfCard = (String)information.get(4);

        List<String> deckCategory = getDeckCategory(currentDeckName);
        if(!checkCardCategory(deckCategory, category)){
            PopUp.showError(ConstAppError.CARD_CATEGORY);
            return false;
        }else if(packetController.sendNewCard(currentDeckName, category, question, answer, evaluation, typeOfCard)){
            updateDecks();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param deckCategory Category of the deck
     * @param cardCategory Category of the card
     * @return True if the card match one of the deck category(ies)
     */
    private boolean checkCardCategory(List<String> deckCategory, String cardCategory){
        for (String category : deckCategory){
            if (Objects.equals(category, cardCategory)){
                return true;
            }
        }
        return false;
    }

    /**
     * Update the token
     * @param information New amount of token
     */
    private void updateToken(List<Object> information){
        this.packetController.updateToken((int)information.get(0));
    }
}
