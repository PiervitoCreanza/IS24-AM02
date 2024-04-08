package it.polimi.ingsw.controller;

/**
 * This enum represents the different states that a game can be at any given time.
 * Each state corresponds to a different phase of the game.
 */
public enum GameStatusEnum {
    /**
     * The game is waiting for players to join.
     */
    WAIT_FOR_PLAYERS,

    /**
     * The game is in the initialization phase.
     * This is when the player has to choose his starter card and objective card.
     * He also has to draw his initial hand of cards.
     */
    INITIALIZATION,

    /**
     * The game is in the phase where players are placing their starter cards.
     */
    PLACE_STARTER_CARD,

    /**
     * The game is in the phase where players are drawing their initial hand of cards.
     */
    INIT_DRAW_CARD,

    /**
     * The game is in the phase where the player is choosing his objective cards.
     */
    INIT_CHOOSE_OBJECTIVE_CARD,

    /**
     * The game is in the phase where players are placing their cards.
     */
    PLACE_CARD,

    /**
     * The game is in the phase where players are drawing cards.
     */
    DRAW_CARD,

    /**
     * The game is in its last round.
     */
    LAST_ROUND,

    /**
     * The game has ended.
     */
    GAME_OVER
}
