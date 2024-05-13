package it.polimi.ingsw.tui.view.component.playerBoard;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class PlayerBoardComponentTest {
    @Test
    void getDrawArea() {
        Parser parser = new Parser();
        DrawArea drawArea = new DrawArea();
        GameCard starterCard = parser.getStarterDeck().getCards().stream().filter(c -> c.getCardId() == 86).findFirst().get();
        PlayerBoard playerBoard = new PlayerBoard(starterCard);
        playerBoard.placeGameCard(new Coordinate(0, 0), starterCard);
        List<Integer> cardIds = Arrays.asList(3, 9, 13, 17, 23, 27, 29, 33, 38);
        int x = 1;
        int y = 1;
        for (GameCard c : parser.getResourceDeck().getCards()) {
            if (!cardIds.contains(c.getCardId()))
                playerBoard.placeGameCard(new Coordinate(x++, y++), c);
        }
        playerBoard.placeGameCard(new Coordinate(-1, -1), parser.getResourceDeck().getCards().getFirst());
        HashMap<Coordinate, GameCard> testMap = playerBoard.getVirtualView().playerBoard();
        HashMap<Integer, Coordinate> unconvertedCoordinates = new HashMap<>();
        testMap.forEach((k, v) -> unconvertedCoordinates.put(v.getCardId(), new Coordinate((int) k.getX(), (int) k.getY())));
        testMap = drawArea.convertCoordinates(testMap);
        testMap.forEach((k, v) -> {
            GameCardComponent gameCardComponent = new GameCardComponent(v, unconvertedCoordinates.get(v.getCardId()));
            drawArea.drawAt((int) (k.getX() * (gameCardComponent.getWidth() - 4)), (int) (k.getY() * (gameCardComponent.getHeight() - 2)), gameCardComponent.getDrawArea());
        });
        System.out.println(drawArea);
    }
}
