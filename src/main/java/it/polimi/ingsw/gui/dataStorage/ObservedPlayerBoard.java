package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Set;

public class ObservedPlayerBoard {
    private final ObservableMap<Coordinate, GameCard> playerBoard;
    private final ObservableMap<MultiSystemCoordinate, ImageView> renderedPlayerBoard;
    private final GridPane playerBoardGrid;
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    public ObservedPlayerBoard(GridPane boundPlayerBoardGrid, ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.playerBoard = FXCollections.observableHashMap();
        this.renderedPlayerBoard = FXCollections.observableHashMap();
        this.playerBoardGrid = boundPlayerBoardGrid;
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;

        // Handle modifications in the playerBoard
        playerBoard.addListener((MapChangeListener<Coordinate, GameCard>) change -> {
            MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInModelSystem(change.getKey());

            if (change.wasAdded()) {
                // Add the new card to the rendering map
                GameCard gameCard = change.getValueAdded();
                renderedPlayerBoard.put(multiSystemCoordinate, GameCardImageFactory.createGameCardImageView(gameCard));
            } else if (change.wasRemoved()) {
                // Remove the card from the rendering map
                renderedPlayerBoard.remove(multiSystemCoordinate);
            }
        });

        renderedPlayerBoard.addListener((MapChangeListener<MultiSystemCoordinate, ImageView>) change -> {
            MultiSystemCoordinate multiSystemCoordinate = change.getKey();
            Coordinate guiCoordinate = multiSystemCoordinate.getCoordinateInGUISystem();

            if (change.wasAdded()) {
                // Render new added cards.
                ImageView imageView = change.getValueAdded();
                playerBoardGrid.add(imageView, guiCoordinate.x, guiCoordinate.y);
            } else if (change.wasRemoved()) {
                // Remove from the view card that have been removed from the rendering map
                playerBoardGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == guiCoordinate.x && GridPane.getRowIndex(node) == guiCoordinate.y);
            }
        });
    }

    public void putFromGUI(Coordinate key, ImageView image) {
        // When a user sets a card, it is locally set to avoid lag.
        MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInGUISystem(key);
        renderedPlayerBoard.put(multiSystemCoordinate, image);

        // The card is then sent to the model and added to the local playerBoard
        GameCard card = (GameCard) image.getUserData();
        playerBoard.put(multiSystemCoordinate.getCoordinateInModelSystem(), card);

        clientNetworkControllerMapper.placeCard(key, card.getCardId(), card.isFlipped());
        System.out.println("Change made by GUI: " + key + " -> " + image);
    }


    public void syncWithServerPlayerBoard(HashMap<Coordinate, GameCard> playerBoard) {
        // The new keys from the Model are added to the local playerBoard
        this.playerBoard.putAll(playerBoard);
        Set<Coordinate> keys = this.playerBoard.keySet();
        // Remove all keys that are not in the new playerBoard
        for (Coordinate key : keys) {
            if (!playerBoard.containsKey(key)) {
                this.playerBoard.remove(key);
            }
        }
    }
}
