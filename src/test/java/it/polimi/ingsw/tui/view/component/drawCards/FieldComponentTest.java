package it.polimi.ingsw.tui.view.component.drawCards;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FieldComponentTest {

    private final ArrayList<GameCard> fieldResourceCards = new ArrayList<>();
    private final ArrayList<GameCard> fieldGoldCards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Parser parser = new Parser();
        fieldGoldCards.add(parser.getGoldDeck().getCards().getFirst());
        fieldGoldCards.add(parser.getGoldDeck().getCards().get(1));
        fieldResourceCards.add(parser.getResourceDeck().getCards().getFirst());
        fieldResourceCards.add(parser.getResourceDeck().getCards().get(1));
    }

    @Test
    @DisplayName("Test if field component of the player inventory is drawn correctly")
    void getPlayerInventoryDrawArea() {
        System.out.println(new FieldComponent(fieldResourceCards, fieldGoldCards, 5));
    }
}
