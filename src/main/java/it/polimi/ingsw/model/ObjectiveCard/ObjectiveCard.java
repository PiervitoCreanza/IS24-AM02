package it.polimi.ingsw.model.ObjectiveCard;

import it.polimi.ingsw.model.PlayerBoard;

/**
 * Abstract class for the ObjectiveCard that let the player earn points by some criteria
 */

abstract public class ObjectiveCard{
    protected final int pointsWon;
    public ObjectiveCard(int pointsWon) {
        if(pointsWon <=0) throw new IllegalArgumentException("pointsWon must be positive");
        this.pointsWon = pointsWon;
    }
    abstract int getPoints(PlayerBoard playerBoard);
}
