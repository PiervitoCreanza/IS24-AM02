package it.polimi.ingsw.network.server.message;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Abstract class representing a message from the server to the client.
 * This class is serializable to allow it to be sent over a network.
 */
public abstract class ServerToClientMessage implements Serializable {
    /**
     * The action that the server wants the client to perform.
     * This is represented as an enum value of ServerActionEnum.
     */
    public ServerActionEnum serverAction;

    /**
     * Constructor for ServerToClientMessage.
     *
     * @param serverAction The action that the server wants the client to perform.
     */
    public ServerToClientMessage(ServerActionEnum serverAction) {
        this.serverAction = serverAction;
    }

    /**
     * Getter for serverAction.
     *
     * @return The action that the server wants the client to perform.
     */
    public ServerActionEnum getServerAction() {
        return serverAction;
    }

    /**
     * Overridden equals method for ServerToClientMessage.
     *
     * @param o The object to compare this ServerToClientMessage to.
     * @return true if the given object is a ServerToClientMessage with the same serverAction.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerToClientMessage that)) return false;
        return this.getServerAction() == that.getServerAction();
    }

    /**
     * Method to get the view of the game controller.
     *
     * @return null in this implementation.
     */
    public GameControllerView getView() {
        return null;
    }

    /**
     * Method to get the games.
     *
     * @return null in this implementation.
     */
    public HashSet<GameRecord> getGames() {
        return null;
    }

    /**
     * Method to get a specific game.
     *
     * @param gameName The name of the game to get.
     * @return null in this implementation.
     */
    public GameRecord getGame(String gameName) {
        return null;
    }

    /**
     * Method to get an error message.
     *
     * @return null in this implementation.
     */
    public String getErrorMessage() {
        return null;
    }

    /**
     * Method to get a success delete message.
     *
     * @return null in this implementation.
     */
    public String getSuccessDeleteMessage() {
        return null;
    }

    /**
     * Method to get a chat message.
     *
     * @return null in this implementation.
     */
    public chatMessageServerToClientMessage getChatMessage() {
        return null;
    }

    /**
     * Method to print a chat message.
     *
     * @return null in this implementation.
     */
    public String chatPrint() {
        return null;
    }
}