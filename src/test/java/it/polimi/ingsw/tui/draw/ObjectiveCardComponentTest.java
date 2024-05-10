package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.card.objectiveCard.PositionalObjectiveCard;
import it.polimi.ingsw.tui.draw.cards.objectiveCard.ObjectiveCardComponent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

class ObjectiveCardComponentTest {

    @Test
    void getDrawArea() {
        ArrayList<PositionalObjectiveCard> objectiveCards = new Parser().getObjectiveDeck().getCards().stream().filter(ObjectiveCard::isPositionalObjectiveCard).map(c -> (PositionalObjectiveCard) c).collect(Collectors.toCollection(ArrayList::new));
        objectiveCards.forEach(objectiveCard -> System.out.println(new ObjectiveCardComponent(objectiveCard)));
    }
}