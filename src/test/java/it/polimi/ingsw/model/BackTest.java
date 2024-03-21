package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * This class contains unit tests for the Back class.
 */

public class BackTest {
    /**
     * This test checks if the Back constructor throws a NullPointerException when a null GameItemStore is passed.
     */
    @Test
    @DisplayName("Test Back constructor")
    public void DontAcceptGameItemStoreNull() {
        assertThrows(NullPointerException.class, () -> new Back(null, mock(Corner.class), mock(Corner.class), mock(Corner.class), mock(Corner.class)));
    }
}