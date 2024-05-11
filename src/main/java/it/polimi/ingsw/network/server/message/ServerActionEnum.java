package it.polimi.ingsw.network.server.message;

/**
 * This enum represents the different types of actions that a server can perform.
 * It is used to communicate the type of action to be performed to the client.
 */
public enum ServerActionEnum {
    /**
     * Represents an action to update the view on the client side.
     */
    UPDATE_VIEW,

    /**
     * Represents an action to delete a game on the server side.
     */
    DELETE_GAME,

    /**
     * Represents an action to get the list of games from the server.
     */
    GET_GAMES,

    /**
     * Represents an action to send an error message to the client.
     */
    ERROR_MSG,

    /**
     * Represents an action to send a chat message to the client.
     */
    CHAT_MSG
}