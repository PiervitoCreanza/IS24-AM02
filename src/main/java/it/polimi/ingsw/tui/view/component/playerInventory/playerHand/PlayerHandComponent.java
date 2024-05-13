package it.polimi.ingsw.tui.view.component.playerInventory.playerHand;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.view.component.cards.gameCard.GameCardComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.ArrayList;

public class PlayerHandComponent implements Drawable {
    private final DrawArea drawArea;

    public PlayerHandComponent(ArrayList<GameCard> hand) {
        drawArea = new DrawArea();
        int spacing = 5;
        drawArea.drawAt(0, 0, new GameCardComponent(hand.getFirst()).getDrawArea());
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, new GameCardComponent(hand.get(1)).getDrawArea());
        drawArea.drawAt(drawArea.getWidth() + spacing, 0, new GameCardComponent(hand.get(2)).getDrawArea());
    }

    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }

    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
