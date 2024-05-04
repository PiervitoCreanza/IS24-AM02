package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.server.MessageHandler;
import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.ViewUpdateMessage;

/**
 * The ClientCommandMapper class is responsible for mapping server commands to updates in the view.
 * It implements the ClientActions interface.
 */
public class ClientCommandMapper implements ServerActions {
    /**
     * The MessageHandler object is used to handle messages received from the server.
     */
    private final MessageHandler<ClientMessage> messageHandler;

    /**
     * Constructs a new ClientCommandMapper object with the specified message handler.
     *
     * @param messageHandler the message handler to be used by the ClientCommandMapper
     */
    public ClientCommandMapper(MessageHandler<ClientMessage> messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * This method is called when the server receives a request for the list of games.
     * TODO: add parameters as needed.
     */
    @Override
    public void receiveGameList() {
        // TODO: Implement this method
    }

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     * TODO: add parameters as needed.
     */
    @Override
    public void receiveGameDeleted() {
        // TODO: Implement this method
    }

    /**
     * This method is called when the server receives an updated view message.
     *
     * @param updatedView The updated view message received from the client.
     */
    @Override
    public void receiveUpdatedView(ViewUpdateMessage updatedView) {
        // TODO: Implement this method
    }

    /**
     * This method is called when the server receives an error message.
     *
     * @param errorMessage The error message received from the client.
     */
    @Override
    public void receiveErrorMessage(ErrorServerMessage errorMessage) {
        // TODO: Implement this method
    }
}
