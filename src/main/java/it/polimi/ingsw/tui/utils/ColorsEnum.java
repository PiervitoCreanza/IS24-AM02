package it.polimi.ingsw.tui.utils;

/**
 * This enum represents the colors that can be used in the console.
 * Each color is represented by a specific escape code.
 */
public enum ColorsEnum {
    /**
     * The reset escape code.
     */
    RESET("\u001B[0m"),

    /**
     * The black escape code.
     */
    BLACK("\u001B[30m"),

    /**
     * The red escape code.
     */
    RED("\u001B[31m"),

    /**
     * The green escape code.
     */
    GREEN("\u001B[32m"),

    /**
     * The yellow escape code.
     */
    YELLOW("\u001B[33m"),

    /**
     * The blue escape code.
     */
    BLUE("\u001B[34m"),

    /**
     * The purple escape code.
     */
    PURPLE("\u001B[35m"),

    /**
     * The cyan escape code.
     */
    CYAN("\u001B[36m"),

    /**
     * The white escape code.
     */
    WHITE("\u001B[37m");  // White color

    /**
     * The escape code of the color.
     */
    private final String code;

    /**
     * Constructor that initializes the color with an escape code.
     *
     * @param code The escape code of the color.
     */
    ColorsEnum(String code) {
        this.code = code;
    }

    /**
     * Returns the escape code of the color.
     *
     * @return The escape code of the color.
     */
    public String getCode() {
        return code;
    }
}