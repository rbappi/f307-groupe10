package ulb.infof307.g10.abstractClass;

import ulb.infof307.g10.app.models.Deck;


public abstract class AbstractEditorController {

    public abstract void setController(AbstractController controller);

    /**
     * Set the deck to be used in the free mode
     * @param deck the deck to be used
     */
    public void setDeck(Deck deck){}
}