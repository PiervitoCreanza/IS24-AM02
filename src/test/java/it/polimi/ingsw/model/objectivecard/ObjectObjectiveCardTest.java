package it.polimi.ingsw.model.objectivecard;

import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.GameObjectStore;
import it.polimi.ingsw.model.PlayerBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectObjectiveCardTest {
    private ObjectObjectiveCard objectObjectiveCard;
    private GameObjectStore gameObjectStore;
    private PlayerBoard playerBoard;
    private GameObject gameObject;

    @BeforeEach
    public void setup() {
        objectObjectiveCard = new ObjectObjectiveCard(10, gameObjectStore);
    }

    @Test
    public void getPointsReturnsCorrectPointsWhenPlayerBoardHasObjects() {
        assertEquals(20, objectObjectiveCard.getPoints(playerBoard));
    }

    @Test
    public void getPointsReturnsZeroWhenPlayerBoardHasNoObjects() {
        assertEquals(0, objectObjectiveCard.getPoints(playerBoard));
    }
}