package it.polimi.ingsw.model.ObjectiveCard;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.Coordinate;

/**
 * This is a supporter record used to represent the pattern contained in the target location card.
 */
public record PositionalData(Coordinate coordinate, CardColor cardColor) {}