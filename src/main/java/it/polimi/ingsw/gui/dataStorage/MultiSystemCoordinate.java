package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.utils.Coordinate;

public class MultiSystemCoordinate {
    private Coordinate coordinateInModelSystem;
    private Coordinate coordinateInGUISystem;

    public MultiSystemCoordinate() {
    }

    public MultiSystemCoordinate setCoordinateInModelSystem(int x, int y) {
        this.coordinateInModelSystem = new Coordinate(x, y);
        this.coordinateInGUISystem = new Coordinate(coordinateInModelSystem.x + 50, coordinateInModelSystem.y + 50);
        return this;
    }

    public MultiSystemCoordinate setCoordinateInGUISystem(int x, int y) {
        this.coordinateInGUISystem = new Coordinate(x, y);
        this.coordinateInModelSystem = new Coordinate(coordinateInGUISystem.x - 50, coordinateInGUISystem.y - 50);
        return this;
    }

    public Coordinate getCoordinateInModelSystem() {
        return coordinateInModelSystem;
    }

    public MultiSystemCoordinate setCoordinateInModelSystem(Coordinate coordinateInModelSystem) {
        this.coordinateInModelSystem = coordinateInModelSystem;
        this.coordinateInGUISystem = new Coordinate(coordinateInModelSystem.x + 50, coordinateInModelSystem.y + 50);
        return this;
    }

    public Coordinate getCoordinateInGUISystem() {
        return coordinateInGUISystem;
    }

    public MultiSystemCoordinate setCoordinateInGUISystem(Coordinate coordinateInGUISystem) {
        this.coordinateInGUISystem = coordinateInGUISystem;
        this.coordinateInModelSystem = new Coordinate(coordinateInGUISystem.x - 50, coordinateInGUISystem.y - 50);
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
