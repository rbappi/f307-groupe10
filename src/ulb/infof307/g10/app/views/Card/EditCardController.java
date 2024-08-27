package ulb.infof307.g10.app.views.Card;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ulb.infof307.g10.abstractClass.AbstractController;
import ulb.infof307.g10.app.controllers.DeckControllerInterface;
import ulb.infof307.g10.constante.appConst.ConstAppButtonAction;
import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.app.models.Card;
import ulb.infof307.g10.app.models.Deck;
import ulb.infof307.g10.abstractClass.AbstractEditorController;
import ulb.infof307.g10.constante.appConst.ConstCardType;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.FileUtil;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.WebviewHandler;
import ulb.infof307.g10.app.views.Util.PopUp;

/**
 * This class is the FXML controller of the edit card window
 */
public class EditCardController extends AbstractEditorController {

    private DeckControllerInterface controller;
    private int position = 0;
    private Deck deck;
    private List<Card> cards = new ArrayList<>();
    private List<Card> cardsToDelete = new ArrayList<>();
    private static final int WINDOW = EDIT_CARD_WINDOW;
    private int index = 0;
    @FXML
    private TextField questionField;
    @FXML
    private TextField answerField;
    @FXML
    private TextField questionFieldWebView;
    @FXML
    private TextField answerFieldWebView;
    @FXML
    private WebView questionWebView;
    @FXML
    private WebView answerWebView;
    @FXML
    private Button nextCardButton;
    @FXML
    private Button saveButton;

    public void setController(AbstractController controller) {
        this.controller = (DeckControllerInterface) controller;
    }

    @Override
    public void setDeck(Deck deck) {
        this.deck = deck;
        this.cards = deck.getListOfCards();
        this.index = this.cards.size();
        this.saveButton.setVisible(false);
        this.loadCard();
    }

    @FXML
    protected void onReturnClicked() {
        String title = "Current Deck is modified";
        String message = "Do you want to return without saving your deck ? The button save will be shown at the end";
        if(PopUp.showWarning(title, message)){
            List<Object> information = new ArrayList<>();
            information.add(DECK_MODE_WINDOW);
            this.controller.buttonHandler(WINDOW, ConstAppButtonAction.CHANGE_VIEW, information);
        }
    }

    @FXML
    protected void onNextCardClicked() {
        this.saveCard();
        this.position += 1;
        this.loadCard();
    }

    @FXML
    protected void onSaveClicked() {
        this.saveCard();
        List<Object> information = new ArrayList<>(CREATE_DECK_WINDOW);
        information.add(this.cardsToDelete);
        information.add(this.deck);
        this.controller.buttonHandler(WINDOW, ConstAppButtonAction.SAVE, information);
    }

    @FXML
    protected void onDeleteCardClicked() {
        if(this.position < this.index){
            this.cardsToDelete.add(this.cards.get(this.position));
        }
        this.position += 1;
        this.loadCard();
    }

    @FXML
    protected  void loadCard() {
        if (this.position < this.index){
            Card card = cards.get(this.position);
            int typeOfCard = card.getTypeOfCard();
            String question = card.getQuestion();
            String answer = card.getAnswer();
            this.loadCardByType(typeOfCard, question, answer);
            if(Objects.equals(this.position, this.index-1)){
                // If it's the last card,
                // we hide the next card button
                // and show the save button
                this.nextCardButton.setVisible(false);
                this.saveButton.setVisible(true);
            }
        }
    }

    private void loadCardByType(int typeOfCard, String question, String answer) {
        if (typeOfCard == ConstCardType.LATEX_TYPE){
            this.switchVisibility(2);
            this.loadLatexCard(question, answer);
        } else if (typeOfCard == ConstCardType.HTML_TYPE){
            this.switchVisibility(2);
            this.loadHTMLCard(question, answer);
        } else if (typeOfCard == ConstCardType.NORMAL_TYPE){
            this.switchVisibility(1);
            this.loadNormalCard(question, answer);
        }
        else {
            PopUp.showError("Not supported yet", "Error", "Error in:\n" + this.getClass().getName());
        }
    }

    private void loadNormalCard(String question, String answer) {
        this.questionField.setText(question);
        this.answerField.setText(answer);
    }

    private void loadHTMLCard(String question, String answer) {

        this.setFieldWebView(question, answer);
        WebviewHandler.setListener(this.questionFieldWebView, this.questionWebView);
        WebviewHandler.setListener(this.answerFieldWebView, this.answerWebView);
    }

    public void loadLatexCard(String question, String answer) {
        this.setFieldWebView(question, answer);
        WebviewHandler.setListener(this.questionFieldWebView, this.questionWebView, 2);
        WebviewHandler.setListener(this.answerFieldWebView, this.answerWebView, 2);
    }

    /**
        * Saves the card in the deck following the type of card
     */
    private void saveCard(){
        Card card = this.cards.get(this.position);
        int typeOfCard = card.getTypeOfCard();

        String newQuestion = "";
        String newAnswer = "";

        if (typeOfCard == ConstCardType.HTML_TYPE || typeOfCard == ConstCardType.LATEX_TYPE) {

            newQuestion = this.questionFieldWebView.getText();
            newAnswer = this.answerFieldWebView.getText();

            FileUtil fileUtil = new FileUtil();
            fileUtil.writeIntoFile(newQuestion, card.getQuestion());
            fileUtil.writeIntoFile(newAnswer, card.getAnswer());
        }else {
            newQuestion = questionField.getText();
            newAnswer = answerField.getText();

            card.setQuestion(newQuestion);
            card.setAnswer(newAnswer);
        }
        this.cards.set(this.position, card);
    }

    /**
     * Switch the visibility of the fields
     * @param visible 1: normal card, 2: latex/html card
     */
    private void switchVisibility(int visible){
        switch (visible) {
            case 1 -> { //normal Card
                questionField.setVisible(true);
                answerField.setVisible(true);
                questionFieldWebView.setVisible(false);
                answerFieldWebView.setVisible(false);
                questionWebView.setVisible(false);
                answerWebView.setVisible(false);
            }
            case 2 -> { //latex/html card
                questionField.setVisible(false);
                answerField.setVisible(false);
                questionFieldWebView.setVisible(true);
                answerFieldWebView.setVisible(true);
                questionWebView.setVisible(true);
                answerWebView.setVisible(true);
            }
        }
    }

    /**
     * Set the content of the webview
     * @param question question file
     * @param answer answer file
     */
    private void setFieldWebView(String question, String answer){
        FileUtil fileUtil = new FileUtil();
        String questionFileContent = fileUtil.readFromFile(question);
        String answerFileContent = fileUtil.readFromFile(answer);
        this.questionFieldWebView.setText(questionFileContent);
        this.answerFieldWebView.setText(answerFileContent);
    }
}

