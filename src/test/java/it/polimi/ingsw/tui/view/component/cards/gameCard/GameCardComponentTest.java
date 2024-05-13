package it.polimi.ingsw.tui.view.component.cards.gameCard;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameCardComponentTest {

    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    @DisplayName("Test if the starter cards are drawn correctly")
    void drawStarterCards() {
        parser.getStarterDeck().getCards().forEach(card -> {
            DrawArea drawArea = new DrawArea();
            GameCardComponent gameCardComponent = new GameCardComponent(card);
            drawArea.drawAt(0, 0, card.getCardId());
            drawArea.drawAt(0, 1, gameCardComponent.getDrawArea());
            card.switchSide();
            gameCardComponent = new GameCardComponent(card);
            drawArea.drawAt(gameCardComponent.getWidth() + 5, 1, gameCardComponent.getDrawArea());
            drawArea.drawNewLine("═".repeat(drawArea.getWidth()), 1);
            System.out.print(drawArea);
        });
    }

    @DisplayName("Test if the gold cards are drawn correctly")
    @Test
    void drawGoldCards() {
        parser.getGoldDeck().getCards().forEach(card -> {
            DrawArea drawArea = new DrawArea();
            GameCardComponent gameCardComponent = new GameCardComponent(card);
            drawArea.drawAt(0, 0, card.getCardId());
            drawArea.drawAt(0, 1, gameCardComponent.getDrawArea());
            card.switchSide();
            gameCardComponent = new GameCardComponent(card);
            drawArea.drawAt(gameCardComponent.getWidth() + 5, 1, gameCardComponent.getDrawArea());
            drawArea.drawNewLine("═".repeat(drawArea.getWidth()), 1);
            System.out.print(drawArea);
        });
    }

    @DisplayName("Test if the resource cards are drawn correctly")
    @Test
    void drawResourceCards() {
        parser.getResourceDeck().getCards().forEach(card -> {
            DrawArea drawArea = new DrawArea();
            GameCardComponent gameCardComponent = new GameCardComponent(card);
            drawArea.drawAt(0, 0, card.getCardId());
            drawArea.drawAt(0, 1, gameCardComponent.getDrawArea());
            card.switchSide();
            gameCardComponent = new GameCardComponent(card);
            drawArea.drawAt(gameCardComponent.getWidth() + 5, 1, gameCardComponent.getDrawArea());
            drawArea.drawNewLine("═".repeat(drawArea.getWidth()), 1);
            System.out.print(drawArea);
        });
    }
}
