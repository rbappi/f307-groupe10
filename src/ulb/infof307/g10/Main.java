package ulb.infof307.g10;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ulb.infof307.g10.app.controllers.MainController;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.debug("Main()");
        Application.launch(MainController.class ,args);
    }
}
