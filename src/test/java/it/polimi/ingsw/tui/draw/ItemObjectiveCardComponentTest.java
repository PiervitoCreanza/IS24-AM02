package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.tui.draw.cards.objectiveCard.ObjectiveCardComponent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

class ItemObjectiveCardComponentTest {
    @Test
    void getDrawArea() {
        ArrayList<ItemObjectiveCard> objectiveCards = new Parser().getObjectiveDeck().getCards().stream().filter(c -> !c.isPositionalObjectiveCard()).map(c -> (ItemObjectiveCard) c).collect(Collectors.toCollection(ArrayList::new));
        objectiveCards.forEach(objectiveCard -> System.out.println(new ObjectiveCardComponent(objectiveCard) {
        }));
    }
}