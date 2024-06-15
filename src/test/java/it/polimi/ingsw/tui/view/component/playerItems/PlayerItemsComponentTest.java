package it.polimi.ingsw.tui.view.component.playerItems;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.tui.view.component.player.playerBoard.PlayerBoardComponent;
import it.polimi.ingsw.tui.view.component.player.playerItems.PlayerItemsComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayerItemsComponentTest {
    PlayerBoard playerBoard;

    @BeforeEach
    void setUp() {
        Parser parser = new Parser();
        Deck<GameCard> starterDeck = parser.getStarterDeck();
        Deck<GameCard> resourceDeck = parser.getResourceDeck();
        GameCard starterCard = starterDeck.draw();
        playerBoard = new PlayerBoard(starterCard);
        playerBoard.placeGameCard(new Coordinate(0, 0), starterCard);
        playerBoard.placeGameCard(new Coordinate(1, 1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(-1, 1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(1, -1), resourceDeck.draw());
        playerBoard.placeGameCard(new Coordinate(-1, -1), resourceDeck.draw());
    }

    @Test
    @DisplayName("Test if every component of the player items is drawn correctly")
    void drawPlayerItemsComponent() {
        DrawArea drawArea = new DrawArea();
        drawArea.drawAt(2, 0, new PlayerBoardComponent(playerBoard.getVirtualView().playerBoard()).getDrawArea());
        drawArea.drawNewLine("═".repeat(drawArea.getWidth() + 1), 1);
        drawArea.drawAt(0, drawArea.getHeight() + 1, new PlayerItemsComponent(playerBoard.getVirtualView().gameItemStore(), 5).getDrawArea());
        System.out.println(drawArea);
    }
}
