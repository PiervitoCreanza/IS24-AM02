package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.CardImage;
import it.polimi.ingsw.gui.ZoomableScrollPane;
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

    private final HashMap<Coordinate, GameCard> playerBoard = new HashMap<>();

    @FXML
    private GridPane playerBoardGridPane;

    @FXML
    private HBox handBoard;

    private int selectedCard;

    @FXML
    public void initialize() {

        VBox vbox = new VBox();
        Parser parser = new Parser();
        ArrayList<GameCard> gameCards = parser.getResourceDeck().getCards();
        playerBoardGridPane = new GridPane();
        playerBoardGridPane.getStyleClass().add("player-board transparent");
        playerBoardGridPane.setHgap(cardWidth / 100 * -22);
        playerBoardGridPane.setVgap(cardWidth / 100 * -27);
        playerBoardGridPane.add(new CardImage(parser.getStarterDeck().draw().getCardId()).getFront(), 1, 1);
        playerBoardGridPane.add(new CardImage(gameCards.get(0).getCardId()).getFront(), 0, 0);
        playerBoardGridPane.add(new CardImage(gameCards.get(1).getCardId()).getFront(), 2, 0);
        playerBoardGridPane.add(new CardImage(gameCards.get(12).getCardId()).getFront(), 2, 2);
        playerBoardGridPane.add(new CardImage(gameCards.get(20).getCardId()).getFront(), 3, 1);

        vbox.getChildren().add(playerBoardGridPane);
        ZoomableScrollPane scrollPane = new ZoomableScrollPane(vbox);
        scrollPane.getStyleClass().add("transparent");
        contentPane.getChildren().addFirst(scrollPane);
//
        ImageView card1 = new CardImage(gameCards.get(2).getCardId()).getFront();
        ImageView card2 = new CardImage(gameCards.get(3).getCardId()).getFront();
        ImageView card3 = new CardImage(gameCards.get(4).getCardId()).getFront();

        // Make the cards draggable
        makeDraggable(card1);
        makeDraggable(card2);
        makeDraggable(card3);

        handBoard.getChildren().add(card1);
        handBoard.getChildren().add(card2);
        handBoard.getChildren().add(card3);
    }

    private void checkIntersectionWithPlayerBoard(ImageView imageView) {
        playerBoardGridPane.getChildren().forEach(node -> {
            if (node.getBoundsInParent().intersects(imageView.getBoundsInParent())) {
                logger.debug("Intersection detected");
            }
        });
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

                if (boundsInPlayerBoard.intersects(boundsInDragPane)) {
                    Point2D playerBoardCardBaricenter = new Point2D(boundsInPlayerBoard.getCenterX(), boundsInPlayerBoard.getCenterY());
                    Point2D dragPaneCardBaricenter = new Point2D(boundsInDragPane.getCenterX(), boundsInDragPane.getCenterY());
                    logger.debug("Intersection detected");
                    if (playerBoardCardBaricenter.getX() > dragPaneCardBaricenter.getX() && playerBoardCardBaricenter.getY() > dragPaneCardBaricenter.getY()) {
                        // The card is in the top left corner of the player board card
                        logger.debug("Top left corner");
                        playerBoardGridPane.add(imageView, GridPane.getColumnIndex(node) - 1, GridPane.getRowIndex(node) - 1);
                    } else if (playerBoardCardBaricenter.getX() < dragPaneCardBaricenter.getX() && playerBoardCardBaricenter.getY() < dragPaneCardBaricenter.getY()) {
                        // The card is in the bottom right corner of the player board card
                        logger.debug("Bottom right corner");
                        playerBoardGridPane.add(imageView, GridPane.getColumnIndex(node) + 1, GridPane.getRowIndex(node) + 1);
                    } else if (playerBoardCardBaricenter.getX() > dragPaneCardBaricenter.getX() && playerBoardCardBaricenter.getY() < dragPaneCardBaricenter.getY()) {
                        // The card is in the bottom left corner of the player board card
                        logger.debug("Bottom left corner");
                        playerBoardGridPane.add(imageView, GridPane.getColumnIndex(node) - 1, GridPane.getRowIndex(node) + 1);
                    } else {
                        // The card is in the top right corner of the player board card
                        logger.debug("Top right corner");
                        logger.debug("COl: {}, ROW: {}", GridPane.getColumnIndex(node) + 1, GridPane.getRowIndex(node) - 1);
                        dragPane.getChildren().remove(imageView);
                        playerBoardGridPane.add(imageView, GridPane.getColumnIndex(node) + 1, GridPane.getRowIndex(node) - 1);
                    }

                    didIntersect = true;
                    // Remove the listeners
                    imageView.setOnMousePressed(null);
                    imageView.setOnMouseReleased(null);
                    imageView.setOnMouseDragged(null);
                    imageView.setOnMouseEntered(null);
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
