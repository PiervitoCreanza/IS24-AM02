package it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.objectiveCard.ItemObjectiveCard;
import it.polimi.ingsw.parsing.Parser;
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