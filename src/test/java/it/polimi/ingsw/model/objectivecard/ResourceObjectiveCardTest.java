package it.polimi.ingsw.model.objectivecard;

import it.polimi.ingsw.model.GameResource;
import it.polimi.ingsw.model.PlayerBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceObjectiveCardTest {
    private ResourceObjectiveCard resourceObjectiveCard;
    private PlayerBoard playerBoard;
    private GameResource gameResource;

    @BeforeEach
    public void setup() {
        resourceObjectiveCard = new ResourceObjectiveCard(10, gameResource);
    }

    @Test
    public void getPointsReturnsCorrectPointsWhenPlayerBoardHasResources() {
        assertEquals(20, resourceObjectiveCard.getPoints(playerBoard));
    }

    @Test
    public void getPointsReturnsZeroWhenPlayerBoardHasNoResources() {
        assertEquals(0, resourceObjectiveCard.getPoints(playerBoard));
    }

    @Test
    public void getPointsReturnsCorrectPointsWhenPlayerBoardHasLessThanThreeResources() {
        assertEquals(0, resourceObjectiveCard.getPoints(playerBoard));
    }
}