package it.polimi.ingsw.tui.view.cards.objectiveCard;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class PlayerBoardComponentTest {

    Deck<GameCard> resourceDeck = new Parser().getResourceDeck();
    Deck<GameCard> goldDeck = new Parser().getGoldDeck();
    private HashMap<Coordinate, GameCard> playerBoard;

    @BeforeEach
    void setUp() {
        Parser parser = new Parser();
        DrawArea drawArea = new DrawArea();
        GameCard starterCard = parser.getStarterDeck().getCards().stream().filter(c -> c.getCardId() == 86).findFirst().get();
        PlayerBoard playerBoard = new PlayerBoard(starterCard);
        playerBoard.placeGameCard(new Coordinate(0, 0), starterCard);
        List<Integer> cardIds = Arrays.asList(3, 9, 13, 17, 23, 27, 29, 33, 38);
        int x = 1;
        int y = 1;

        playerBoard.placeGameCard(new Coordinate(-1, -1), getResourceCardById(1));
        playerBoard.placeGameCard(new Coordinate(1, -1), getResourceCardById(11));
        playerBoard.placeGameCard(new Coordinate(-1, 1), getResourceCardById(25));
        playerBoard.placeGameCard(new Coordinate(1, 1), getResourceCardById(31));
        playerBoard.placeGameCard(new Coordinate(0, 2), getResourceCardById(32));
        playerBoard.placeGameCard(new Coordinate(-2, 0), getResourceCardById(13));
        this.playerBoard = playerBoard.getVirtualView().playerBoard();
    }

    private GameCard getResourceCardById(int id) {
        GameCard res = resourceDeck.getCards().stream().filter(c -> c.getCardId() == id).findFirst().get();
        resourceDeck.getCards().remove(res);
        return res;
    }

    private GameCard getGoldCardById(int id) {
        GameCard res = goldDeck.getCards().stream().filter(c -> c.getCardId() == id).findFirst().get();
        goldDeck.getCards().remove(res);
        return res;
    }

    @Test
    void testToString() {

        PlayerBoardComponent playerBoardComponent = new PlayerBoardComponent(playerBoard);
        System.out.println(playerBoardComponent);
    }
}