package it.polimi.ingsw.view.gui.utils;

/**
 * GUIUtils is a utility class that provides methods for GUI related operations.
 */
public class GUIUtils {
    /**
     * The default maximum length for a string.
     */
    public static int defaultMaxStringLength = 24;

    /**
     * Truncates a string to the default maximum length.
     *
     * @param string the string to truncate
     * @return the truncated string, or the original string if it's shorter than the default maximum length
     */
    public static String truncateString(String string) {
        return truncateString(string, defaultMaxStringLength);
    }

    /**
     * Truncates a string to a specified maximum length.
     *
     * @param string    the string to truncate
     * @param maxLength the maximum length for the string
     * @return the truncated string, or the original string if it's shorter than the maximum length
     */
    public static String truncateString(String string, int maxLength) {
        if (string.length() > maxLength) {
            return string.substring(0, maxLength - 3) + "...";
        }
        return string;
    }

    /**
     * Capitalizes the first letter of a string.
     *
     * @param str the string to capitalize
     * @return the string with the first letter capitalized
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }
}