package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import org.junit.jupiter.api.Test;

class PositionalObjectiveCardComponentTest {

    @Test
    void getDrawArea() {
        PositionalObjectiveCard objectiveCard = (PositionalObjectiveCard) new Parser().getObjectiveDeck().getCards().stream().filter(c -> c.getCardId() == 91).findFirst().get();
        PositionalObjectiveCardComponent positionalObjectiveCardComponent = new PositionalObjectiveCardComponent(objectiveCard);
        System.out.println(objectiveCard.getClass());
        System.out.println(positionalObjectiveCardComponent);
    }
}