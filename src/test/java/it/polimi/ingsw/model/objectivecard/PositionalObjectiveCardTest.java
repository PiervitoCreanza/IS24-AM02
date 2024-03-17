package it.polimi.ingsw.model.objectivecard;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionalObjectiveCardTest {
    private PositionalObjectiveCard positionalObjectiveCard;
    private PlayerBoard playerBoard;
    private GameCard gameCard1;
    private GameCard gameCard2;
    private GameCard gameCard3;

    @BeforeEach
    public void setup() {
        ArrayList<PositionalData> positionalData = new ArrayList<>(Arrays.asList(
                new PositionalData(new Point(0, 0), CardColor.BLUE),
                new PositionalData(new Point(1, 0), CardColor.RED),
                new PositionalData(new Point(0, 1), CardColor.GREEN)
        ));
        positionalObjectiveCard = new PositionalObjectiveCard(10, positionalData);
    }

    @Test
    public void getPointsReturnsCorrectPointsWhenPlayerBoardMatchesPositionalData() {
        assertEquals(10, positionalObjectiveCard.getPoints(playerBoard));
    }

    @Test
    public void getPointsReturnsZeroWhenPlayerBoardDoesNotMatchPositionalData() {
        assertEquals(0, positionalObjectiveCard.getPoints(playerBoard));
    }
}