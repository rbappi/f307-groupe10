package ulb.infof307.g10.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.FileUtil;
import ulb.infof307.g10.constante.appConst.ConstCardType;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.LatexViewer;
import ulb.infof307.g10.app.views.Card.LaTex_HTML.util.HtmlViewer;


/**
 * Class that export a deck as "name of deck".CSV and "name of deck".PDF.
 */
public class DeckExporter {

    private final Logger logger = LoggerFactory.getLogger(DeckExporter.class);
    private List<Integer> currentPosition;
    private static final int SPACING = 20;

    /**
     * Methode that export a deck in CSV file.
     * @param fileName The file name is the deck name
     * @param data The data are the information of the cards that the deck contains
     * @param filePath The file path is the path of the file
     */
    public void exportToCsv(String fileName, List<String[]> data, String filePath) throws IOException {
        logger.info("Exporting deck to csv file");
        FileUtil fileUtil = new FileUtil();
        logger.debug("File path: {}{}{}.csv", filePath, fileUtil.getFileSeparator(), fileName);

        String pathName = filePath + fileUtil.getFileSeparator() + fileName + ".csv";
        File file = new File(pathName);
        logger.debug("File path: {}", file.getAbsolutePath());
        makeCSVFile(file);
        try(FileWriter fileWriter = new FileWriter(pathName)) {
            // Define header row
            String[] headerRow = {"Question", "Answer", "Category", "Evaluation", "Type of card"};
            fileWriter.append(String.join(",", headerRow)).append("\n");
            // Write data rows
            for (String[] row : data) {
                fileWriter.append(String.join(",", row)).append("\n");
            }
            fileWriter.flush();
        }
        logger.info("Deck successfully exported to csv file");
    }

    /**
     * Methode that create a CSV file
     * @param file The file is the file that we want to create
     */
    private void makeCSVFile(File file) {
        try {
            if (file.createNewFile()) {
                logger.debug("File created successfully!");
            } else {
                logger.debug("File already exists.");
            }
        } catch (IOException e) {
            logger.debug("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }

    /**
     * Methode that export a deck into PDF file.
     * @param fileName The file name is the deck name
     * @param data The data are the information of the cards that the deck contains
     * @param filePath The file path is the path of the file
     * @throws IOException The IOException is an exception that is thrown when an I/O operation fails
     */
    public void importToPDF(String fileName, List<String[]> data, String filePath) throws IOException {
        logger.info("Exporting deck to PDF file");
        FileUtil fileUtil = new FileUtil();

        logger.debug("File path: {}{}{}.pdf", filePath, fileUtil.getFileSeparator(), fileName);

        try (PDDocument document = new PDDocument()) {
            // create a new PDF document

            PDFont subtitleFont = PDType1Font.HELVETICA;
            float subtitleFontSize = 14;

            for (String[] row : data) {
                String typeOfCard = row[4];
                String category = row[2];
                String evaluation = row[3];
                String question = row[0];
                String answer = row[1];

                // create a new page for the card
                PDPage page = new PDPage();
                document.addPage(page);

                // define content stream for writing to the page
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                addPdfCategory(category, contentStream);

                addPdfEvaluation(evaluation, contentStream);

                currentPosition = Arrays.asList(50, 660);
                // write question
                addTitle("Question", subtitleFont, subtitleFontSize, contentStream);
                addCardContent(typeOfCard, question, document, contentStream);

                // write answer
                addTitle("Answer", subtitleFont, subtitleFontSize, contentStream);
                addCardContent(typeOfCard, answer, document, contentStream);

                // close the content stream
                contentStream.close();
            }
            // save the document and close it
            document.save(filePath + fileUtil.getFileSeparator() + fileName + ".pdf");

        }
        logger.info("Deck successfully exported to PDF file");
    }

    /**
     * Write the content of a card into one page
     * @param typeOfCard The type of the card (normal, HTML, Latex)
     * @param content The question or answer
     * @param document The PDF
     * @param contentStream A page of the PDF
     */
    private void addCardContent(String typeOfCard,
                                String content,
                                PDDocument document,
                                PDPageContentStream contentStream) throws IOException{

        if (Integer.parseInt(typeOfCard) == ConstCardType.LATEX_TYPE) {
            addLaTeXContent(content, contentStream, document);

        } else if (Integer.parseInt(typeOfCard) == ConstCardType.HTML_TYPE) {
            logger.info("HTML type");
            addHtmlContent(content, contentStream, document);

        } else if (Integer.parseInt(typeOfCard) == ConstCardType.NORMAL_TYPE){
            logger.info("Text type");
            addNormalContent(content, contentStream);
        }
    }

    /**
     * Write only normal card into the page of the PDF
     * @param content Contains the information of a card
     * @param contentStream A page of the PDF
     */
    private void addNormalContent(String content, PDPageContentStream contentStream) throws IOException {
        PDFont font = PDType1Font.TIMES_ROMAN;
        float fontSize = 12;

        addTitle(content, font, fontSize, contentStream);
    }

    /**
     * Write an imagine into the page of the PDF
     * @param image An image that contains the card information
     * @param contentStream A page of the PDF
     * @param document The PDF
     */
    private void addImage(BufferedImage image,
                          PDPageContentStream contentStream,
                          PDDocument document) throws IOException {
        logger.debug("addImage");
        PDImageXObject pdImage = JPEGFactory.createFromImage(document, image);
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int currentPositionY = currentPosition.get(1) - imageHeight;
        contentStream.drawImage(pdImage
                , currentPosition.get(0)
                , currentPositionY
                , imageWidth, imageHeight);
        currentPosition.set(1, currentPosition.get(1) - imageHeight - SPACING);
    }

    /**
     * Write only HTML card into the page of the PDF
     * @param content Contains the information of a card
     * @param contentStream A page of the PDF
     * @param document The PDF
     */
    private void addHtmlContent(String content,
                                PDPageContentStream contentStream,
                                PDDocument document) throws IOException {
        logger.debug("addHtmlContent");
        logger.debug("Content: {}", content);
        HtmlViewer htmlViewer = new HtmlViewer();
        BufferedImage image = htmlViewer.getBufferedImageFromFile(content);
        addImage(image, contentStream, document);
    }

    /**
     * Write only Latex card into the page of the PDF
     * @param content Contains the information of a card
     * @param contentStream A page of the PDF
     * @param document The PDF
     */
    private void addLaTeXContent(String content,
                                 PDPageContentStream contentStream,
                                 PDDocument document) throws IOException {
        logger.info("Latex type");
        LatexViewer latexViewer = new LatexViewer();
        BufferedImage image = latexViewer.getBufferedImageFromFile(content);
        addImage(image, contentStream, document);
    }

    /**
     * Write the evaluation of a card into the page of the PDF
     * @param evaluation The evaluation of a card
     * @param contentStream The page of the PDF
     */
    private void addPdfEvaluation(String evaluation, PDPageContentStream contentStream) throws IOException {
        PDFont evaluationFont = PDType1Font.TIMES_ROMAN;
        float evaluationFontSize = 12;

        contentStream.beginText();
        contentStream.setFont(evaluationFont, evaluationFontSize);
        contentStream.newLineAtOffset(50, 730);
        contentStream.showText("Evaluation:");
        contentStream.newLineAtOffset(50, 710);
        contentStream.showText(evaluation);
        contentStream.endText();
    }

    /**
     * Write the category of a card into the page of the PDF
     * @param category The category of a card
     * @param contentStream The page of the PDF
     */
    private void addPdfCategory(String category, PDPageContentStream contentStream) throws IOException {
        // write category title
        PDFont categoryFont = PDType1Font.HELVETICA_BOLD;
        float categoryFontSize = 18;

        contentStream.beginText();
        contentStream.setFont(categoryFont, categoryFontSize);
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText(category);
        contentStream.endText();
    }

    /**
     * Write the subtitle into the page of the PDF
     * @param subtitle The title
     * @param subtitleFont The font of the title
     * @param subtitleFontSize The size if the title
     * @param contentStream The page of the PDF
     */
    private void addTitle(String subtitle,
                          PDFont subtitleFont,
                          float subtitleFontSize,
                          PDPageContentStream contentStream) throws IOException {
        currentPosition.set(1, currentPosition.get(1) - SPACING);
        contentStream.beginText();
        contentStream.setFont(subtitleFont, subtitleFontSize);
        contentStream.newLineAtOffset(currentPosition.get(0), currentPosition.get(1));
        contentStream.showText(subtitle);
        contentStream.endText();
    }


    /**
     * Export a deck
     * @param deckName The name of the deck
     * @param deckData The data of the cards ()
     * @param path The path of the file
     * @param extension The extension of the export (PDF or CSV)
     * @return true if the export succeeded
     */
    public boolean export(String deckName, List<String[]> deckData, String path, String extension) {
        logger.debug("export()");
        switch (extension) {
            case "csv" -> {
                try {
                    exportToCsv(deckName, deckData, path);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
            case "pdf" -> {
                try {
                    importToPDF(deckName, deckData, path);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
            default -> logger.error("Invalid file extension");
        }
        return false;
    }
}


