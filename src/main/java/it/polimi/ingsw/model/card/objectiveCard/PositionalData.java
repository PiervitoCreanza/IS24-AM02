package it.polimi.ingsw.model.card.objectiveCard;

import it.polimi.ingsw.model.card.CardColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This is a supporter record used to represent the pattern contained in the target location card
 */
public record PositionalData(Coordinate coordinate, CardColorEnum cardColorEnum) {
}