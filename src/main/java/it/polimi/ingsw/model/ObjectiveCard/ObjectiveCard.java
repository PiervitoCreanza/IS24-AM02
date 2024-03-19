package it.polimi.ingsw.model.ObjectiveCard;
import it.polimi.ingsw.model.Card;

/**
 * Abstract class for the ObjectiveCard that let the player earn points by some criteria
 */

abstract public class ObjectiveCard implements Card {
    protected final int pointsWon;
    public ObjectiveCard(int pointsWon) {
        this.pointsWon = pointsWon;
    }
}
