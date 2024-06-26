package it.polimi.ingsw.view.tui.drawables.component.cards.objectiveCard;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.parsing.Parser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

class ObjectiveCardComponentTest {

    @Test
    void getDrawArea() {
        ArrayList<PositionalObjectiveCard> objectiveCards = new Parser().getObjectiveDeck().getCards().stream().filter(ObjectiveCard::isPositionalObjectiveCard).map(c -> (PositionalObjectiveCard) c).collect(Collectors.toCollection(ArrayList::new));
        objectiveCards.forEach(objectiveCard -> {
            System.out.println(objectiveCard.getCardId());
            System.out.println(new ObjectiveCardComponent(objectiveCard));
        });
    }
}