package it.polimi.ingsw.tui.view.drawer;

import it.polimi.ingsw.model.utils.Coordinate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * This class represents a translation map.
 * It converts a positive y-coordinate system to a negative one. And shifts the x-coordinates to the right to put it in the 1st quadrant.
 * This is useful when drawing on a terminal, where the origin is in the top-left corner.
 *
 * @param <T> the type of the values in the map
 */
public class TranslationMap<T> {
    /**
     * The original map. This map contains the original coordinates and their associated values.
     */
    private final HashMap<Coordinate, T> originalMap;

    /**
     * The translation map. This map contains the translated coordinates and their associated original coordinates.
     * The coordinates are translated from a positive y-coordinate system to a negative one, and shifted to the right to put them in the 1st quadrant.
     */
    private final HashMap<Coordinate, Coordinate> translationMap;

    /**
     * Constructs a new TranslationMap with the given original map.
     * The constructor translates the coordinates of the original map and stores them in the translation map.
     * The y-coordinates are inverted to convert from a positive y-coordinate system to a negative one.
     * The x-coordinates are shifted to the right to put them in the 1st quadrant.
     *
     * @param originalMap the original map with the original coordinates and their associated values
     */
    public TranslationMap(HashMap<Coordinate, T> originalMap) {
        this.originalMap = originalMap;
        translationMap = new HashMap<>();
        int maxY = this.originalMap.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
        int minX = this.originalMap.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        originalMap.keySet().forEach(p -> {
            Coordinate translationCoordinate = new Coordinate(p.x, p.y);
            if (minX < 0) {
                translationCoordinate.x += Math.abs(minX);
            }
            translationCoordinate.y = maxY - translationCoordinate.y;
            translationMap.put(translationCoordinate, p);
        });
    }

    /**
     * Returns the value to which the specified coordinate is mapped,
     * or null if this map contains no mapping for the coordinate.
     *
     * @param coordinate the coordinate whose associated value is to be returned
     * @return the value to which the specified coordinate is mapped, or null if this map contains no mapping for the coordinate
     */
    public T get(Coordinate coordinate) {
        return originalMap.get(translationMap.get(coordinate));
    }

    /**
     * Returns a Set view of the coordinates contained in this map.
     *
     * @return a set view of the coordinates contained in this map
     */
    public Set<Coordinate> keySet() {
        return translationMap.keySet();
    }

    /**
     * Returns a Collection view of the values contained in this map.
     *
     * @return a collection view of the values contained in this map
     */
    public Collection<T> values() {
        return originalMap.values();
    }

    /**
     * Returns the original coordinate to which the specified coordinate is mapped,
     * or null if this map contains no mapping for the coordinate.
     *
     * @param coordinate the coordinate whose associated original coordinate is to be returned
     * @return the original coordinate to which the specified coordinate is mapped, or null if this map contains no mapping for the coordinate
     */
    public Coordinate getOriginalCoord(Coordinate coordinate) {
        return translationMap.get(coordinate);
    }
}