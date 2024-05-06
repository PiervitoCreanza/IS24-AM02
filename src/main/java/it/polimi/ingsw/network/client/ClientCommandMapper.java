package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.util.HashSet;

/**
 * The ClientCommandMapper class is responsible for mapping server commands to updates in the view.
 * It implements the ClientActions interface.
 */
public class ClientCommandMapper implements ServerActions {
    /**
     * The MessageHandler object is used to handle messages received from the server.
     */
    private ClientMessageHandler messageHandler;

    /**
     * Constructs a new ClientCommandMapper object with the specified message handler.
     *
     */
    public ClientCommandMapper() {
    }

    public void setMessageHandler(ClientMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * This method is called when the server receives a request for the list of games.
     * TODO: add parameters as needed.
     */
    @Override
    public void receiveGameList(HashSet<GameRecord> games) {
        System.out.println("Received games: " + games);
        // TODO: Implement this method
    }

    /**
     * This method is called when the server receives a notification that a game has been deleted.
     * TODO: add parameters as needed.
     */
    @Override
    public void receiveGameDeleted(String message) {
        System.out.println(message);
        // TODO: Implement this method
    }

    /**
     * This method is called when the server receives an updated view message.
     *
     * @param updatedView The updated view message received from the client.
     */
    @Override
    public void receiveUpdatedView(GameControllerView updatedView) {
        System.out.println("Received updated view: " + updatedView);
        // TODO: Implement this method
    }

    /**
     * This method is called when the server receives an error message.
     *
     * @param errorMessage The error message received from the client.
     */
    @Override
    public void receiveErrorMessage(String errorMessage) {
        System.out.println("Received error message: " + errorMessage);
        // TODO: Implement this method
    }
}
