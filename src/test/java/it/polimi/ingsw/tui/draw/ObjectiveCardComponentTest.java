package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.tui.draw.cards.objectiveCard.ObjectiveCardComponent;
import org.junit.jupiter.api.Test;

class ObjectiveCardComponentTest {

    @Test
    void getDrawArea() {
        PositionalObjectiveCard objectiveCard = (PositionalObjectiveCard) new Parser().getObjectiveDeck().getCards().stream().filter(c -> c.getCardId() == 92).findFirst().get();
        ObjectiveCardComponent objectiveCardComponent = new ObjectiveCardComponent(objectiveCard);
        System.out.println(objectiveCard.getClass());
        System.out.println(objectiveCardComponent);
    }
}