package ulb.infof307.g10.app.views.Card;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import ulb.infof307.g10.app.models.Card;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.Loader;
import ulb.infof307.g10.app.views.Util.PopUp;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;


/**
 * FXML controller for the card study mode window
 */
public class CardStudyModeController extends AbstractEditorController{

    private List<Pair<Integer,Integer>> pairList = new ArrayList<>(); // list of pair (index, evaluation)
    private DeckControllerInterface controller;
    private int actualPosition = 0;
    private List<Card> cards = new ArrayList<>();
    private int index = 0;
    private static final int WINDOW = CARD_STUDY_MODE_WINDOW;
    @FXML
    private WebView questionWebView;
    @FXML
    private WebView answerWebView;
    @FXML
    private Button nextCardButton;
    @FXML

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface) controller;
    }

    @Override
    public void setDeck(Deck deck) {
        this.cards = deck.getListOfCards();
        this.index = this.cards.size();
        setCardsReversion();
        try {
            this.loadCard();
        }catch (IOException e){
            PopUp.showError(ConstAppError.CARD_LOADING);
        }
    }

    public void setCardsReversion(){
        // creates a list of pair (index, evaluation) and sorts it by evaluation
        for (int i=0;i<this.cards.size();i++){
            Pair<Integer, Integer> pair = new Pair<>(i, cards.get(i).getEvaluation());
            pairList.add(pair);
        }
        pairList.sort(Comparator.comparing(Pair::getValue));
    }

    @FXML
    protected void onAnswerClicked(){
        this.answerWebView.setVisible(true);
        this.nextCardButton.setVisible(true);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(DECK_MODE_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onNextCardClicked() throws IOException {
        this.actualPosition += 1;
        this.loadCard();
    }

    @FXML
    protected  void loadCard() throws IOException {
        if (this.actualPosition < this.index){
            this.questionWebView.setVisible(true);
            this.answerWebView.setVisible(false);
            Pair<Integer,Integer> pair = pairList.get(this.actualPosition);
            Integer key = pair.getKey();
            Card card = cards.get(key);
            String question = card.getQuestion();
            String answer = card.getAnswer();
            int typeOfCard = card.getTypeOfCard();
            Loader.load(typeOfCard, question, answer, questionWebView, answerWebView);
            this.nextCardButton.setVisible(false);
        }else {
            int token = this.cards.size()/10;
            List<Object> information = new ArrayList<>();
            information.add(token);
            this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SET, information);
        }
    }
}
