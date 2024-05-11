package it.polimi.ingsw.tui.draw;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.draw.cards.gameCard.GameCardComponent;
import org.junit.jupiter.api.Test;

public class GameCardComponentTest {
    @Test
    void getDrawArea() {
        GameCard gameCard = new Parser().getStarterDeck().getCards().stream().filter(c -> c.getCardId() == 86).findFirst().get();
        GameCardComponent gameCardComponent = new GameCardComponent(gameCard);
        System.out.println(gameCard.getClass());
        System.out.println(gameCardComponent);
        gameCard.switchSide();
        gameCardComponent = new GameCardComponent(gameCard);
        System.out.println(gameCard.getClass());
        System.out.println(gameCardComponent);
    }

    @Test
    void printAll() {
        Parser parser = new Parser();
        parser.getStarterDeck().getCards().forEach(c -> {
            DrawArea drawArea = new DrawArea();
            GameCardComponent gameCardComponent = new GameCardComponent(c);
            drawArea.drawAt(0, 0, gameCardComponent.getDrawArea());
            c.switchSide();
            gameCardComponent = new GameCardComponent(c);
            drawArea.drawAt(gameCardComponent.getWidth() + 5, 0, gameCardComponent.getDrawArea());
            System.out.println(c.getCardId());
            System.out.print(drawArea);
            String divider = "-".repeat(drawArea.getWidth());
            System.out.println(divider);
        });
        parser.getGoldDeck().getCards().forEach(c -> {
            DrawArea drawArea = new DrawArea();
            GameCardComponent gameCardComponent = new GameCardComponent(c);
            drawArea.drawAt(0, 0, gameCardComponent.getDrawArea());
            c.switchSide();
            gameCardComponent = new GameCardComponent(c);
            drawArea.drawAt(gameCardComponent.getWidth() + 5, 0, gameCardComponent.getDrawArea());
            System.out.println(c.getCardId());
            System.out.print(drawArea);
            String divider = "-".repeat(drawArea.getWidth());
            System.out.println(divider);
        });
        parser.getResourceDeck().getCards().forEach(c -> {
            DrawArea drawArea = new DrawArea();
            GameCardComponent gameCardComponent = new GameCardComponent(c);
            drawArea.drawAt(0, 0, gameCardComponent.getDrawArea());
            c.switchSide();
            gameCardComponent = new GameCardComponent(c);
            drawArea.drawAt(gameCardComponent.getWidth() + 5, 0, gameCardComponent.getDrawArea());
            System.out.println(c.getCardId());
            System.out.print(drawArea);
            String divider = "-".repeat(drawArea.getWidth());
            System.out.println(divider);
        });
    }
}
