package ulb.infof307.g10.app.views.Card.LaTex_HTML.util;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * This class is used to display a Latex code in a BufferedImage
 */
public class LatexViewer{

    /**
     * This method takes a Latex code as a file and returns a BufferedImage by calling getBufferedImage(String text)
     * @param fileToRead the file containing the Latex code
     * @return getBufferedImage(String fileContent)
     */
    public String getLaTexImageFromFile(String fileToRead) throws IOException {
        // Used for viewing a Latex file in the CardViewer stuff
        FileUtil fileUtil = new FileUtil();
        String fileContent =  fileUtil.readFromFile(fileToRead);
        return getLaTexImage(fileContent);
    }

    /**
     * This method takes a Latex code and returns an HTML code containing the Latex image
     * @param text the Latex code to display
     * @return the HTML code
     */
    public String getLaTexImage(String text) throws IOException {
        BufferedImage image = getBufferedImage(text);
        // Convert the BufferedImage to a Base64 encoded string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        // Set the Base64 encoded string as the content of the WebView
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
        return "<img src='data:image/png;base64," + base64Image + "'/>";
    }

    /**
     * This method takes a Latex code and returns a BufferedImage
     * @param text the Latex code to display
     * @return the BufferedImage
     */
    public BufferedImage getBufferedImage(String text) {
        // Create a TeXFormula object from the inputted LaTeX code
        TeXFormula formula = new TeXFormula(text);
        // Render the TeXFormula as a [BufferedImage:
        // https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html]
        return (BufferedImage) formula.createBufferedImage(TeXConstants.STYLE_DISPLAY
                , 20, null, null);
    }

    /**
     * This method takes a file path and returns a BufferedImage
     * @param fileToRead the file path to read
     * @return the BufferedImage
     */
    public BufferedImage getBufferedImageFromFile(String fileToRead) {
        FileUtil fileUtil = new FileUtil();
        String fileContent =  fileUtil.readFromFile(fileToRead);
        return getBufferedImage(fileContent);
    }
}
