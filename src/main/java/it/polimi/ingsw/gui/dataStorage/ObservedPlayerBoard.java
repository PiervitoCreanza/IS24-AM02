package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.gui.components.GuiCardFactory;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ObservedPlayerBoard {

    private final Logger logger = LogManager.getLogger(ObservedPlayerBoard.class);
    private final ObservableMap<Coordinate, GameCard> playerBoard;
    private final ObservableMap<MultiSystemCoordinate, ImageView> renderedPlayerBoard;
    private final GridPane playerBoardGrid;
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    public ObservedPlayerBoard(GridPane boundPlayerBoardGrid) {
        this.playerBoard = FXCollections.observableHashMap();
        this.renderedPlayerBoard = FXCollections.observableHashMap();
        this.playerBoardGrid = boundPlayerBoardGrid;
        this.clientNetworkControllerMapper = ClientNetworkControllerMapper.getInstance();

        // Handle modifications in the playerBoard
        playerBoard.addListener((MapChangeListener<Coordinate, GameCard>) change -> {
            MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInModelSystem(change.getKey());

            if (change.wasAdded()) {
                // Add the new card to the rendering map
                GameCard gameCard = change.getValueAdded();
                renderedPlayerBoard.put(multiSystemCoordinate, GuiCardFactory.createImageView(gameCard));
            } else if (change.wasRemoved()) {
                // Remove the card from the rendering map
                renderedPlayerBoard.remove(multiSystemCoordinate);
            }
        });

        renderedPlayerBoard.addListener((MapChangeListener<MultiSystemCoordinate, ImageView>) change -> {
            MultiSystemCoordinate multiSystemCoordinate = change.getKey();
            Coordinate guiCoordinate = multiSystemCoordinate.getCoordinateInGUISystem();

            if (change.wasAdded()) {
                // Render newly added cards.
                ImageView imageView = change.getValueAdded();
                replaceNodeInGridPane(playerBoardGrid, imageView, guiCoordinate.x, guiCoordinate.y);
            } else if (change.wasRemoved()) {
                // Remove from the view card that have been removed from the rendering map
                playerBoardGrid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == guiCoordinate.x && GridPane.getRowIndex(node) == guiCoordinate.y);
            }
        });
    }

    public void putFromGUI(Coordinate key, ImageView imageView) {
        GameCard card = (GameCard) imageView.getUserData();
        // We need to create a new image in order to not have the card in playerBoard linked with the ObservedGameCard in hand.
        // To improve performances, we keep the same image element that has already been loaded.
        ImageView playerBoardImage = GuiCardFactory.createImageView(card, imageView.getImage());
        // When a user sets a card, it is locally set to avoid lag.
        MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInGUISystem(key);
        renderedPlayerBoard.put(multiSystemCoordinate, playerBoardImage);

        // The card is then sent to the model and added to the local playerBoard
        Coordinate modelCoordinate = multiSystemCoordinate.getCoordinateInModelSystem();
        playerBoard.put(modelCoordinate, card);

        clientNetworkControllerMapper.placeCard(modelCoordinate, card.getCardId(), card.isFlipped());
        logger.debug("Card with id: {} put by GUI at pos: ({},{})", card.getCardId(), modelCoordinate.x, modelCoordinate.y);
    }

    private boolean isCardInPlayerBoard(Coordinate key, GameCard card) {
        return !playerBoard.containsKey(key) || playerBoard.get(key).getCardId() != card.getCardId();
    }


    public void syncWithServerPlayerBoard(HashMap<Coordinate, GameCard> playerBoard) {
        // The new keys from the Model are added to the local playerBoard
        for (Coordinate key : playerBoard.keySet()) {
            if (isCardInPlayerBoard(key, playerBoard.get(key))) {
                this.playerBoard.put(key, playerBoard.get(key));
            }
        }
        Set<Coordinate> keys = new HashSet<>(this.playerBoard.keySet());
        // Remove all keys that are not in the new playerBoard
        for (Coordinate key : keys) {
            if (!playerBoard.containsKey(key)) {
                this.playerBoard.remove(key);
            }
        }
    }

    private void replaceNodeInGridPane(GridPane gridPane, Node newNode, int col, int row) {
        // Find and remove the existing node
        Node existingNode = getNodeFromGridPane(gridPane, col, row);
        if (existingNode != null) {
            gridPane.getChildren().remove(existingNode);
        }

        // Add the new node
        gridPane.add(newNode, col, row);
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
