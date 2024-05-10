package it.polimi.ingsw.model.card;

import it.polimi.ingsw.tui.utils.ColorsEnum;

/**
 * This enum represents the possible colors that a card can have in the game.
 * Each color corresponds to a different type of card.
 */
public enum CardColorEnum {
    /**
     * Represents a card of color red.
     */
    RED(ColorsEnum.RED),

    /**
     * Represents a card of color blue.
     */
    BLUE(ColorsEnum.BLUE),

    /**
     * Represents a card of color green.
     */
    GREEN(ColorsEnum.GREEN),

    /**
     * Represents a card of color purple.
     */
    PURPLE(ColorsEnum.RED),

    /**
     * Represents a card of color neutral.
     * This is used when the card does not have a specific color.
     */
    NONE(ColorsEnum.RESET);

    private final ColorsEnum color;

    CardColorEnum(ColorsEnum color) {
        this.color = color;
    }

    public String getCode() {
        return color.getCode();
    }

    public ColorsEnum getColor() {
        return color;
    }
}