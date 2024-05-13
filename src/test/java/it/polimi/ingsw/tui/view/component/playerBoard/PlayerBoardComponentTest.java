package it.polimi.ingsw.tui.view.component.playerBoard;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


public class PlayerBoardComponentTest {

    Deck<GameCard> resourceDeck;
    PlayerBoard playerBoard;

    @BeforeEach
    void setUp() {
        Parser parser = new Parser();
        Deck<GameCard> starterDeck = parser.getStarterDeck();
        resourceDeck = parser.getResourceDeck();
        GameCard starterCard = starterDeck.draw();
        playerBoard = new PlayerBoard(starterCard);
        playerBoard.placeGameCard(new Coordinate(0, 0), starterCard);
    }

    @Test
    @DisplayName("Cards on every corner of the starter card")
    void cardsOnEveryCorner() {
        playerBoard.placeGameCard(new Coordinate(1, 1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(-1, 1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(1, -1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(-1, -1), resourceDeck.draw());
        System.out.println(new PlayerBoardComponent(playerBoard.getVirtualView().playerBoard()).getDrawArea());
    }

    @Test
    @DisplayName("Card over two cards")
    void cardOverTwoCards() {
        ArrayList<Integer> allowedCardIds = new ArrayList<>(Arrays.asList(7, 10));
        ArrayList<GameCard> placeableCards = resourceDeck.getCards().stream().filter(c -> allowedCardIds.contains(c.getCardId())).collect(Collectors.toCollection(ArrayList::new));
        playerBoard.placeGameCard(new Coordinate(1, 1), placeableCards.getFirst());
        playerBoard.placeGameCard(new Coordinate(1, -1), placeableCards.getLast());
        playerBoard.placeGameCard(new Coordinate(2, 0), resourceDeck.draw());
        System.out.println(new PlayerBoardComponent(playerBoard.getVirtualView().playerBoard()).getDrawArea());
    }

    @Test
    @DisplayName("All cards placed on top right corner")
    void allCardsOnTopRightCorner() {
        ArrayList<Integer> notAllowedCardIds = new ArrayList<>(Arrays.asList(3, 9, 13, 17, 23, 27, 29, 33, 38));
        int x = 1;
        int y = 1;
        for (GameCard card : resourceDeck.getCards()) {
            if (!notAllowedCardIds.contains(card.getCardId()))
                playerBoard.placeGameCard(new Coordinate(x++, y++), card);
        }
        System.out.println(new PlayerBoardComponent(playerBoard.getVirtualView().playerBoard()).getDrawArea());
    }
}
