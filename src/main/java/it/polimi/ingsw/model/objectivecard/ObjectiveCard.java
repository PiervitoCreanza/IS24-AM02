package it.polimi.ingsw.model.objectivecard;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.PlayerBoard;

/**
 * Abstract class for the ObjectiveCard that let the player earn points by some criteria
 */

abstract public class ObjectiveCard implements Card {
    protected final int pointsWon;
    public ObjectiveCard(int pointsWon) {
        this.pointsWon = pointsWon;
    }

    /**
     * Calculate the points earned based on parameters contained in the PlayerBoard
     * @param playerBoard of the player
     * @return points earned by this card
     */
    @Override
    public int getPoints(PlayerBoard playerBoard){
        return 0;
    };
}
