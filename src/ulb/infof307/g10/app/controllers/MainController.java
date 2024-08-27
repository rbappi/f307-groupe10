package ulb.infof307.g10.app.controllers;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

import static ulb.infof307.g10.constante.appConst.ConstAppWindow.*;
import ulb.infof307.g10.network.connection.SocketClient;


/**
 * The main class of the application. Init the controllers.
 */
public class MainController extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    SocketClient client;

    public MainController() {
        try {
            client = new SocketClient("localhost", 8080);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) {
        logger.debug("start()");
        logger.info("Starting the application");

        PacketController packetController = new PacketController(client.getSocket());
        WindowController windowController = new WindowController(stage);

        DeckController deckController = new DeckController(packetController, windowController);
        ConnectionController connectionController = new ConnectionController(packetController,  windowController);
        StoreController storeController  = new StoreController(packetController, windowController);

        windowController.setController(deckController);
        windowController.setController(connectionController);
        windowController.setController(storeController);

        windowController.changeView(CONNECTION_MENU_WINDOW);
    }




}