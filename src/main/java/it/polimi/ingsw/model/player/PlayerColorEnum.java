package it.polimi.ingsw.model.player;

import it.polimi.ingsw.tui.utils.ColorsEnum;

import java.util.stream.Stream;

/**
 * The PlayerColorEnum is an enumeration that represents the different colors a pawn can have in the game.
 * The colors are RED, BLUE, GREEN, and YELLOW.
 */
public enum PlayerColorEnum {
    /**
     * Represents the color RED.
     */
    RED(ColorsEnum.RED),
    /**
     * Represents the color BLUE.
     */
    BLUE(ColorsEnum.BLUE),
    /**
     * Represents the color GREEN.
     */
    GREEN(ColorsEnum.GREEN),
    /**
     * Represents the color YELLOW.
     */
    YELLOW(ColorsEnum.YELLOW),
    /**
     * Represents the color NONE.
     */
    NONE(null);

    private final ColorsEnum color;

    PlayerColorEnum(ColorsEnum color) {
        this.color = color;
    }

    /**
     * Returns the color of the card.
     *
     * @return the color of the card
     */
    public ColorsEnum getColor() {
        return color;
    }

    /**
     * Returns a stream of all the PlayerColorEnum values.
     *
     * @return a stream of all the PlayerColorEnum values
     */
    public static Stream<PlayerColorEnum> stream() {
        return Stream.of(PlayerColorEnum.values());
    }
}