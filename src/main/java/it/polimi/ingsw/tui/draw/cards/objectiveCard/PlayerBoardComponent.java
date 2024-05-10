package it.polimi.ingsw.tui.draw.cards.objectiveCard;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.draw.DrawArea;
import it.polimi.ingsw.tui.draw.Drawable;

import java.util.ArrayList;

public class PlayerBoardComponent implements Drawable {
    private final DrawArea drawArea = new DrawArea("");


    public PlayerBoardComponent(PlayerView playerView) {
        Parser parser = new Parser();
        ArrayList<ObjectiveCard> objectiveCards = parser.getObjectiveDeck().getCards();
        for (int i = 0; i < objectiveCards.size(); i++) {
            drawArea.drawAt(drawArea.getWidth() + 3, getHeight(), new ObjectiveCardComponent(objectiveCards.get(i)).getDrawArea());
        }

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
     * Returns the draw area of the drawable object.
     *
     * @return the draw area of the drawable object.
     */
    @Override
    public DrawArea getDrawArea() {
        return drawArea;
    }

    @Override
    public String toString() {
        return drawArea.toString();
    }
}
