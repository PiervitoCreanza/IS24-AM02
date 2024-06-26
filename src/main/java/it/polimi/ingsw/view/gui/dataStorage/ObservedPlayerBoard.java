package it.polimi.ingsw.view.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.view.gui.components.GuiCardFactory;
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

/**
 * Class representing the observed player board.
 * It contains the logic for handling changes in the player's board and rendering them in the GUI.
 */
public class ObservedPlayerBoard {

    /**
     * Logger for this class.
     */
    private final Logger logger = LogManager.getLogger(ObservedPlayerBoard.class);

    /**
     * Observable map representing the player's board.
     * It mirrors the player's board in the model to keep track of changes and updating only the diffs.
     * It maps coordinates to game cards.
     */
    private final ObservableMap<Coordinate, GameCard> playerBoard;

    /**
     * Observable map representing the rendered player's board.
     * It maps multi-system coordinates to image views.
     * The multi-system coordinates are used to map the coordinates in the model system to the coordinates in the GUI system.
     */
    private final ObservableMap<MultiSystemCoordinate, ImageView> renderedPlayerBoard;

    /**
     * GridPane representing the player's board grid.
     */
    private final GridPane playerBoardGrid;

    /**
     * Instance of the ClientNetworkControllerMapper.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * Constructor for the ObservedPlayerBoard class.
     * It initializes the player board, the rendered player board, the player board grid, and the client network controller mapper.
     * It also sets up listeners for changes in the player board and the rendered player board.
     *
     * @param boundPlayerBoardGrid the GridPane representing the player's board grid
     */
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

    /**
     * Method to add a card to the player board from the GUI.
     * It creates a new image view for the card, adds it to the rendering map and the player board, and sends a place card action to the server.
     *
     * @param key       the coordinate of the card in the GUI system
     * @param imageView the image view of the card
     */
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

    /**
     * Method to check if a card is in the player board.
     *
     * @param key  the coordinate of the card
     * @param card the game card to check
     * @return true if the card is not in the player board or if it has a different ID, false otherwise
     */
    private boolean isCardInPlayerBoard(Coordinate key, GameCard card) {
        return !playerBoard.containsKey(key) || playerBoard.get(key).getCardId() != card.getCardId();
    }

    /**
     * Method to synchronize the local player board with the server player board.
     * It adds new keys from the server player board to the local player board and removes keys that are not in the server player board.
     *
     * @param playerBoard the server player board
     */
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

    /**
     * Method to replace a node in a GridPane.
     *
     * @param gridPane the GridPane to modify
     * @param newNode  the new node to add
     * @param col      the column of the node
     * @param row      the row of the node
     */
    private void replaceNodeInGridPane(GridPane gridPane, Node newNode, int col, int row) {
        // Find and remove the existing node
        Node existingNode = getNodeFromGridPane(gridPane, col, row);
        if (existingNode != null) {
            gridPane.getChildren().remove(existingNode);
        }

        // Get the card to place
        GameCard cardToPlace = (GameCard) newNode.getUserData();
        int placementIndex = cardToPlace.getPlacementIndex();

        // If we are placing the card from the GUI, the placement index is not set yet.
        if (placementIndex == 0) {
            // If the placement index is not set, set it to the highest placement index in the player board + 1
            placementIndex = playerBoard.values().stream()
                    .map(GameCard::getPlacementIndex)
                    .max(Integer::compareTo)
                    .orElse(0) + 1;
            cardToPlace.setPlacementIndex(placementIndex);
        }

        // Set the view order to the placement index to ensure the correct rendering order
        // The index is negated to ensure that the card with the highest placement index is rendered at the bottom
        // In fact a node with a lower view order is rendered on top of a node with a higher view order
        newNode.setViewOrder(-placementIndex);

        // Add the new node
        gridPane.add(newNode, col, row);

        logger.debug("Card: {} set on z-index: {}", cardToPlace.getCardId(), placementIndex);
    }

    /**
     * Method to get a node from a GridPane.
     *
     * @param gridPane the GridPane to get the node from
     * @param col      the column of the node
     * @param row      the row of the node
     * @return the node at the specified column and row, or null if no such node exists
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}