package ulb.infof307.g10.app.views.Card;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.app.models.Card;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.Loader;
import ulb.infof307.g10.app.views.Util.PopUp;


/**
 * FXML controller for the card free mode window
 */
public class CardFreeModeController extends AbstractEditorController{

    private List<Pair<Integer,Integer>> pairList = new ArrayList<Pair<Integer,Integer>>(); // list of pair (index, evaluation)
    private DeckControllerInterface controller;
    private int position = 0;
    private List<Card> cards = new ArrayList<>();
    private int index = 0;
    private String scoreValue = "0";
    private String bestScoreValue = "0";
    private static final int WINDOW = CARD_FREE_MODE_WINDOW;

    @FXML
    private WebView questionView;
    @FXML
    private WebView answerView;
    @FXML
    private Label currentScoreLabel;
    @FXML
    private Label bestScoreLabel;
    @FXML
    private Button nextCardButton;
    @FXML
    public CheckBox knownAnswerBox;

    public void setController(AbstractController mainController) {
        this.controller = (DeckControllerInterface) mainController;
        initializeScore();
        listenCheckBox();
    }

    public void initializeScore() {
        bestScoreLabel.setText(this.bestScoreValue);
        currentScoreLabel.setText(this.scoreValue);
    }

    public void scoreComparison() {
        if (Integer.parseInt(this.bestScoreValue) < Integer.parseInt(this.scoreValue)){
            this.bestScoreLabel.setText(this.scoreValue);
        }
        else{
            this.bestScoreLabel.setText(this.bestScoreValue);
        }
    }

    public void listenCheckBox(){
        knownAnswerBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    knownAnswerBox.setDisable(true);
                    int value = Integer.parseInt(scoreValue);
                    value++;
                    scoreValue = Integer.toString(value);
                }
            }
        });
    }

    @Override
    public void setDeck(Deck deck) {
        this.cards = deck.getListOfCards();
        this.index = this.cards.size();
        this.bestScoreValue = deck.getScoreMax();
        scoreComparison();
        this.bestScoreLabel.setText(this.bestScoreValue);
        setCardsReversion();
        try {
            this.loadCard();
        }catch (IOException e){
            PopUp.showError(ConstAppError.CARD_LOADING);
        }
    }

    /**
     * Set the cards in the order of their evaluation
     */
    public void setCardsReversion(){
        for (int i=0;i<this.cards.size();i++){
            Pair<Integer, Integer> pair = new Pair<>(i, cards.get(i).getEvaluation());
            pairList.add(pair);
        }
        pairList.sort(Comparator.comparing(Pair::getValue));
    }

    @FXML
    protected void onAnswerClicked(){
        this.answerView.setVisible(true);
        this.nextCardButton.setVisible(true);
        scoreComparison();
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
        knownAnswerBox.setSelected(false);
        knownAnswerBox.setDisable(false);
    }

    @FXML
    protected void loadCard() throws IOException {
        if (this.position < this.index){
            this.questionView.setVisible(true);
            this.answerView.setVisible(false);
            this.currentScoreLabel.setText(this.scoreValue);
            Pair<Integer,Integer> pair = pairList.get(this.position);
            Integer key = pair.getKey();
            Card card = cards.get(key);
            String question = card.getQuestion();
            String answer = card.getAnswer();
            int typeOfCard = card.getTypeOfCard();
            scoreComparison();
            Loader.load(typeOfCard, question, answer, questionView, answerView);
            this.nextCardButton.setVisible(false);
        }else {
            int token = this.cards.size()/10;
            List<Object> information = new ArrayList<>();
            information.add(token);
            if (Integer.parseInt(this.bestScoreValue) < Integer.parseInt(this.scoreValue)){
                information.add(this.scoreValue);
            }
            else{information.add(this.bestScoreValue);
            }
            this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SET, information);
        }
    }
}
