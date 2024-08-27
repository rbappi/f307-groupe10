package ulb.infof307.g10.app.controllers;


import ulb.infof307.g10.app.models.Deck;

import java.util.List;

public interface DeckControllerInterface{

    void buttonHandler(int window, String action, List<Object> information);
    List<Deck> getDecks();
    Deck getCurrentDeck();
}
