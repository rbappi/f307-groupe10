package ulb.infof307.g10.network.connection;

import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;
import ulb.infof307.g10.constante.networkConst.ConstServerSuccess;
import ulb.infof307.g10.network.database.DatabaseCardManager;
import ulb.infof307.g10.network.database.DatabaseDeckManager;
import ulb.infof307.g10.network.database.DatabaseStoreManager;
import ulb.infof307.g10.network.packet.Packet;

import java.sql.SQLException;

/**
 * This class provides methods for managing interactions related to decks and cards.
 * It handles actions such as deck and card creation, deleting cards and decks, buying decks,
 * getting deck and card counts, and managing the store.
 */
public class DBDeckAndCardInteraction extends DBInteraction {

    private final DatabaseCardManager databaseCardManager = new DatabaseCardManager();
    private final DatabaseDeckManager databaseDeckManager = new DatabaseDeckManager();
    private final DatabaseStoreManager databaseStoreManager = new DatabaseStoreManager();


    /**
     * Constructor for the DBDeckAndCardInteraction class.
     *
     * @param content The data that will be processed by the DBDeckAndCardInteraction.
     */
    DBDeckAndCardInteraction(String content) {
        this.content = parser.parse(content);
    }

    /**
     * Manages the deck creation request from the client.
     *
     * @return Packet Contains the result of the deck creation attempt.
     */
    public Packet manageDeckCreation() {
        String username = content[0];
        String deckName  = content[1];
        String score= content[2];
        try {
            boolean stateResult = databaseDeckManager.createDeck(username, deckName,score);
            return getStatePacket(stateResult, ConstServerSuccess.DECK_CREATED, ConstServerError.DECK_EXIST);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the creation of a new category.
     *
     * @return Packet Contains the status of the category creation request.
     */
    public Packet manageCategoryCreation() {
        String username = content[0];
        String deckName = content[1];
        String categoryName = content[2];

        boolean isCategoryCreated = databaseDeckManager.createCategory(username, deckName, categoryName);

        return getStatePacket(isCategoryCreated, ConstServerSuccess.CATEGORY_CREATED, ConstServerError.CATEGORY_EXIST);
    }

    /**
     * Manages the creation of a new card.
     *
     * @return Packet Contains the status of the card creation request.
     */
    public Packet manageCardCreation() {
        String username = content[0];
        String deckName = content[1];
        String category = content[2];
        String question = content[3];
        String answer = content[4];
        String evaluation = content[5];
        String typeOfCard = content[6];

        boolean isCardCreated = databaseCardManager.createCard(username, deckName, category, question, answer, evaluation, typeOfCard);

        return getStatePacket(isCardCreated, ConstServerSuccess.CARD_CREATED, ConstServerError.CARD_EXIST);
    }

    /**
     * Manages the deletion of a deck.
     *
     * @return Packet Contains the status of the deck deletion request.
     */
    public Packet manageDeckDelete() {
        String username = content[0];
        String deckName = content[1];

        try {
            boolean isDeckDeleted = databaseDeckManager.deleteDecks(username, deckName);
            return getStatePacket(isDeckDeleted, ConstServerSuccess.DELETE_DECK, ConstServerError.DECK_NOT_EXIST);
        } catch (SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the purchase of a deck.
     *
     * @return Packet Contains the status of the deck purchase request.
     */
    public Packet manageBuyDeck() {
        String deckName = content[0];
        String sellerUsername = content[1];
        String buyerUsername = content[2];

        try{
            boolean isDeckBought = databaseStoreManager.buyDeck(deckName, sellerUsername, buyerUsername);
            return getStatePacket(isDeckBought, ConstServerSuccess.BUY_DECK, ConstServerError.DECK_NOT_EXIST);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Retrieves the count of cards in a specific deck.
     *
     * @return Packet Contains the count of cards in the deck.
     */
    public Packet manageGetCardCountInDeck() {
        String deckName = content[0];
        String username = content[1];

        try {
            int cardCount = databaseDeckManager.getAmountOfCardsInDeck(deckName, username);
            return new Packet(ConstServerRequest.SUCCESS, String.valueOf(cardCount));}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the updating of a store.
     * @return Packet Contains the status of the store update request.
     */
    public Packet manageUpdateStore() {
        String deckName = content[0];
        String username = content[1];
        int price = Integer.parseInt(content[2]);
        boolean inStore = Boolean.parseBoolean(content[3]);
        boolean isStoreUpdated = databaseStoreManager.hasStoreUpdated(deckName, username, price, inStore);
        return getStatePacket(isStoreUpdated, ConstServerSuccess.HAS_STORE_UPDATED, ConstServerError.STORE_UPDATE_ERROR);
    }

    /**
     * Manages the request for deleting a category.
     * @return Packet Contains the result of the category deletion attempt.
     */
    public Packet manageCategoryDelete() {
        String username = content[0];
        String categoryName = content[1];

        try{
            boolean isCategoryDeleted = databaseDeckManager.deleteCategory(username, categoryName);
            return getStatePacket(isCategoryDeleted, ConstServerSuccess.DELETE_CATEGORY, ConstServerError.CATEGORY_NOT_EXIST);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the card deletion request from the client.
     * @return Packet Contains the result of the card deletion attempt.
     */
    public Packet manageCardDelete() {
        int cardId = Integer.parseInt(content[0]);

        try {
            boolean isCardDeleted = databaseCardManager.deleteCards(cardId);
            return getStatePacket(isCardDeleted, ConstServerSuccess.DELETE_CARD, ConstServerError.CARD_NOT_EXIST);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the request for getting a deck.
     * @return Packet Contains the requested deck or an error message.
     */
    public Packet manageGetDeck() {
        String username = content[0];

        try {
            String[][] deckData = databaseDeckManager.getDeck(username);
            String messagePacket = parser.inverseParse(deckData);
            return new Packet(ConstServerRequest.SUCCESS, messagePacket);}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the request for getting the deck in the store.
     * @return Packet Contains the deck in the store or an error message.
     */
    public Packet manageGetDeckInStore() {
        String username = content[0];
        String[][] deckData = databaseStoreManager.getDeckInStore(username);
        String messagePacket = parser.inverseParse(deckData);

        return new Packet(ConstServerRequest.SUCCESS, messagePacket);
    }

    /**
     * Manages the request to get a card.
     * @return Packet Contains the requested card or an error message.
     */
    public Packet manageGetCard() {
        String username = content[0];
        String deckName = content[1];

        try {
            String[][] cardList = databaseCardManager.getCards(username, deckName);
            return new Packet(ConstServerRequest.SUCCESS, parser.inverseParse(cardList));}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the request to get categories of a deck.
     *
     * @return Packet Contains the categories of a deck or an error message.
     */
    public Packet manageGetCategoriesOfDeck() {
        String username = content[0];
        String deckName = content[1];

        try {
            String[] categories = databaseStoreManager.getCategory(username, deckName);
            return new Packet(ConstServerRequest.SUCCESS, parser.inverseParse(categories));
        }
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the request to get cards of a category.
     *
     * @return Packet Contains the cards of a category or an error message.
     */
    public Packet manageGetCardsOfCategory() {
        String username = content[0];
        String categoryName = content[1];

        try {
            String[][] cardList = databaseCardManager.getCardFromCategory(username, categoryName);
            return new Packet(ConstServerRequest.SUCCESS, parser.inverseParse(cardList));}
        catch(SQLException e) {
            return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_DATABASE);
        }
    }

    /**
     * Manages the request to modify a card.
     *
     * @return Packet Contains the result of the card modification attempt.
     */
    public Packet manageModifiedCard() {
        int cardId = Integer.parseInt(content[0]);
        String deckName = content[1];
        String category = content[2];
        String newQuestion = content[3];
        String newAnswer = content[4];
        String newEvaluation = content[5];

        boolean isCardUpdated = databaseCardManager.updateCard(cardId, deckName, category, newQuestion, newAnswer, newEvaluation);
        return getStatePacket(isCardUpdated, ConstServerSuccess.MODIFIED_CARD, ConstServerError.CARD_NOT_EXIST);
    }
}
