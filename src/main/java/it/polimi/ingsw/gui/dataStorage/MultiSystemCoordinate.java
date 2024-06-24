package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.utils.Coordinate;

/**
 * Class representing a coordinate in both the model and GUI systems.
 * It provides methods for converting coordinates between the two systems.
 */
public class MultiSystemCoordinate {
    /**
     * The coordinate in the model system.
     */
    private Coordinate coordinateInModelSystem;

    /**
     * The coordinate in the GUI system.
     */
    private Coordinate coordinateInGUISystem;

    /**
     * Default constructor.
     */
    public MultiSystemCoordinate() {
    }

    /**
     * Converts a GUI coordinate to a model coordinate.
     *
     * @param coordinate The GUI coordinate.
     * @return The model coordinate.
     */
    public static Coordinate convertGUItoModel(Coordinate coordinate) {
        return new Coordinate(coordinate.x - 50, 50 - coordinate.y);
    }

    /**
     * Converts a model coordinate to a GUI coordinate.
     *
     * @param coordinate The model coordinate.
     * @return The GUI coordinate.
     */
    public static Coordinate convertModelToGUI(Coordinate coordinate) {
        return new Coordinate(coordinate.x + 50, 50 - coordinate.y);
    }

    /**
     * Gets the coordinate in the model system.
     *
     * @return The model coordinate.
     */
    public Coordinate getCoordinateInModelSystem() {
        return coordinateInModelSystem;
    }

    /**
     * Sets the coordinate in the model system and updates the GUI coordinate accordingly.
     *
     * @param coordinateInModelSystem The coordinate in the model system.
     * @return This MultiSystemCoordinate.
     */
    public MultiSystemCoordinate setCoordinateInModelSystem(Coordinate coordinateInModelSystem) {
        this.coordinateInModelSystem = coordinateInModelSystem;
        this.coordinateInGUISystem = convertModelToGUI(coordinateInModelSystem);
        return this;
    }

    /**
     * Sets the coordinate in the model system and updates the GUI coordinate accordingly.
     *
     * @param x The x-coordinate in the model system.
     * @param y The y-coordinate in the model system.
     * @return This MultiSystemCoordinate.
     */
    public MultiSystemCoordinate setCoordinateInModelSystem(int x, int y) {
        this.coordinateInModelSystem = new Coordinate(x, y);
        this.coordinateInGUISystem = convertModelToGUI(new Coordinate(x, y));
        return this;
    }

    /**
     * Gets the coordinate in the GUI system.
     *
     * @return The GUI coordinate.
     */
    public Coordinate getCoordinateInGUISystem() {
        return coordinateInGUISystem;
    }

    /**
     * Sets the coordinate in the GUI system and updates the model coordinate accordingly.
     *
     * @param coordinateInGUISystem The coordinate in the GUI system.
     * @return This MultiSystemCoordinate.
     */
    public MultiSystemCoordinate setCoordinateInGUISystem(Coordinate coordinateInGUISystem) {
        this.coordinateInGUISystem = coordinateInGUISystem;
        this.coordinateInModelSystem = convertGUItoModel(coordinateInGUISystem);
        return this;
    }

    /**
     * Sets the coordinate in the GUI system and updates the model coordinate accordingly.
     *
     * @param x The x-coordinate in the GUI system.
     * @param y The y-coordinate in the GUI system.
     * @return This MultiSystemCoordinate.
     */
    public MultiSystemCoordinate setCoordinateInGUISystem(int x, int y) {
        this.coordinateInGUISystem = new Coordinate(x, y);
        this.coordinateInModelSystem = convertGUItoModel(new Coordinate(x, y));
        return this;
    }

    /**
     * Checks if this MultiSystemCoordinate is equal to another object.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiSystemCoordinate that = (MultiSystemCoordinate) o;
        return coordinateInModelSystem.equals(that.coordinateInModelSystem);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return coordinateInModelSystem.hashCode();
    }
}