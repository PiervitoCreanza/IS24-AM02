package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.utils.Coordinate;

public class MultiSystemCoordinate {
    private Coordinate coordinateInModelSystem;
    private Coordinate coordinateInGUISystem;

    public MultiSystemCoordinate() {
    }

    public static Coordinate convertGUItoModel(Coordinate coordinate) {
        return new Coordinate(coordinate.x - 50, 50 - coordinate.y);
    }

    public static Coordinate convertModelToGUI(Coordinate coordinate) {
        return new Coordinate(coordinate.x + 50, 50 - coordinate.y);
    }

    public Coordinate getCoordinateInModelSystem() {
        return coordinateInModelSystem;
    }

    public MultiSystemCoordinate setCoordinateInModelSystem(int x, int y) {
        this.coordinateInModelSystem = new Coordinate(x, y);
        this.coordinateInGUISystem = convertModelToGUI(new Coordinate(x, y));
        return this;
    }

    public Coordinate getCoordinateInGUISystem() {
        return coordinateInGUISystem;
    }

    public MultiSystemCoordinate setCoordinateInGUISystem(int x, int y) {
        this.coordinateInGUISystem = new Coordinate(x, y);
        this.coordinateInModelSystem = convertGUItoModel(new Coordinate(x, y));
        return this;
    }

    public MultiSystemCoordinate setCoordinateInModelSystem(Coordinate coordinateInModelSystem) {
        this.coordinateInModelSystem = coordinateInModelSystem;
        this.coordinateInGUISystem = convertModelToGUI(coordinateInModelSystem);
        return this;
    }

    public MultiSystemCoordinate setCoordinateInGUISystem(Coordinate coordinateInGUISystem) {
        this.coordinateInGUISystem = coordinateInGUISystem;
        this.coordinateInModelSystem = convertGUItoModel(coordinateInGUISystem);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiSystemCoordinate that = (MultiSystemCoordinate) o;
        return coordinateInModelSystem.equals(that.coordinateInModelSystem);
    }

    @Override
    public int hashCode() {
        return coordinateInModelSystem.hashCode();
    }
}
