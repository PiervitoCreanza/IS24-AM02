package it.polimi.ingsw.model.player;

import java.util.stream.Stream;

/**
 * The PlayerColorEnum is an enumeration that represents the different colors a pawn can have in the game.
 * The colors are RED, BLUE, GREEN, and YELLOW.
 */
public enum PlayerColorEnum {
    /**
     * Represents the color RED.
     */
    RED,
    /**
     * Represents the color BLUE.
     */
    BLUE,
    /**
     * Represents the color GREEN.
     */
    GREEN,
    /**
     * Represents the color YELLOW.
     */
    YELLOW,
    /**
     * Represents the color NONE.
     */
    NONE;

    /**
     * Returns a stream of all the PlayerColorEnum values.
     *
     * @return a stream of all the PlayerColorEnum values
     */
    public static Stream<PlayerColorEnum> stream() {
        return Stream.of(PlayerColorEnum.values());
    }
}