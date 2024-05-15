package it.polimi.ingsw.network.server.message;

import java.util.Objects;

/**
 * This class represents an error message from the server to the client.
 * It extends the ServerToClientMessage class.
 */
public class ErrorServerToClientMessage extends ServerToClientMessage {
    /**
     * The error message to be sent to the client.
     */
    private final String errorMessage;

    /**
     * Constructor for ErrorServerToClientMessage.
     *
     * @param message The error message to be sent to the client.
     */
    public ErrorServerToClientMessage(String message) {
        super(ServerActionEnum.ERROR_MSG);
        this.errorMessage = message;
    }

    /**
     * Getter for the errorMessage.
     *
     * @return The error message to be sent to the client.
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Overridden equals method for ErrorServerToClientMessage.
     * @param o The object to compare this ErrorServerToClientMessage to.
     * @return true if the given object is an ErrorServerToClientMessage with the same errorMessage.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorServerToClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.errorMessage, that.errorMessage);
    }
}