package it.polimi.ingsw.view.tui.utils;

/**
 * This enum represents the colors that can be used in the console.
 * Each color is represented by a specific escape code.
 */
public enum ColorsEnum {
    /**
     * The reset escape code.
     */
    RESET("\u001B[0m", null),

    /**
     * The black escape code.
     */
    BLACK("\u001B[30m", "black-text"),

    /**
     * The bright black escape code.
     */
    BRIGHT_BLACK("\u001B[90m", "bright-black-text"),

    /**
     * The red escape code.
     */
    RED("\u001B[31m", "red-text"),

    /**
     * The bright red escape code.
     */
    BRIGHT_RED("\u001B[91m", "bright-red-text"),

    /**
     * The green escape code.
     */
    GREEN("\u001B[32m", "green-text"),

    /**
     * The bright green escape code.
     */
    BRIGHT_GREEN("\u001B[92m", "bright-green-text"),

    /**
     * The yellow escape code.
     */
    YELLOW("\u001B[33m", "yellow-text"),

    /**
     * The bright yellow escape code.
     */
    BRIGHT_YELLOW("\u001B[93m", "bright-yellow-text"),

    /**
     * The blue escape code.
     */
    BLUE("\u001B[34m", "blue-text"),

    /**
     * The bright blue escape code.
     */
    BRIGHT_BLUE("\u001B[94m", "bright-blue-text"),

    /**
     * The purple escape code.
     */
    PURPLE("\u001B[35m", "purple-text"),

    /**
     * The bright purple escape code.
     */
    BRIGHT_PURPLE("\u001B[95m", "bright-purple-text"),

    /**
     * The cyan escape code.
     */
    CYAN("\u001B[36m", "cyan-text"),

    /**
     * The brightcyan escape code.
     */
    BRIGHT_CYAN("\u001B[96m", "bright-cyan-text"),

    /**
     * The white escape code.
     */
    WHITE("\u001B[37m", "white-text"),  // White color

    /**
     * The bright white escape code.
     */
    BRIGHT_WHITE("\u001B[97m", "bright-white-text");

    /**
     * The escape code of the color.
     */
    private final String code;

    /**
     * The text css class name of the color.
     */
    private final String textCssClassName;

    /**
     * Constructor that initializes the color with an escape code.
     *
     * @param code             The escape code of the color.
     * @param textCssClassName The text css class name of the color.
     */
    ColorsEnum(String code, String textCssClassName) {
        this.code = code;
        this.textCssClassName = textCssClassName;
    }

    /**
     * Returns the escape code of the color.
     *
     * @return The escape code of the color.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the text css class name of the color.
     *
     * @return The text css class name of the color.
     */
    public String getTextCssClassName() {
        return textCssClassName;
    }
}