package ulb.infof307.g10.app.views.Card.LaTex_HTML.util;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javax.swing.*;
import java.io.IOException;

import ulb.infof307.g10.app.views.Util.PopUp;


/**
 * This class is used to display a html code in a WebView
 */
public class WebviewHandler extends JFrame {

    public static void update(String content, WebView webView) {
        webView.getEngine().loadContent(content);
    }

    /**
     * This method is used to update a WebView
     * @param content the content to display
     * @param webView the WebView
     * @param mode the mode of the card
     */
    public static void update(String content, WebView webView, int mode) throws IOException {

        String webviewLoadableContent;

        if (mode == 2) {
            LatexViewer latexViewer = new LatexViewer();
            webviewLoadableContent = latexViewer.getLaTexImage(content);
        } else {
            webviewLoadableContent = content;
        }
        webView.getEngine().loadContent(webviewLoadableContent);
    }

    /**
     * This method is used to set a listener on a TextArea
     * @param textArea the TextArea
     * @param webView the WebView
     * @param mode the mode of the card
     */
    public static void setListener(TextArea textArea, WebView webView, int mode) {

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                WebviewHandler.update(textArea.getText(), webView, mode);
            } catch (IOException e) {
                PopUp.showError("Error", "Error while loading the webview", e.getMessage());
            }
        });
    }

    /**
     * This method is used to set a listener on a TextField with a mode
     * @param textField the TextField
     * @param webView the WebView
     * @param mode the mode of the card
     */
    public static void setListener(TextField textField, WebView webView, int mode) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                WebviewHandler.update(textField.getText(), webView, mode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method is used to set a listener on a TextField without a mode
     * @param textField the TextArea
     * @param webView the WebView
     */
    public static void setListener(TextField textField, WebView webView) {
        textField.textProperty().addListener((observable, oldValue, newValue)
                -> WebviewHandler.update(textField.getText(), webView));
    }
}
