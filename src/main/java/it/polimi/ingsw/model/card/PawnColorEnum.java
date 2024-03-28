package it.polimi.ingsw.model.card;

import java.util.stream.Stream;

/**
 * The PawnColorEnum is an enumeration that represents the different colors a pawn can have in the game.
 * The colors are RED, BLUE, GREEN, and YELLOW.
 */
public enum PawnColorEnum {
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
    YELLOW;

    /**
     * Returns a stream of all the PawnColorEnum values.
     *
     * @return a stream of all the PawnColorEnum values
     */
    public static Stream<PawnColorEnum> stream() {
        return Stream.of(PawnColorEnum.values());
    }
}