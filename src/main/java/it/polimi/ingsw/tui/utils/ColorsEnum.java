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
     * The bright black escape code.
     */
    BRIGHT_BLACK("\u001B[90m"),

    /**
     * The red escape code.
     */
    RED("\u001B[31m"),

    /**
     * The bright red escape code.
     */
    BRIGHT_RED("\u001B[91m"),

    /**
     * The green escape code.
     */
    GREEN("\u001B[32m"),

    /**
     * The bright green escape code.
     */
    BRIGHT_GREEN("\u001B[92m"),

    /**
     * The yellow escape code.
     */
    YELLOW("\u001B[33m"),

    /**
     * The bright yellow escape code.
     */
    BRIGHT_YELLOW("\u001B[93m"),

    /**
     * The blue escape code.
     */
    BLUE("\u001B[34m"),

    /**
     * The bright blue escape code.
     */
    BRIGHT_BLUE("\u001B[94m"),

    /**
     * The purple escape code.
     */
    PURPLE("\u001B[35m"),

    /**
     * The bright purple escape code.
     */
    BRIGHT_PURPLE("\u001B[95m"),

    /**
     * The cyan escape code.
     */
    CYAN("\u001B[36m"),

    /**
     * The brightcyan escape code.
     */
    BRIGHT_CYAN("\u001B[96m"),

    /**
     * The white escape code.
     */
    WHITE("\u001B[37m"),  // White color

    /**
     * The bright white escape code.
     */
    BRIGHT_WHITE("\u001B[97m");

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