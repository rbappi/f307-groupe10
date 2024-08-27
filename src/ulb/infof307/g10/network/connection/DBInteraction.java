package ulb.infof307.g10.network.connection;

import ulb.infof307.g10.constante.networkConst.ConstServerRequest;
import ulb.infof307.g10.network.packet.Packet;
import ulb.infof307.g10.network.packet.Parse;
/**
 * The abstract class `DBInteraction` is used for all database interactions.
 * It has a parser for packet parsing and a content string array.
 */
abstract class DBInteraction {
    // A `Parse` object to handle the parsing of network packets
    protected Parse parser = new Parse();
    // An array to hold the content of the parsed network packets
    protected String[] content;

    /**
     * This function creates and returns a Packet object based on the success state.
     *
     * @param successState, messageSuccess, errorMessage
     * successState is a boolean indicating if the operation was successful.
     * messageSuccess is a string that holds the success message.
     * errorMessage is a string that holds the error message.
     *
     * @return Packet
     * Returns a new Packet object that contains either the success or failure message.
     */

    protected Packet getStatePacket(Boolean successState, String messageSuccess, String errorMessage){
        if (successState) {
            return new Packet(ConstServerRequest.SUCCESS, messageSuccess);
        }
        else {
            return new Packet(ConstServerRequest.FAILURE, errorMessage);
        }
    }
}