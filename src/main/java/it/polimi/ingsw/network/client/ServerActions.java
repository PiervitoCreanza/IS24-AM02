package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.message.ErrorServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.ViewUpdateMessage;

/**
 * The ServerActions interface defines the actions that a server can perform.
 * These actions are triggered by receiving different types of messages from the client.
 */
public interface ServerActions {

    /**
     * This method is called when the server receives a request for the list of games.
     * TODO: add parameters as needed.
     */
    void receiveGameList(/* TODO: add parameters */);

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     * TODO: add parameters as needed.
     */
    void receiveGameDeleted(/* TODO: add parameters */);

    /**
     * This method is called when the server receives an updated view message.
     *
     * @param updatedView The updated view message received from the client.
     */
    void receiveUpdatedView(ViewUpdateMessage updatedView);

    /**
     * This method is called when the server receives an error message.
     *
     * @param errorMessage The error message received from the client.
     */
    void receiveErrorMessage(ErrorServerMessage errorMessage);
}