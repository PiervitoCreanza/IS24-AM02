package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.PlayerColorEnum;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerColorEnumTest {

    @Test
    public void shouldReturnAllPawnColorsWhenStreamIsCalled() {
        List<PlayerColorEnum> expectedColors = Arrays.asList(it.polimi.ingsw.model.card.PlayerColorEnum.RED, it.polimi.ingsw.model.card.PlayerColorEnum.BLUE, it.polimi.ingsw.model.card.PlayerColorEnum.GREEN, it.polimi.ingsw.model.card.PlayerColorEnum.YELLOW);
        List<PlayerColorEnum> actualColors = it.polimi.ingsw.model.card.PlayerColorEnum.stream().toList();

        assertEquals(expectedColors.size(), actualColors.size(), "The size of the actual colors list should be equal to the size of the expected colors list");

        for (PlayerColorEnum color : expectedColors) {
            assertTrue(actualColors.contains(color), "The actual colors list should contain the color " + color);
        }
    }
}