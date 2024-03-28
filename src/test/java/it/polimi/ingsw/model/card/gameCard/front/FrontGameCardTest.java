package it.polimi.ingsw.model.card.gameCard.front;

import it.polimi.ingsw.model.card.GameItemEnum;
import it.polimi.ingsw.model.card.corner.Corner;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@DisplayName("Front Game Card Test")
//100% line coverage was already ensured by child classes tests
public class FrontGameCardTest {

    private FrontGameCard frontGameCard;

    @BeforeEach
    public void setUp() {
        Corner topRight = new Corner(GameItemEnum.NONE);
        Corner topLeft = new Corner(GameItemEnum.NONE);
        Corner bottomLeft = new Corner(GameItemEnum.NONE);
        Corner bottomRight = new Corner(GameItemEnum.NONE);
        int points = 5;

        frontGameCard = new FrontGameCard(topRight, topLeft, bottomLeft, bottomRight, points);
    }

    @Test
    public void getPointsReturnsCorrectValue() {
        PlayerBoard playerBoard = mock(PlayerBoard.class);
        Coordinate coordinate = new Coordinate(1,1);

        int points = frontGameCard.getPoints(coordinate, playerBoard);

        assertEquals(5, points);
    }
}