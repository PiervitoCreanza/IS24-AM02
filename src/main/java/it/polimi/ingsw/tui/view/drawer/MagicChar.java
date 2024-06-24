package it.polimi.ingsw.tui.view.drawer;

import it.polimi.ingsw.tui.utils.ColorsEnum;

/**
 * This class represents a character that can be drawn with a specific color.
 */
public class MagicChar {

    /**
     * The character to be drawn.
     */
    private char character;

    /**
     * The color of the character.
     */
    private ColorsEnum color;

    /**
     * Constructor that initializes the MagicChar with a character.
     *
     * @param character The character to be drawn.
     */
    public MagicChar(char character) {
        this.character = character;
    }

    /**
     * Constructor that initializes the MagicChar with a character and a color.
     *
     * @param character The character to be drawn.
     * @param color     The color of the character.
     */
    public MagicChar(char character, ColorsEnum color) {
        this.character = character;
        this.color = color;
    }

    /**
     * Returns the character of the MagicChar.
     *
     * @return The character of the MagicChar.
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Sets the character of the MagicChar.
     *
     * @param character The character to set.
     */
    public void setCharacter(char character) {
        this.character = character;
    }

    /**
     * Returns the color of the MagicChar.
     *
     * @return The color of the MagicChar.
     */
    public ColorsEnum getColor() {
        return color;
    }

    /**
     * Sets the color of the MagicChar.
     *
     * @param color The color to set.
     */
    public void setColor(ColorsEnum color) {
        this.color = color;
    }

    /**
     * Returns a string representation of the MagicChar.
     * If the color is not null, the string will include the color code.
     *
     * @return A string representation of the MagicChar.
     */
    @Override
    public String toString() {
        if (color == null) {
            return String.valueOf(character);
        } else {
            return color.getCode() + character + "\u001B[0m";
        }
    }
}