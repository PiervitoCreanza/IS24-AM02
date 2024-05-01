package it.polimi.ingsw.network.messages;

/**
 * This class represents a client command message.
 * It is a base class for other message types in the application.
 */
public class ClientCommandMessage {

    /**
     * This method checks if the message contains valid data.
     * In this implementation, it always returns false, indicating that the message is always considered invalid.
     * Subclasses should override this method to provide their own validation logic.
     *
     * @return false, as the base class does not contain any data to validate
     */
    private boolean isValid() {
        // We are checking if the message contains valid data, GSON fills the object with default values if the JSON fields are not valid
        // In this case, the method always returns false, so the object is always considered invalid
        return false;
    }
}