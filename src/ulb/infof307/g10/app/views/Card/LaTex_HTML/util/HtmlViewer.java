package ulb.infof307.g10.app.views.Card.LaTex_HTML.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class is used to display a html code in a BufferedImage
 */
public class HtmlViewer {

    private static final Logger logger = LoggerFactory.getLogger(HtmlViewer.class);

    /**
     * This method takes a html code and returns a BufferedImage
     * @param htmlCode the html code to display
     * @return the BufferedImage
     */
    public BufferedImage getBufferedImageFromHtml(String htmlCode) {
        // loads the html code into the webview and takes a screenshot of it
        logger.debug("getBufferedImageFromHtml()");

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(htmlCode);
        editorPane.setSize(500, 500);

        Rectangle rect = editorPane.getBounds();
        BufferedImage image = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        editorPane.paint(g);
        g.dispose();
        return image;
    }

    /**
     * This method takes a file path and returns a BufferedImage
     * @param fileToRead the file path to read
     * @return the BufferedImage
     */
    public BufferedImage getBufferedImageFromFile(String fileToRead) {
        FileUtil fileUtil = new FileUtil();
        String fileContent = fileUtil.readFromFile(fileToRead);
        return getBufferedImageFromHtml(fileContent);
    }
}


