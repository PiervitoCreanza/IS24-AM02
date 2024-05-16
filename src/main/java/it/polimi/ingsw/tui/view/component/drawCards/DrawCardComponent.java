package it.polimi.ingsw.tui.view.component.drawCards;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.tui.view.drawer.DrawArea;
import it.polimi.ingsw.tui.view.drawer.Drawable;

import java.util.ArrayList;

public class DrawCardComponent implements Drawable {

    /**
     * The draw area of the decks component.
     */
    DrawArea drawArea;

    public DrawCardComponent(GameCard firstResourceCard, GameCard firstGoldCard, ArrayList<GameCard> fieldResourceCards, ArrayList<GameCard> fieldGoldCards, int spacing) {
        drawArea = new DrawArea();
        DrawArea temp = new DrawArea();
        DrawArea fieldComponent = new FieldComponent(fieldResourceCards, fieldGoldCards, spacing).getDrawArea();
        DrawArea decksComponent = new DecksComponent(firstResourceCard, firstGoldCard, spacing).getDrawArea();

        temp.drawAt(0, 0, fieldComponent);
        temp.drawAt(fieldComponent.getWidth() + spacing, 0, decksComponent);
        drawArea.drawAt(0, 0, "═".repeat(temp.getWidth()));
        drawArea.drawAt(0, 1, temp);
    }

    /**
     * Returns the height of the drawable object.
     *
     * @return the height of the drawable object.
     */
    @Override
    public int getHeight() {
        return drawArea.getHeight();
    }

    /**
     * Returns the width of the drawable object.
     *
     * @return the width of the drawable object.
     */
    @Override
    public int getWidth() {
        return drawArea.getWidth();
    }

    /**
     * Returns a string representation of the drawable object.
     *
     * @return a string representation of the drawable object.
     */
    @Override
    public String toString() {
        return drawArea.toString();
    }

    /**
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }
}
