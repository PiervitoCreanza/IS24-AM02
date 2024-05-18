package it.polimi.ingsw.tui.utils;

/**
 * Utility class for console color codes.
 */
public class Utils {
    /**
     * ANSI escape code to reset the console color.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * ANSI escape code for red color.
     */
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * ANSI escape code for green color.
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * ANSI escape code for yellow color.
     */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * ANSI escape code for blue color.
     */
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * ANSI escape code for purple color.
     */
    public static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * ANSI escape code for cyan color.
     */
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * ANSI escape code to clear the console screen on Unix-based systems.
     */
    public static final String ANSI_CLEAR_UNIX = "\033[H\033[2J";

    /**
     * Clears the console screen based on the operating system.
     */
    public static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                System.out.print(Utils.ANSI_CLEAR_UNIX);
            } else {
                // Fallback to printing new lines if OS detection failed
                for (int i = 0; i < 100; i++) {
                    System.out.println();
                }
            }
        } catch (Exception ignored) {
        }
    }
}