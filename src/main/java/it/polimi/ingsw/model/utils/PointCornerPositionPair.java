package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.card.corner.CornerPosition;

/**
 * This class represents a pair of a point and a corner position.
 */
public record PointCornerPositionPair(Coordinate coordinate, CornerPosition cornerPosition) {
}
