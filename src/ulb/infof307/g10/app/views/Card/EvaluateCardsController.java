package ulb.infof307.g10.app.views.Card;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.app.models.Card;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.Loader;
import ulb.infof307.g10.app.views.Util.PopUp;

/**
 * This class is the FXML controller of the evaluate card window
 */
public class EvaluateCardsController extends AbstractEditorController{

    private int position = 0;
    private Deck deck;
    private int index = 0;
    private List<Card> cards = new ArrayList<>();
    private DeckControllerInterface controller;
    private final Hashtable<String, String> evaluationDict = new Hashtable<>(); // Dictionary to convert evaluation to number
    private static final int WINDOW = EVALUATE_CARDS_WINDOW;
    @FXML
    private WebView questionWebView;
    @FXML
    private WebView answerWebView;
    @FXML
    private ComboBox<String> evaluationBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button nextCardButton;

    public void setController(AbstractController mainController) {
        this.controller = (DeckControllerInterface) mainController;
    }

    @Override
    public void setDeck(Deck deck) {
        setEvaluationBox();
        this.deck = deck;
        this.cards = deck.getListOfCards();
        this.index = this.cards.size();
        try {
            this.loadCard();
        }catch (IOException e){
            PopUp.showError(ConstAppError.CARD_LOADING);
        }
    }

    private void setEvaluationBox(){
        String[] eval = {"Very Bad", "Bad", "Average", "Good", "Very Good"}; // Evaluation
        int index = 1;
        ObservableList<String> evaluation = FXCollections.observableArrayList();
        evaluation.addAll(eval);
        for(String content : eval){
            evaluationDict.put(content, Integer.toString(index));
            index++;
        }
        this.evaluationBox.setItems(evaluation);
    }

    @FXML
    protected void onAnswerClicked() {
        this.answerWebView.setVisible(true);
        this.saveButton.setVisible(true);
    }

    @FXML
    protected void onReturnClicked() {
        List<Object> information = new ArrayList<>();
        information.add(DECK_MODE_WINDOW);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
    }

    @FXML
    protected void onNextCardClicked() throws IOException {
        this.position += 1;
        this.loadCard();
    }

    @FXML
    protected void onSaveEvaluationClicked(){
        String value =  evaluationBox.getValue();
        if(value != null && !value.isEmpty()) {
            nextCardButton.setVisible(true);
            Card card = cards.get(this.position);
            String evaluation = evaluationDict.get(value);
            card.setEvaluation(Integer.parseInt(evaluation));
        }
    }

    @FXML
    protected  void loadCard() throws IOException {
        if (this.position < this.index){
            this.questionWebView.setVisible(true);
            this.answerWebView.setVisible(false);
            int typeOfCard = cards.get(this.position).getTypeOfCard();
            String question = cards.get(this.position).getQuestion();
            String answer = cards.get(this.position).getAnswer();
            Loader.load(typeOfCard, question, answer, questionWebView, answerWebView);
            this.nextCardButton.setVisible(false);
        }else {
            List<Object> information = new ArrayList<>();
            information.add(deck);
            this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SET, information);
        }
    }
}