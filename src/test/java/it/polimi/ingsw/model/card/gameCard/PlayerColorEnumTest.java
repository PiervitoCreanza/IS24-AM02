package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.PlayerColorEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerColorEnumTest {

    @Test
    @DisplayName("Test the stream method of PlayerColorEnum")
    public void shouldReturnAllPawnColorsWhenStreamIsCalled() {
        List<PlayerColorEnum> expectedColors = Arrays.asList(PlayerColorEnum.RED, PlayerColorEnum.BLUE, PlayerColorEnum.GREEN, PlayerColorEnum.YELLOW);
        List<PlayerColorEnum> actualColors = it.polimi.ingsw.model.card.PlayerColorEnum.stream().toList();

        assertEquals(expectedColors.size(), actualColors.size(), "The size of the actual colors list should be equal to the size of the expected colors list");

        assertTrue(actualColors.containsAll(expectedColors), "The actual colors list should contain all the expected colors");
        assertTrue(expectedColors.containsAll(actualColors), "The expected colors list should contain all the actual colors");
    }
}