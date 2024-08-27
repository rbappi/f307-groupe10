package ulb.infof307.g10.app.controllers;

import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.app.models.DeckForSale;

import java.util.List;

public interface StoreControllerInterface {

    void buttonHandler(int window, String action, List<Object> information);
    List<Deck> getDecks();
    String getUsername();
    int getTokens();
    List<DeckForSale> getDecksInStore();
    int getCardCountInDeck(String deckName, String username);
}
