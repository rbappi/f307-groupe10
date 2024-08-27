package ulb.infof307.g10.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeckImporter {

    private final Logger logger = LoggerFactory.getLogger(DeckImporter.class);

    /**
     * Import a deck from a CSV file
     * @param filePath The path of the file
     * @return The data of the deck
     */
    public List<String[]> importFromCsv(String filePath) throws IOException {
        logger.debug("importDeck()");
        logger.info("Importing deck from csv file");

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<String[]> importedData = new ArrayList<>();
        StringBuilder deckInfo = new StringBuilder(getDeckName(filePath) + ";");
        String row;
        while ((row = reader.readLine()) != null) {
            String[] rowArray = row.split(",");
            List<String> parsedDeckInfo = List.of(deckInfo.toString().split(";"));
            //Checks if category is already added and doesn't match the 3rd column of the first row.
            if(!parsedDeckInfo.contains(rowArray[2]) && !Objects.equals(rowArray[2], "Category"))
                deckInfo.append(rowArray[2]).append(";");
            importedData.add(rowArray);
        }
        importedData.add(deckInfo.toString().split(";"));
        reader.close();
        return importedData;
    }

    /**
     * @param path The path of the file
     * @return The name of the deck
     */
    private String getDeckName(String path) {
        //Get the name of the file without the extension
        File file = new File(path);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
}
