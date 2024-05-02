package it.polimi.ingsw.network.client.message;

/**
 * This enum represents the possible actions a player can take in the game.
 * Each enum value corresponds to a specific type of client message.
 */
public enum PlayerActionEnum {
    /**
     * Represents a request to get the list of available games.
     */
    GETGAMES,

    /**
     * Represents a request to create a new game.
     */
    CREATEGAME,

    /**
     * Represents a request to join an existing game.
     */
    JOINGAME,

    /**
     * Represents a request to choose a player color.
     */
    CHOOSEPLAYERCOLOR,

    /**
     * Represents a request to set a player objective.
     */
    SETPLAYEROBJECTIVE,

    /**
     * Represents a request to place a card.
     */
    PLACECARD,

    /**
     * Represents a request to draw a card from the field.
     */
    DRAWCARDFROMFIELD,

    /**
     * Represents a request to draw a card from the resource deck.
     */
    DRAWCARDFROMRESOURCEDECK,

    /**
     * Represents a request to draw a card from the gold deck.
     */
    DRAWCARDFROMGOLDDECK,

    /**
     * Represents a request to switch the side of a card.
     */
    SWITCHCARDSIDE
}