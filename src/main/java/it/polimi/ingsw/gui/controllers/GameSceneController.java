package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.ZoomableScrollPane;
import it.polimi.ingsw.gui.controllers.gameSceneController.ChatController;
import it.polimi.ingsw.gui.controllers.gameSceneController.HandController;
import it.polimi.ingsw.gui.controllers.gameSceneController.RightSidebarController;
import it.polimi.ingsw.gui.dataStorage.*;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
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
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private ImageView resourceDeck;
    @FXML
    private ImageView goldDeck;
    @FXML
    private ImageView firstResourceField;
    @FXML
    private ImageView secondResourceField;
    @FXML
    private ImageView firstGoldField;
    @FXML
    private ImageView secondGoldField;

    @FXML
    private Node pointsPanel;

    private ObservedGameCard firstResourceFieldCard;
    private ObservedGameCard secondResourceFieldCard;
    private ObservedGameCard firstGoldFieldCard;
    private ObservedGameCard secondGoldFieldCard;
    private ObservedGameCard resourceDeckCard;
    private ObservedGameCard goldDeckCard;

    private HandController handController;
    private final ObservableDrawArea drawArea = new ObservableDrawArea();
    private RightSidebarController rightSidebarController;
    private ChatController chatController;

    private ObservedPlayerBoard observedPlayerBoard;

    private GameCard placedCard;

    private boolean isDrawingPhase;

    private GameItemStore gameItemStore;

    private void loadDummyData() {
        Parser parser = new Parser();
        ArrayList<ObjectiveCard> objectiveCards = parser.getObjectiveDeck().getCards();
        gameCards = parser.getResourceDeck().getCards();
        ArrayList<GameCard> goldCards = parser.getGoldDeck().getCards();

        final HashMap<Coordinate, GameCard> origPlayerBoard = new HashMap<>();
        origPlayerBoard.put(new Coordinate(0, 0), gameCards.get(0));
        origPlayerBoard.put(new Coordinate(1, 1), gameCards.get(1));
        origPlayerBoard.put(new Coordinate(-1, -1), gameCards.get(2));
        origPlayerBoard.put(new Coordinate(1, -1), gameCards.get(3));
        origPlayerBoard.put(new Coordinate(-1, 1), gameCards.get(4));


        //drawArea.loadData(globalBoardView);
        firstResourceFieldCard = new ObservedGameCard(goldCards.get(1), firstGoldField);
        secondResourceFieldCard = new ObservedGameCard(goldCards.get(2), secondGoldField);
        firstGoldFieldCard = new ObservedGameCard(gameCards.get(9), firstResourceField);
        secondGoldFieldCard = new ObservedGameCard(gameCards.get(10), secondResourceField);
        resourceDeckCard = new ObservedGameCard(gameCards.get(11), resourceDeck);
        goldDeckCard = new ObservedGameCard(goldCards.get(0), goldDeck);


        observedPlayerBoard.syncWithServerPlayerBoard(origPlayerBoard);
        playerPrompt.setText("Place a new card on the board");

        handController.update(new ArrayList<>(gameCards.subList(5, 8)));

        rightSidebarController.updateObjectiveCards(objectiveCards.getFirst(), new ArrayList<>(objectiveCards.subList(1, 3)));

        rightSidebarController.updateStats(new ArrayList<String>(Arrays.asList("Pier", "Marco", "Simo", "Mattia")), new ArrayList<Integer>(Arrays.asList(10, 20, 30, 40)), new GameItemStore());
        chatController.updateUsers(new ArrayList<>(Arrays.asList("Pier", "Marco", "Simo", "Mattia")));

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

    public boolean handleCardDrop(ImageView imageView, Pane parent) {
        boolean didIntersect = false;

        for (Node node : playerBoardGridPane.getChildren()) {
            Bounds boundsInDragPane = parent.localToScene(imageView.getBoundsInParent());
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

                // Obtain the NodeDataBinding associated with the card
                NodeGameCardBinding nodeGameCardBinding = (NodeGameCardBinding) imageView.getUserData();
                placedCard = nodeGameCardBinding.getGameCard();

                // Add the card to the player board
                observedPlayerBoard.putFromGUI(newCardCoordinates, imageView);
                //playerBoard.putFromGUI(multiSystemCoordinate, nodeGameCardBinding.getGameCardImage());

                // Remove the card from the hand
                parent.getChildren().remove(imageView);
                didIntersect = true;
                break;
            }
        }
        imageView.setCursor(Cursor.DEFAULT);
        return didIntersect;

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


        this.observedPlayerBoard = new ObservedPlayerBoard(playerBoardGridPane, networkControllerMapper);
        this.handController = new HandController(handBoard, this);
        rightSidebarController = new RightSidebarController(pointsPanel);
        chatController = new ChatController(ChatSideBar, ChatSideBarButton, networkControllerMapper);
        initializePlayerBoardGridPane();
        logger.fatal("DEBUG MODE ENABLED");
        loadDummyData();
    }

    private boolean canDrawCard() {
        if (!isDrawingPhase) {
            return false;
        }
        isDrawingPhase = false;
        return true;
    }

    @FXML
    private void drawCardFromResourceDeck() {
        if (canDrawCard()) {
            networkControllerMapper.drawCardFromResourceDeck();
        }
    }

    @FXML
    private void drawCardFromGoldDeck() {
        if (canDrawCard()) {
            networkControllerMapper.drawCardFromGoldDeck();
        }
    }

    private void loadUpdatedView(GameControllerView gameControllerView, String playerName) {
        if (gameControllerView.gameStatus() == GameStatusEnum.DRAW_CARD) {
            isDrawingPhase = true;
        }

        PlayerView playerView = gameControllerView.getPlayerViewByName(playerName);
        observedPlayerBoard.syncWithServerPlayerBoard(playerView.playerBoardView().playerBoard());
        List<String> playerNames = gameControllerView.gameView().playerViews().stream().map(PlayerView::playerName).toList();

        rightSidebarController.updateObjectiveCards(gameControllerView.getPlayerViewByName(playerName).objectiveCard(), gameControllerView.gameView().globalBoardView().globalObjectives());
        rightSidebarController.updateStats(playerNames, gameControllerView.gameView().playerViews().stream().map(PlayerView::playerPos).toList(), gameControllerView.gameView().getViewByPlayer(playerName).playerBoardView().gameItemStore());

        chatController.updateUsers(playerNames);

        gameItemStore = playerView.playerBoardView().gameItemStore();
        handController.update(playerView.playerHandView().hand());
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
        gameControllerView = (GameControllerView) evt.getNewValue();
        loadUpdatedView(gameControllerView, getProperty("playerName"));
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
        switch (propertyName) {
            case "ERROR":
                // Reset the view
                Platform.runLater(() -> loadUpdatedView(gameControllerView, getProperty("playerName")));
                break;
            case "UPDATE_VIEW":
                gameControllerView = (GameControllerView) evt.getNewValue();
                logger.debug("Received updated view");

                Platform.runLater(() -> loadUpdatedView(gameControllerView, getProperty("playerName")));
                break;
            case "CHAT_MESSAGE":
                chatController.handleChatMessage((ServerToClientMessage) evt.getNewValue());
                break;
        }
    }
}
