package ulb.infof307.g10.app.views.Card.LaTex_HTML.util;

import javafx.scene.web.WebView;
import ulb.infof307.g10.constante.appConst.ConstCardType;

import java.io.IOException;

/**
 * This class is used to load a card with all the webview
 */
public class Loader {

    /**
     * This method is used to load a card with all the webview
     * @param typeOfCard the type of the card
     * @param question the question of the card
     * @param answer the answer of the card
     * @param questionWebView the webview of the question
     * @param answerWebView the webview of the answer
     */
    public static void load(int typeOfCard
            , String question
            , String answer
            , WebView questionWebView
            , WebView answerWebView) throws IOException {

        if (typeOfCard == ConstCardType.LATEX_TYPE){
            LatexViewer latexViewer = new LatexViewer();
            question = latexViewer.getLaTexImageFromFile(question);
            answer = latexViewer.getLaTexImageFromFile(answer);
        } else if (typeOfCard == ConstCardType.HTML_TYPE){
            FileUtil fileUtil = new FileUtil();
            question = fileUtil.readFromFile(question);
            answer = fileUtil.readFromFile(answer);
        }

        WebviewHandler.update(question, questionWebView);
        WebviewHandler.update(answer, answerWebView);
    }
}
