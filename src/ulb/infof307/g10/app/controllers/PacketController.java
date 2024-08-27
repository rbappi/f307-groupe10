package ulb.infof307.g10.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import ulb.infof307.g10.constante.appConst.ConstAppSuccess;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.app.views.Util.PopUp;
import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;
import ulb.infof307.g10.constante.networkConst.ConstServerSuccess;
import ulb.infof307.g10.network.packet.Packet;
import ulb.infof307.g10.network.packet.PacketHandler;
import ulb.infof307.g10.network.packet.Parse;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The purpose of this class is to exchange information with the server.
 */
public class PacketController {

    private static final Logger logger = LoggerFactory.getLogger(PacketController.class);

    private final Socket clientSocket;
    private final PacketHandler clientPacketHandler;
    private String username = "";
    private final Parse parser = new Parse();

    public PacketController(Socket socket){
        this.clientSocket = socket;
        this.clientPacketHandler = new PacketHandler(this.clientSocket);
    }


    /**
     * This method requests the cards in a deck from the server
     * @param deckName name of the deck that contains the cards
     * @return A string with the cards in the deck
     */
    public String askDeckCard(String deckName){
        String deckCardInfo = this.username + ";" + deckName;
        String response = sendPacket(ConstServerRequest.GET_CARD, deckCardInfo);
        if (!Objects.equals(response, ConstServerError.CONTENT_EMPTY) )return response;
        return "";
    }

    /**
     * This method is called when a new deck is created
     * @param deckName name of the deck that contains the cards
     * @param category category of the decks
     * @return true if the deck is sent successfully
     */
    public boolean sendNewDeck(String deckName, String category) {

        if (deckName.isEmpty() || category.isEmpty()) {
            PopUp.showError(ConstAppError.DECK_EMPTY);
        } else {
            String deckInfo = this.username + ";" + deckName + ";" + "0";
            String response = sendPacket(ConstServerRequest.CREATE_DECK, deckInfo);
            if(Objects.equals(response, ConstServerSuccess.DECK_CREATED)) {
                String[] categories = parser.parse(category);
                for(String content : categories){
                    String categoryDeckInfo = this.username + ";" + deckName + ";" + content;
                    sendPacket(ConstServerRequest.CREATE_CATEGORY, categoryDeckInfo);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This method is called when a new card is created
     * @param deckName Name of the deck in which the card was created
     * @param category Category of the card
     * @param question The question of the card
     * @param answer The answer of the card
     * @param evaluation The evaluation of the card
     * @param cardType The type of the card
     * @return Returns true if the card was successfully created
     */
    public boolean sendNewCard(String deckName, String category, String question, String answer, String evaluation, String cardType){

        if (category.isEmpty() || question.isEmpty() || answer.isEmpty()) {
            PopUp.showError(ConstAppError.CARD_EMPTY_FIELDS);
        } else {
            String card = category + ";" + question + ";" + answer + ";" + evaluation + ";" + cardType;
            String cardInfo = this.username + ";" + deckName + ";" + card;
            String response = sendPacket(ConstServerRequest.CREATE_CARD, cardInfo);
            return checkNewCard(response);
        }
        return false;
    }

    /**
     * This method is called after a card is created. It verifies that the card creation was successful.
     * @param response Answer received from the server after a card creation request
     * @return Returns true if the card was successfully created
     */
    private boolean checkNewCard(String response){
        if (Objects.equals(response, ConstServerSuccess.CARD_CREATED)) {
            return true;
        } else if (Objects.equals(response, ConstServerError.CARD_EXIST)) {
            PopUp.showError(ConstServerError.CARD_EXIST);
        }
        return false;
    }

    /**
     * This method is called when a user switches the password of their account
     * @param username Account username
     * @param oldPassword Old account password
     * @param newPassword New account password
     * @param infoType The type of request to send
     * @return Returns true if the change was successful
     */
    public boolean sendNewPasswordInfo(String username, String oldPassword, String newPassword, int infoType){
        String loginInfo = username + ";" + oldPassword + ";" + newPassword;
        String response = sendPacket(infoType, loginInfo);
        if (Objects.equals(response, ConstServerSuccess.PASSWORD_MODIFIED)) {
            PopUp.showSuccess(ConstAppSuccess.EDIT_PASSWORD);
            return true;
        } else if (Objects.equals(response, ConstServerError.PASSWORD)) {
            PopUp.showError(ConstAppError.EDIT_PASSWORD);
        }
        return false;
    }

    /**
     * This method is called when a user logs into or creates an account
     * @param username Account username
     * @param password Account password
     * @param infoType The type of request to send
     * @return Returns true if the login was successful
     */
    public boolean sendAccountInfo(String username, String password, int infoType){
        logger.debug("sendAccountInfo()");
        if (username.isEmpty() || password.isEmpty()) {
            PopUp.showError(ConstAppError.LOGIN_EMPTY);
        } else {
            String loginInfo = username + ";" + password;
            String response = sendPacket(infoType, loginInfo);
            return checkLoginInfo(username, response);
        }
        return false;
    }

    /**
     * This method is called during a user login or account creation. It verifies the servers answer to the users given information
     * @param username Account username
     * @param response Server answer
     * @return Returns true if the answer was positive
     */
    private boolean checkLoginInfo(String username, String response){
        if (Objects.equals(response, ConstServerSuccess.LOGIN_SUCCESS)) {
            this.username = username;
            return true;
        } else if (Objects.equals(response, ConstServerError.ACCOUNT_NOT_EXIST)) {
            PopUp.showError(ConstServerError.ACCOUNT_NOT_EXIST);
        } else if (Objects.equals(response, ConstServerError.PASSWORD)) {
            PopUp.showError(ConstServerError.PASSWORD);
        } else if (Objects.equals(response, ConstServerError.ACCOUNT_EXIST)) {
            PopUp.showError(ConstServerError.ACCOUNT_EXIST);
        } else if (Objects.equals(response, ConstServerSuccess.ACCOUNT_CREATED)) {
            PopUp.showSuccess(ConstAppSuccess.CREATE_ACCOUNT);
            return true;
        }
        return false;
    }

    /**
     * This method is called whenever a card is edited
     * @param Id Card id
     * @param deckName Name of the deck the card belongs to
     * @param category Card category
     * @param newQuestion New question of the card
     * @param newAnswer New answer of the card
     * @param newEvaluation New evaluation of the card
     */
    public void sendModifiedCard(String Id, String deckName, String category, String newQuestion, String newAnswer, String newEvaluation){
        logger.debug("sendModifierCard()");
        String cardInfo = Id + ";" + deckName + ";" + category + ";" + newQuestion + ";" + newAnswer + ";" + newEvaluation;
        sendPacket(ConstServerRequest.MODIFIED_CARD, cardInfo);
    }

    /**
     * This method is called whenever a card is deleted
     * @param cardId Card id
     * @return Returns true if card deletion was successful
     */
    public boolean deleteCard(int cardId){
        logger.debug("deleteCard()");
        String response;
        response = sendPacket(ConstServerRequest.DELETE_CARD, Integer.toString(cardId));
        return !Objects.equals(response, ConstServerSuccess.DELETE_CARD);
    }

    /**
     * This method is called whenever a deck is deleted
     * @param deckName Name of the deck
     */
    public void deleteDeck(String deckName){
        logger.debug("deleteDeck()");
        String deckInfo = this.username + ";" + deckName;
        sendPacket(ConstServerRequest.DELETE_DECK, deckInfo);
    }

    /**
     * This method gets the users decks
     * @return List of user decks
     */
    public List<Deck> askDecks() {
        return completeDeck(askDeckName());
    }

    /**
     * This method gets the decks for sale in the store
     * @return A decks that can be purchased
     */
    public String[][] askDecksInStore() {
        return askDeckNameInStore();
    }

    /**
     * This method creates a list of deck objects with id, name, categories and score
     * @param deckNames Names and categories of the decks to create
     * @return List of decks
     */
    private List<Deck> completeDeck(String[][] deckNames) {
        logger.debug("completeDeck()");
        List<Deck> decks = new ArrayList<>();
        String categoryInfo;
        String category;

        for (String[] deckName : deckNames) {
            categoryInfo = this.username + ";" + deckName[1];
            category = sendPacket(ConstServerRequest.GET_CATEGORIES_DECK, categoryInfo);
            List<String> separatedCategory = List.of(category.split(";"));
            decks.add(new Deck(deckName[0], deckName[1], separatedCategory, deckName[2]));
        }
        return decks;
    }

    /**
     * This method gets the id's, the decks names and the scores of the users decks
     * @return A String[][] each containing [[id, deck name, score of the deck], ...]
     */
    private String[][] askDeckName() {
        logger.debug("askDeckName()");
        String deckInfo = this.username;
        String response;
        response = sendPacket(ConstServerRequest.GET_DECK, deckInfo);
        if(Objects.equals(response, ConstServerError.CONTENT_EMPTY)){
            return new String[0][0];
        }
        return parser.parse(response, 3);
    }

    /**
     * This method gets the information of the decks to display in the store
     * @return String[][] containing deckName and information
     */
    private String[][] askDeckNameInStore() {
        logger.debug("askDeckNameInStore()");
        String deckInfo = this.username;
        String response;
        response = sendPacket(ConstServerRequest.GET_DECK_IN_STORE, deckInfo);
        if(Objects.equals(response, ConstServerError.CONTENT_EMPTY)){
            return new String[0][0];
        }
        return parser.parse(response, 5);
    }

    /**
     * This method gets the tokens of the user
     * @return An int containing number of user tokens
     */
    public int getAmount(){
        return Integer.parseInt(sendPacket(ConstServerRequest.GET_AMOUNT_TOKENS, this.username));
    }

    /**
     * This method changes the amount of tokens a user has
     *
     * @param newAmountToken New user token number
     */
    public void updateToken(int newAmountToken){
        String contentToSend = this.username + ";" + newAmountToken;
        sendPacket(ConstServerRequest.UPDATE_AMOUNT_TOKENS, contentToSend);
    }

    /**
     * This method sends packets to the server
     * @param infoType Type of request
     * @param packetInfo Information needed for the request
     * @return Answer to the request from the server
     */
    private String sendPacket(int infoType, String packetInfo){
        Packet packet = new Packet(infoType, packetInfo);
        this.clientPacketHandler.sendPacket(packet);
        Packet response = this.clientPacketHandler.receivePacket();
        return response.getContent();
    }

    /**
     * This method gets the name of the current user
     * @return String containing the name
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method is called whenever a deck is bought
     * @param deckName       Name of the deck
     * @param sellerUsername Name of the user account selling the deck
     */
    public void buyDeck(String deckName, String sellerUsername){
        String buyPacketInfo = deckName + ";" + sellerUsername + ";" + this.username;
        sendPacket(ConstServerRequest.BUY_DECK, buyPacketInfo);
    }

    /**
     * This method gets the number of cards in a deck
     * @param deckName Name of the deck
     * @param username Name of the user account the deck belongs to
     * @return An int containing the number of cards in a deck
     */
    public int getCardCountInDeck(String deckName,String username){
        String deckInfo = deckName + ";" + username;
        return Integer.parseInt(sendPacket(ConstServerRequest.GET_CARD_COUNT_IN_DECK, deckInfo));
    }

    /**
     * This method sets the information of a deck in display
     *
     * @param deckName Name of the deck
     * @param price    Number of tokens required to buy the deck
     * @param inStore  Whether the deck is up for sale
     */
    public void setStoreDeckInfo(String deckName, int price, boolean inStore){
        String deckInfo = deckName + ";" + this.username + ";" + price + ";" + inStore;
        sendPacket(ConstServerRequest.UPDATE_STORE, deckInfo);
    }

    /**
     * This method sets the score of a deck
     * @param deckName Name of the deck
     * @param score Score to assign to the deck
     */
    public void setScore(String deckName,String score){
        String deckInfo = deckName + ";" + this.username + ";" + score;
        sendPacket(ConstServerRequest.SET_USER_DECK_SCORE, deckInfo);
    }
}
