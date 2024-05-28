package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.GameCardImage;
import it.polimi.ingsw.gui.ZoomableScrollPane;
import it.polimi.ingsw.gui.dataStorage.MultiSystemCoordinate;
import it.polimi.ingsw.gui.dataStorage.ObservableHand;
import it.polimi.ingsw.gui.dataStorage.ObservarblePlayerBoard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class GameSceneController extends Controller {

    private static final ControllersEnum NAME = ControllersEnum.GAME_SCENE;

    private static final double cardWidth = 234;

    private static final Logger logger = LogManager.getLogger(GameSceneController.class);

    @FXML
    public StackPane contentPane;

    @FXML
    public Pane dragPane;
    private final ObservarblePlayerBoard playerBoard = new ObservarblePlayerBoard();
    private final ObservableHand hand = new ObservableHand();
    public StackPane rootPane;

    @FXML
    private GridPane playerBoardGridPane;

    @FXML
    private HBox handBoard;

    private int selectedCard;

    private void loadDummyData() {
        Parser parser = new Parser();
        ArrayList<GameCard> gameCards = parser.getResourceDeck().getCards();

        final HashMap<Coordinate, GameCard> origPlayerBoard = new HashMap<>();
        origPlayerBoard.put(new Coordinate(-20, -20), gameCards.get(1));
        origPlayerBoard.put(new Coordinate(20, 20), gameCards.get(1));
        origPlayerBoard.put(new Coordinate(0, 0), gameCards.get(0));
        origPlayerBoard.put(new Coordinate(1, 1), gameCards.get(1));
        origPlayerBoard.put(new Coordinate(-1, -1), gameCards.get(2));
        origPlayerBoard.put(new Coordinate(1, -1), gameCards.get(3));
        origPlayerBoard.put(new Coordinate(-1, 1), gameCards.get(4));

        hand.setCard(gameCards.get(5), 0);
        hand.setCard(gameCards.get(6), 1);
        hand.setCard(gameCards.get(7), 2);

        playerBoard.loadData(origPlayerBoard);
    }

    private void removeNodeFromGrid(int col, int row) {
        Node nodeToRemove = null;

        // Iterate over the children of the GridPane
        for (Node node : playerBoardGridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                // If the current node's coordinates match the specified coordinates, mark it for removal
                nodeToRemove = node;
                break;
            }
        }

        if (nodeToRemove != null) {
            // Remove the node from the GridPane
            playerBoardGridPane.getChildren().remove(nodeToRemove);
        }
    }

    @FXML
    public void initialize() {
        StackPane pane = new StackPane();
        pane.getStyleClass().add("black");
        playerBoardGridPane = new GridPane();
        playerBoardGridPane.gridLinesVisibleProperty().set(true);
        playerBoardGridPane.getStyleClass().add("player-board black"); // TODO: Add transparent
        playerBoardGridPane.setHgap(cardWidth / 100 * -22);
        playerBoardGridPane.setVgap(cardWidth / 100 * -27);
        pane.getChildren().add(playerBoardGridPane);

        ZoomableScrollPane scrollPane = new ZoomableScrollPane(pane);
        //scrollPane.getStyleClass().add("transparent");
        contentPane.getChildren().addFirst(scrollPane);

        // Handle the add of a new card to the player board
        playerBoard.addPropertyChangeListener(evt -> {
            String propertyName = evt.getPropertyName();
            MultiSystemCoordinate multiSystemCoordinate = (MultiSystemCoordinate) evt.getNewValue();
            Coordinate guiCoordinate = multiSystemCoordinate.getCoordinateInGUISystem();
            // If the card was removed from the network, it is removed from the GUI.
            if (propertyName.equals("remove")) {
                logger.debug("Removing card at {}", guiCoordinate);
                removeNodeFromGrid(guiCoordinate.x, guiCoordinate.y);
                return;
            }

            // If the card is added from the GUI, it is dispatched to the network.
            if (propertyName.equals("putFromGUI")) {
                Coordinate modelCoordinate = multiSystemCoordinate.getCoordinateInModelSystem();
                logger.debug("Sending new card at {} to network", modelCoordinate);
                // TODO: Send the card to the network
            }

            // Either if the card is added from the network or from the GUI, it is displayed on the board.
            logger.debug("Displaying card at {}", guiCoordinate);
            ImageView imageView = playerBoard.get(multiSystemCoordinate).getFront();
            playerBoardGridPane.add(imageView, guiCoordinate.x, guiCoordinate.y);
        });

        // Handle the add of a new card to the hand
        hand.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("setCard")) {
                GameCardImage card = (GameCardImage) evt.getNewValue();
                ImageView imageView = card.getFront();
                handBoard.getChildren().add(imageView);
                makeDraggable(imageView);
            }

            if (evt.getPropertyName().equals("removeCard")) {
                // TODO
            }
        });

        loadDummyData();
    }

    private void makeDraggable(ImageView imageView) {
        final Coordinate dragDelta = new Coordinate(0, 0);
        imageView.setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            if (imageView.getParent().equals(handBoard)) {
                imageView.getBoundsInLocal();
                Point2D originaPosition = imageView.localToScene(imageView.getBoundsInLocal().getMinX(), imageView.getBoundsInLocal().getMinY());
                Point2D newPos = dragPane.sceneToLocal(originaPosition.getX(), originaPosition.getY());
                logger.debug(originaPosition);

                // Create a placeholder with the same size as the imageView
                Region placeholder = new Region();
                placeholder.setPrefWidth(imageView.getFitWidth());
                placeholder.setPrefHeight(imageView.getFitHeight());

                // Replace the imageView with the placeholder in the parent
                selectedCard = handBoard.getChildren().indexOf(imageView);
                handBoard.getChildren().set(selectedCard, placeholder);

                dragPane.getChildren().add(imageView);
                imageView.relocate(newPos.getX(), newPos.getY());
                logger.debug("New pos: {}:{}", imageView.getLayoutX(), imageView.getLayoutY());

            }


            dragDelta.setLocation(imageView.getLayoutX() - mouseEvent.getSceneX(), imageView.getLayoutY() - mouseEvent.getSceneY());
            imageView.setCursor(Cursor.MOVE);
        });

        imageView.setOnMouseReleased(mouseEvent -> {
            logger.debug("Mouse released");
            boolean didIntersect = false;
            for (Node node : playerBoardGridPane.getChildren()) {
                Bounds boundsInDragPane = dragPane.localToScene(imageView.getBoundsInParent());
                Bounds boundsInPlayerBoard = playerBoardGridPane.localToScene(node.getBoundsInParent());

                // If the cards intersect
                if (boundsInPlayerBoard.intersects(boundsInDragPane)) {
                    // Get the respective barycenter. This is needed in order to calculate on which corner of the existing card the new one is placed
                    Point2D playerBoardCardBarycenter = new Point2D(boundsInPlayerBoard.getCenterX(), boundsInPlayerBoard.getCenterY());
                    Point2D dragPaneCardBarycenter = new Point2D(boundsInDragPane.getCenterX(), boundsInDragPane.getCenterY());
                    logger.debug("Intersection detected");
                    Coordinate newCardCoordinates = new Coordinate(GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
                    if (playerBoardCardBarycenter.getX() > dragPaneCardBarycenter.getX() && playerBoardCardBarycenter.getY() > dragPaneCardBarycenter.getY()) {
                        // The card is in the top left corner of the player board card
                        logger.debug("Top left corner");

                        // Set the new card Coordinates
                        newCardCoordinates.x += -1;
                        newCardCoordinates.y += -1;
                    } else if (playerBoardCardBarycenter.getX() < dragPaneCardBarycenter.getX() && playerBoardCardBarycenter.getY() < dragPaneCardBarycenter.getY()) {
                        // The card is in the bottom right corner of the player board card
                        logger.debug("Bottom right corner");

                        // Set the new card Coordinates
                        newCardCoordinates.x += 1;
                        newCardCoordinates.y += 1;
                    } else if (playerBoardCardBarycenter.getX() > dragPaneCardBarycenter.getX() && playerBoardCardBarycenter.getY() < dragPaneCardBarycenter.getY()) {
                        // The card is in the bottom left corner of the player board card
                        logger.debug("Bottom left corner");

                        // Set the new card Coordinates
                        newCardCoordinates.x += -1;
                        newCardCoordinates.y += 1;
                    } else {
                        // The card is in the top right corner of the player board card
                        logger.debug("Top right corner");

                        // Set the new card Coordinates
                        newCardCoordinates.x += 1;
                        newCardCoordinates.y += -1;
                    }
                    MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInGUISystem(newCardCoordinates);
                    // Add the card to the player board
                    playerBoard.putFromGUI(multiSystemCoordinate, hand.getCardImage(selectedCard));

                    // Remove the card from the hand
                    dragPane.getChildren().remove(imageView);
                    didIntersect = true;
                    // Remove the listeners
//                    imageView.setOnMousePressed(null);
//                    imageView.setOnMouseReleased(null);
//                    imageView.setOnMouseDragged(null);
//                    imageView.setOnMouseEntered(null);
                    break;
                }
            }
            if (!didIntersect) {
                dragPane.getChildren().remove(imageView);
                handBoard.getChildren().set(selectedCard, imageView);
            }
            imageView.setCursor(Cursor.DEFAULT);
        });

        imageView.setOnMouseDragged(mouseEvent -> {

            imageView.setLayoutX(mouseEvent.getSceneX() + dragDelta.getX());
            imageView.setLayoutY(mouseEvent.getSceneY() + dragDelta.getY());
        });

        imageView.setOnMouseEntered(mouseEvent -> {
            imageView.setCursor(Cursor.HAND);
        });
    }

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    /**
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     */
    @Override
    public void beforeMount() {

    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }
}
