package it.polimi.ingsw.data;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.gameCard.front.FrontGameCard;

public class Test {
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
