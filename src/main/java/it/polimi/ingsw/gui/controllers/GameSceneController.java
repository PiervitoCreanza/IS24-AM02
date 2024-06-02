package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.GameCardImage;
import it.polimi.ingsw.gui.ZoomableScrollPane;
import it.polimi.ingsw.gui.dataStorage.MultiSystemCoordinate;
import it.polimi.ingsw.gui.dataStorage.ObservableDrawArea;
import it.polimi.ingsw.gui.dataStorage.ObservableHand;
import it.polimi.ingsw.gui.dataStorage.ObservarblePlayerBoard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GameSceneController extends Controller implements PropertyChangeListener {

    private static final ControllersEnum NAME = ControllersEnum.GAME_SCENE;

    private static final double cardWidth = 234;
    private static final double cardRatio = 1.5;

    private static final Logger logger = LogManager.getLogger(GameSceneController.class);
    private GameControllerView gameControllerView;
    @FXML
    public StackPane contentPane;

    @FXML
    public Pane dragPane;

    @FXML
    private final ObservableHand hand = new ObservableHand();
    private final ObservarblePlayerBoard playerBoard = new ObservarblePlayerBoard();
    @FXML
    public StackPane rootPane;
    @FXML
    private Text playerPrompt;
    @FXML
    private Button ChatSideBarButton;
    private ArrayList<GameCard> gameCards;
    @FXML
    private Text playerName;

    @FXML
    private GridPane playerBoardGridPane;

    @FXML
    private HBox handBoard;
    @FXML
    private Parent ChatSideBar;
    @FXML
    private HBox resourceDeck;
    @FXML
    private HBox goldDeck;
    @FXML
    private HBox firstResourceField;
    @FXML
    private HBox secondResourceField;
    @FXML
    private HBox firstGoldField;
    @FXML
    private HBox secondGoldField;
    private final ObservableDrawArea drawArea = new ObservableDrawArea();

    private int selectedCard;

    private void loadDummyData() {
        Parser parser = new Parser();
        gameCards = parser.getResourceDeck().getCards();
        ArrayList<GameCard> goldCards = parser.getGoldDeck().getCards();

        final HashMap<Coordinate, GameCard> origPlayerBoard = new HashMap<>();
        origPlayerBoard.put(new Coordinate(0, 0), gameCards.get(0));
        origPlayerBoard.put(new Coordinate(1, 1), gameCards.get(1));
        origPlayerBoard.put(new Coordinate(-1, -1), gameCards.get(2));
        origPlayerBoard.put(new Coordinate(1, -1), gameCards.get(3));
        origPlayerBoard.put(new Coordinate(-1, 1), gameCards.get(4));

        hand.setCard(gameCards.get(5), 0);
        hand.setCard(gameCards.get(6), 1);
        hand.setCard(gameCards.get(7), 2);

        GlobalBoardView globalBoardView = new GlobalBoardView(
                goldCards.get(1),
                gameCards.get(9),
                new ArrayList<>(goldCards.subList(2, 4)),
                new ArrayList<>(gameCards.subList(12, 14)),
                new ArrayList<>(parser.getObjectiveDeck().getCards().subList(0, 2))
        );


        drawArea.loadData(globalBoardView);

        playerBoard.loadData(origPlayerBoard);
        playerPrompt.setText("Place a new card on the board");

    }

    private void setPlayerPrompt(String prompt) {
        playerPrompt.setText(playerPrompt.getText() + " " + prompt);
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

    private void initializePlayerBoardGridPane() {
        // Add column constraints to set column width
        for (int i = 0; i <= 100; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(cardWidth); // Set preferred width of the column
            playerBoardGridPane.getColumnConstraints().add(colConstraints);
        }

        // Add row constraints to set row height
        for (int i = 0; i <= 100; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(cardWidth / cardRatio); // Set preferred height of the row
            playerBoardGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    @FXML
    public void initialize() {
        StackPane pane = new StackPane();
        pane.getStyleClass().add("transparent");
        playerBoardGridPane = new GridPane();
        playerBoardGridPane.getStyleClass().add("player-board transparent");
        playerBoardGridPane.setHgap(cardWidth / 100 * -22);
        playerBoardGridPane.setVgap(cardWidth / 100 * -27);
        pane.getChildren().add(playerBoardGridPane);
        ZoomableScrollPane scrollPane = new ZoomableScrollPane(pane);
        scrollPane.getStyleClass().add("transparent");
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
            ImageView imageView = playerBoard.get(multiSystemCoordinate).getCurrentSide();
            playerBoardGridPane.add(imageView, guiCoordinate.x, guiCoordinate.y);
        });

        // Handle the add of a new card to the hand
        hand.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("setCard")) {
                GameCardImage card = (GameCardImage) evt.getNewValue();
                int index = (int) evt.getOldValue();
                ImageView imageView = card.getCurrentSide();
                if (handBoard.getChildren().size() <= index) {
                    handBoard.getChildren().add(imageView);
                } else {
                    handBoard.getChildren().set(index, imageView);
                }
                makeDraggable(imageView);
            }

            if (evt.getPropertyName().equals("removeCard")) {
                // TODO
            }
        });

        drawArea.addPropertyChangeListener(evt -> {
            String eventName = evt.getPropertyName();
            GameCard gameCard = (GameCard) evt.getNewValue();
            ImageView imageView;
            if (gameCard != null) {
                imageView = new GameCardImage(gameCard.getCardId()).getCurrentSide();
            } else {
                imageView = new ImageView();
                imageView.setFitWidth(cardWidth);
                imageView.setFitHeight(cardWidth / cardRatio);
            }

            if (!eventName.equals("firstGoldCard") && !eventName.equals("firstResourceCard")) {
                imageView.setFitWidth(109.5);
                imageView.setFitHeight(109.5 / cardRatio);
            }

            switch (eventName) {
                case "firstGoldCard":
                    goldDeck.getChildren().clear();
                    goldDeck.getChildren().add(imageView);
                    break;
                case "firstResourceCard":
                    resourceDeck.getChildren().clear();
                    resourceDeck.getChildren().add(imageView);
                    break;
                case "firstGoldFieldCard":
                    firstResourceField.getChildren().clear();
                    firstResourceField.getChildren().add(imageView);
                    break;
                case "secondGoldFieldCard":
                    secondGoldField.getChildren().clear();
                    secondGoldField.getChildren().add(imageView);
                    break;
                case "firstResourceFieldCard":
                    firstGoldField.getChildren().clear();
                    firstGoldField.getChildren().add(imageView);
                    break;
                case "secondResourceFieldCard":
                    secondResourceField.getChildren().clear();
                    secondResourceField.getChildren().add(imageView);
                    break;
            }
        });

        initializePlayerBoardGridPane();
        loadDummyData();
    }

    private void makeDraggable(ImageView imageView) {
        final Coordinate dragDelta = new Coordinate(0, 0);

        imageView.setOnMousePressed(mouseEvent -> {
            // Save the index of the selected card
            selectedCard = handBoard.getChildren().indexOf(imageView);

            // If the right mouse button is pressed, switch the card
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                logger.debug("Context menu requested");
                hand.switchCard(selectedCard);
                return;
            }
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
                handBoard.getChildren().set(selectedCard, placeholder);

                dragPane.getChildren().add(imageView);
                imageView.relocate(newPos.getX(), newPos.getY());
                logger.debug("New pos: {}:{}", imageView.getLayoutX(), imageView.getLayoutY());

            }


            dragDelta.setLocation(imageView.getLayoutX() - mouseEvent.getSceneX(), imageView.getLayoutY() - mouseEvent.getSceneY());
            imageView.setCursor(Cursor.MOVE);
        });

        imageView.setOnMouseReleased(mouseEvent -> {
            // If the right mouse button is released, do nothing
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                return;
            }
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
                    Integer xCoord = GridPane.getColumnIndex(node);
                    Integer yCoord = GridPane.getRowIndex(node);

                    // This check is needed as some cells are filled with empty elements and the coordinates are null
                    if (xCoord == null || yCoord == null) {
                        continue;
                    }

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

    private void loadUpdatedView(GameControllerView gameControllerView, String playerName) {
        PlayerView playerView = gameControllerView.getPlayerViewByName(playerName);
        hand.loadData(playerView.playerHandView().hand());
        playerBoard.loadData(playerView.playerBoardView().playerBoard());
        drawArea.loadData(gameControllerView.gameView().globalBoardView());
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
     * If the switchScene was caused by a property change, the event is passed as an argument.
     *
     * @param evt the property change event that caused the switch.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {

    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }

    @FXML
    private void showChat(ActionEvent actionEvent) {
        ChatSideBar.setVisible(true);
        ChatSideBarButton.setVisible(false);
        ChatSideBarButton.setManaged(false);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("UPDATE_VIEW")) {
            gameControllerView = (GameControllerView) evt.getNewValue();
            logger.debug("Received updated view");
            Platform.runLater(() -> loadUpdatedView(gameControllerView, getProperty("playerName")));
        }
    }
}
