package ulb.infof307.g10.app.views.Card.LaTex_HTML.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ulb.infof307.g10.app.views.Util.PopUp;
import ulb.infof307.g10.constante.appConst.ConstAppError;
import ulb.infof307.g10.constante.appConst.ConstCardType;


/**
 * This class is used to create, modify and read files
 */
public class FileUtil {

    private final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final String questionPath = "src/ulb/infof307/g10/network/database/cards/questions/";
    private static final String answerPath = "src/ulb/infof307/g10/network/database/cards/answers/";

    public String getFileSeparator() {
        return File.separator;
    }
    /**
     * This method is the main file generator for LaTex and HTML cards
     * @param sourceString the string to write into the file
     * @param typeOfCard the type of card (LaTex or HTML)
     * @param question if the card is a question or an answer
     * @return the path of the file
     */
    public String mainFileOperation(String sourceString, int typeOfCard, Boolean question) {
        String path = fileCreation(typeOfCard, question);
        writeIntoFile(sourceString, path);
        return path;
    }

    private String determineFileExtension(int typeOfCard) {
        return switch (typeOfCard) {
            case ConstCardType.LATEX_TYPE -> ".tex";
            case ConstCardType.HTML_TYPE -> ".html";
            case ConstCardType.NORMAL_TYPE -> ".txt";
            default -> "";
        };
    }

    /**
     * This method creates only the file
     * @return the path of the file
     */
    private String fileCreation(int typeOfCard, Boolean question) {
        String fileExtension = determineFileExtension(typeOfCard);
        String filename = randomAlphanumeric();
        String path;
        if (Boolean.TRUE.equals(question)) {
            path = questionPath + filename;
        } else {
            path = answerPath + filename;
        }
        String fullPath = path + fileExtension;
        logger.debug("path: {}", fullPath);
        try {
            File newFile = new File(fullPath);
            if (newFile.createNewFile())
                logger.debug("File created: {}", newFile.getName());
            else
                logger.debug("File already exists.");
        } catch (IOException e) {
            PopUp.showError(ConstAppError.FILE_CREATION);
            logger.error("An error occurred.", e);
            logger.debug("fullPath: {}", fullPath);
            e.printStackTrace();
        }
        return fullPath;
    }

    /**
     * This method is used to write into a file
     * @param sourceString  the string to write into the file
     * @param path the path of the file
     */
    public void writeIntoFile(String sourceString, String path) {
        logger.debug("writeIntoFile()");
        try (FileWriter fileToWrite = new FileWriter(path)) {
            fileToWrite.write(sourceString);
        } catch (IOException e) {
            PopUp.showError(ConstAppError.WEBVIEW);
        }
    }

    /**
     * This method is used to read from a file
     * @param path the path of the file
     * @return the content of the file
     */
    public String readFromFile(String path){
        try{
            File file = new File(path);
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            logger.debug("content: {}", content);
            return content;
        } catch (IOException e){
            PopUp.showError(ConstAppError.READ_FILE);
        }
        return "";
    }

    /**
     * This method is used to generate a random alphanumeric string
     * @return random alphanumeric string
     */
    private String randomAlphanumeric() {
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
