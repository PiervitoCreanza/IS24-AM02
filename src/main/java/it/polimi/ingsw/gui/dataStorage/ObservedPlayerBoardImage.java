package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.utils.Coordinate;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ObservedPlayerBoardImage {
    private final ObservableMap<Coordinate, ImageView> playerBoard;
    private final GridPane playerBoardGrid;

    public ObservedPlayerBoardImage(GridPane boundPlayerBoardGrid) {
        this.playerBoard = FXCollections.observableHashMap();
        this.playerBoardGrid = boundPlayerBoardGrid;

        playerBoard.addListener((MapChangeListener<Coordinate, ImageView>) change -> {
            if (change.wasAdded()) {
                MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInModelSystem(change.getKey());
                Coordinate guiCoordinate = multiSystemCoordinate.getCoordinateInGUISystem();
                ImageView imageView = change.getValueAdded();
                playerBoardGrid.add(imageView, guiCoordinate.x, guiCoordinate.y);
            } else if (change.wasRemoved()) {
                MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInModelSystem(change.getKey());
                Coordinate guiCoordinate = multiSystemCoordinate.getCoordinateInGUISystem();
                playerBoardGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == guiCoordinate.x && GridPane.getRowIndex(node) == guiCoordinate.y);
            }
        });
    }

    public void putFromGUI(Coordinate key, ImageView image) {
        playerBoard.put(key, image);
        System.out.println("Change made by GUI: " + key + " -> " + image);
    }

    public void putFromNetwork(Coordinate key, ImageView image) {
        playerBoard.put(key, image);
        System.out.println("Change made by Network: " + key + " -> " + image);
    }

}
