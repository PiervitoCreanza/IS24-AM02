package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.corner.CornerPosition;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.draw.cards.gameCard.GameCardComponent;
import org.junit.jupiter.api.Test;

public class GameCardComponentTest {
    @Test
    void getDrawArea() {
        GameCard gameCard = new Parser().getResourceDeck().getCards().stream().filter(c -> c.getCardId() == 18).findFirst().get();
        gameCard.setCornerCovered(CornerPosition.BOTTOM_LEFT);
        GameCardComponent gameCardComponent = new GameCardComponent(gameCard);
        System.out.println(gameCard.getClass());
        System.out.println(gameCardComponent);
        gameCard.switchSide();
        gameCardComponent = new GameCardComponent(gameCard);
        System.out.println(gameCard.getClass());
        System.out.println(gameCardComponent);
    }
}
