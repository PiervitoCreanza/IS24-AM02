package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Utility class for testing
 */
public class Utils {

    /**
     * Asserts that an IllegalArgumentException is thrown with the given message
     *
     * @param message    the message to check
     * @param executable the executable to run
     */
    public void assertIllegalArgument(String message, org.junit.jupiter.api.function.Executable executable) {
        Exception exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(message, exception.getMessage());
    }
}
