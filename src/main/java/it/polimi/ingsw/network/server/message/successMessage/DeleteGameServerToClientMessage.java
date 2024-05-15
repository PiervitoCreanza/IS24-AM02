package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;

import java.util.Objects;

/**
 * This class represents a message from the server to the client that a game has been deleted.
 * It extends the ServerToClientMessage class.
 */
public class DeleteGameServerToClientMessage extends ServerToClientMessage {
    /**
     * The success message to be sent to the client.
     */
    private final String successDeleteMessage;

    /**
     * Constructor for DeleteGameServerToClientMessage.
     * It initializes the successDeleteMessage with a default message.
     */
    public DeleteGameServerToClientMessage() {
        super(ServerActionEnum.DELETE_GAME);
        this.successDeleteMessage = "The game has been deleted";
    }

    /**
     * Getter for the successDeleteMessage.
     *
     * @return The success message to be sent to the client.
     */
    @Override
    public String getSuccessDeleteMessage() {
        return this.successDeleteMessage;
    }

    /**
     * Overridden equals method for DeleteGameServerToClientMessage.
     *
     * @param o The object to compare this DeleteGameServerToClientMessage to.
     * @return true if the given object is a DeleteGameServerToClientMessage with the same successDeleteMessage.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteGameServerToClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.successDeleteMessage, that.successDeleteMessage);
    }
}