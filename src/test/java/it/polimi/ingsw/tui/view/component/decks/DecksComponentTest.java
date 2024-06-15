package it.polimi.ingsw.tui.view.component.decks;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DecksComponentTest {

    GameCard firstResourceCard;
    GameCard firstGoldCard;

    @BeforeEach
    void setUp() {
        Parser parser = new Parser();
        firstResourceCard = parser.getResourceDeck().getCards().getFirst();
        firstGoldCard = parser.getGoldDeck().getCards().getFirst();
    }

    @Test
    @DisplayName("Test if every component of the decks is drawn correctly")
    void getDecksDrawArea() {
        System.out.println(new DecksComponent(firstResourceCard, firstGoldCard, 5));
    }
}
