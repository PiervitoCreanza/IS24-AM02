package it.polimi.ingsw.tui.view.component.drawCards;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DrawCardComponentTest {

    GameCard firstResourceCard;
    GameCard firstGoldCard;
    private final ArrayList<GameCard> fieldResourceCards = new ArrayList<>();
    private final ArrayList<GameCard> fieldGoldCards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Parser parser = new Parser();
        firstResourceCard = parser.getResourceDeck().getCards().getFirst();
        firstGoldCard = parser.getGoldDeck().getCards().getFirst();
        fieldGoldCards.add(parser.getGoldDeck().getCards().getFirst());
        fieldGoldCards.add(parser.getGoldDeck().getCards().get(1));
        fieldResourceCards.add(parser.getResourceDeck().getCards().getFirst());
        fieldResourceCards.add(parser.getResourceDeck().getCards().get(1));
    }

    @Test
    @DisplayName("Test if every component of the decks is drawn correctly")
    void getDecksDrawArea() {
        System.out.println(new DrawCardComponent(firstResourceCard, firstGoldCard, fieldResourceCards, fieldGoldCards, 5));
    }
}
