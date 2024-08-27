package ulb.infof307.g10.network.connection;

import ulb.infof307.g10.constante.networkConst.ConstServerError;
import ulb.infof307.g10.constante.networkConst.ConstServerRequest;
import ulb.infof307.g10.constante.networkConst.ConstSizeForPacketContent;
import ulb.infof307.g10.network.packet.Packet;
import ulb.infof307.g10.network.packet.PacketHandler;

import java.net.Socket;
import java.util.Objects;

public class ServerClientDBConnection implements Runnable {
    /**
     * This class is the main class to handle Client - Server interaction
     * Runnable means the code in the function "run" is automatically executed when a Thread is created.
     * In consequence, you ONLY need to call the constructor in the class Server.
     *
     * It is in this class that you receive or send packets to the clients & manage their interactions.
     * Take a close look to the function "requestHandler" which defines the behaviour to take when a packet arrives
     * If you want to modify the behaviour of Packets, go instead to the class PacketHandler
     **/
    private boolean isStopped= false;
    private final PacketHandler serverPacketHandler;

    public ServerClientDBConnection(Socket clientsocket) {
        this.serverPacketHandler = new PacketHandler(clientsocket);
    }

    public void run() {
        //**
        // This function is executed when a thread is created.
        // It receives packets from the client, call the functions to answer their requests & send packets.
        //**
        try {
            while(!isStopped) {
                Packet clientPacket = serverPacketHandler.receivePacket();

                Packet serverPacket = requestHandler(clientPacket);
                if(!(serverPacketHandler.sendPacket(serverPacket))) {
                    isStopped = true;
                }
            }

        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private Packet requestHandler(Packet clientPacket) {
        //**
        // This function identifies the request of the user and call functions corresponding to the request
        // If you want to create a new request, you need to add another request in the class appConst
        // You will also need to create a function corresponding to the request below this class
        //**

        String[] parsedPacket = serverPacketHandler.parsePacket(clientPacket);
        DBDeckAndCardInteraction dbDeckAndCardInteraction = new DBDeckAndCardInteraction(clientPacket.getContent());
        DBAccountInteraction dbAccountInteraction = new DBAccountInteraction(clientPacket.getContent());

        switch (clientPacket.getRequest()) {
            case ConstServerRequest.ACCOUNT_CREATION -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.ACCOUNT_CREATION_SIZE)) {
                    return dbAccountInteraction.manageAccountCreation();}
            }
            case ConstServerRequest.LOGIN -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.LOGIN_SIZE))
                    return dbAccountInteraction.manageLogin();
            }
            case ConstServerRequest.CHANGING_PASSWORD -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.CHANGING_PASSWORD_SIZE))
                    return dbAccountInteraction.managePassword();
            }
            case ConstServerRequest.CREATE_CARD -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.CREATE_CARD_SIZE))
                    return dbDeckAndCardInteraction.manageCardCreation();
            }
            case ConstServerRequest.CREATE_DECK -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.CREATE_DECK_SIZE))
                    return dbDeckAndCardInteraction.manageDeckCreation();
            }
            case ConstServerRequest.CREATE_CATEGORY -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.CREATE_CATEGORY_SIZE))
                    return dbDeckAndCardInteraction.manageCategoryCreation();
            }
            case ConstServerRequest.DELETE_DECK -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.DELETE_DECK_SIZE))
                    return dbDeckAndCardInteraction.manageDeckDelete();
            }
            case ConstServerRequest.DELETE_CATEGORY -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.DELETE_CATEGORY_SIZE))
                    return dbDeckAndCardInteraction.manageCategoryDelete();
            }
            case ConstServerRequest.DELETE_CARD -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.DELETE_CARD_SIZE))
                    return dbDeckAndCardInteraction.manageCardDelete();
            }
            case ConstServerRequest.GET_DECK -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_DECK_SIZE))
                    return dbDeckAndCardInteraction.manageGetDeck();
            }
            case ConstServerRequest.GET_DECK_IN_STORE -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_DECK_IN_STORE_SIZE))
                    return dbDeckAndCardInteraction.manageGetDeckInStore();
            }
            case ConstServerRequest.BUY_DECK -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.BUY_DECK_SIZE))
                    return dbDeckAndCardInteraction.manageBuyDeck();
            }
            case ConstServerRequest.GET_CARD_COUNT_IN_DECK -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_CARD_COUNT_IN_DECK_SIZE))
                    return dbDeckAndCardInteraction.manageGetCardCountInDeck();
            }
            case ConstServerRequest.GET_CARD -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_CARD_SIZE)){
                    return dbDeckAndCardInteraction.manageGetCard();}
            }
            case ConstServerRequest.GET_CATEGORIES_DECK -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_CATEGORIES_DECK_SIZE)){
                    return dbDeckAndCardInteraction.manageGetCategoriesOfDeck();}
            }
            case ConstServerRequest.GET_CATEGORIES_CARD -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_CATEGORIES_CARD_SIZE)){
                    return dbDeckAndCardInteraction.manageGetCardsOfCategory();}
            }
            case ConstServerRequest.MODIFIED_CARD -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.MODIFIED_CARD_SIZE)){
                    return dbDeckAndCardInteraction.manageModifiedCard();}
            }
            case ConstServerRequest.GET_AMOUNT_TOKENS ->  {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.GET_AMOUNT_TOKENS_SIZE)){
                    return dbAccountInteraction.manageGetAmountTokens();}
            }
            case ConstServerRequest.SET_USER_DECK_SCORE -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.SET_USER_DECK_SCORE_SIZE)){
                    return dbAccountInteraction.manageSetDeckScore();}
            }
            case ConstServerRequest.UPDATE_AMOUNT_TOKENS -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.UPDATE_AMOUNT_TOKENS_SIZE)){
                    return dbAccountInteraction.manageUpdateAmountTokens();}
            }
            case ConstServerRequest.UPDATE_STORE -> {
                if(Objects.equals(parsedPacket.length, ConstSizeForPacketContent.UPDATE_STORE_SIZE)){
                    return dbDeckAndCardInteraction.manageUpdateStore();}
            }
            default -> {
                return new Packet(ConstServerRequest.EMPTY_REQUEST, "Your request has not been recognised");
            }
        }
        return new Packet(ConstServerRequest.FAILURE, ConstServerError.ERROR_CONTENT_SIZE);
    }
}

