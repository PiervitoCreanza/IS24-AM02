package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;

import java.io.Serializable;

/**
 * This is a supporter record used to represent the pattern contained in the target location card.
 */
public record PositionalData(Coordinate coordinate, CardColorEnum cardColorEnum) implements Serializable {
    /**
     * Checks if the given object is equal to this PositionalObjectiveCard.
     * Two PositionalObjectiveCards are equal if they have the same cardId, pointsWon, and positionalData.
     *
     * @param o The object to compare this PositionalObjectiveCard against.
     * @return true if the given object represents a PositionalObjectiveCard equivalent to this PositionalObjectiveCard, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionalData that)) return false;
        return this.coordinate.equals(that.coordinate) && this.cardColorEnum == that.cardColorEnum;
    }
}