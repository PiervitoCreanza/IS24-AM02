package it.polimi.ingsw.data;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;

/**
 * This class is used to test the serialization and deserialization of the GameCard class.
 */
public class Test {
    /**
     * Main method of the class.
     * It creates a GameCard object, serializes it to JSON, and deserializes it back to a GameCard object.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Parser parser = new Parser();
        GameCard testcard = new GameCard(0, new FrontGameCard(null, null, null, null, 0), new FrontGameCard(null, null, null, null, 0), CardColorEnum.RED);
        testcard.switchSide();

        String serialized = parser.serializeToJson(testcard);
        System.out.println(serialized);
        GameCard card = parser.getResourceDeck().draw();
        System.out.println(card.isFlipped());
        card.isFlipped();
    }
}
