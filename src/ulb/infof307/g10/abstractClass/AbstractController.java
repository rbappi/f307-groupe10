package ulb.infof307.g10.abstractClass;

import ulb.infof307.g10.app.controllers.PacketController;
import ulb.infof307.g10.app.controllers.WindowController;

public abstract class AbstractController {
    protected PacketController packetController;
    protected WindowController windowController;

    protected AbstractController(PacketController packetController, WindowController windowController) {
        this.packetController = packetController;
        this.windowController = windowController;

    }

}
