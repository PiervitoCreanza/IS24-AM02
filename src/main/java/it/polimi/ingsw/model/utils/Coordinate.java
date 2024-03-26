package it.polimi.ingsw.model.utils;

import java.awt.*;

/**
 * This class represents a coordinate point in a 2D space.
 * It extends the Point class from java.awt package in order to provide a more specific name.
 */
public class Coordinate extends Point {
    /**
     * Constructs a new Coordinate object with the specified x and y coordinates.
     *
     * @param x the X coordinate of the newly constructed Coordinate
     * @param y the Y coordinate of the newly constructed Coordinate
     */
    public Coordinate(int x, int y) {
        super(x, y);
    }
}