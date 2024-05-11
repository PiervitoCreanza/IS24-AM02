package it.polimi.ingsw.network.client.message;

/**
 * This enum represents the possible actions a player can take in the game.
 * Each enum value corresponds to a specific type of client message.
 */
public enum PlayerActionEnum {
    /**
     * Represents a request to get the list of available games.
     */
    GET_GAMES,

    /**
     * Represents a request to create a new game.
     */
    CREATE_GAME,

    /**
     * Represents a request to delete an existing game.
     */
    DELETE_GAME,

    /**
     * Represents a request to join an existing game.
     */
    JOIN_GAME,

    /**
     * Represents a request to choose a player color.
     */
    CHOOSE_PLAYER_COLOR,

    /**
     * Represents a request to set a player objective.
     */
    SET_PLAYER_OBJECTIVE,

    /**
     * Represents a request to place a card.
     */
    PLACE_CARD,

    /**
     * Represents a request to draw a card from the field.
     */
    DRAW_CARD_FROM_FIELD,

    /**
     * Represents a request to draw a card from the resource deck.
     */
    DRAW_CARD_FROM_RESOURCE_DECK,

    /**
     * Represents a request to draw a card from the gold deck.
     */
    DRAW_CARD_FROM_GOLD_DECK,

    /**
     * Represents a request to switch the side of a card.
     */
    SWITCH_CARD_SIDE,
    /**
     * Represents a request to send a message.
     */
    CHAT_MSG
}