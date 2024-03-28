package it.polimi.ingsw.model.card.gameCard;

import it.polimi.ingsw.model.card.PawnColorEnum;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnColorEnumTest {

    @Test
    public void shouldReturnAllPawnColorsWhenStreamIsCalled() {
        List<PawnColorEnum> expectedColors = Arrays.asList(PawnColorEnum.RED, PawnColorEnum.BLUE, PawnColorEnum.GREEN, PawnColorEnum.YELLOW);
        List<PawnColorEnum> actualColors = PawnColorEnum.stream().toList();

        assertEquals(expectedColors.size(), actualColors.size(), "The size of the actual colors list should be equal to the size of the expected colors list");

        for (PawnColorEnum color : expectedColors) {
            assertTrue(actualColors.contains(color), "The actual colors list should contain the color " + color);
        }
    }
}