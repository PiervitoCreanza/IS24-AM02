package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import org.junit.jupiter.api.Test;

class ItemObjectiveCardComponentTest {

    @Test
    void getDrawArea() {
        ItemObjectiveCard objectiveCard = (ItemObjectiveCard) new Parser().getObjectiveDeck().getCards().stream().filter(c -> c.getCardId() == 98).findFirst().get();
        ItemObjectiveCardComponent positionalObjectiveCardComponent = new ItemObjectiveCardComponent(objectiveCard);
        System.out.println(objectiveCard.getClass());
        System.out.println(positionalObjectiveCardComponent);
    }
}