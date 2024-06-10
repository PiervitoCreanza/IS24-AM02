package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.ZoomableScrollPane;
import it.polimi.ingsw.gui.controllers.Controller;
import it.polimi.ingsw.gui.controllers.ControllersEnum;
import it.polimi.ingsw.gui.dataStorage.GameCardImageFactory;
import it.polimi.ingsw.gui.dataStorage.ObservableDrawArea;
import it.polimi.ingsw.gui.dataStorage.ObservedPlayerBoard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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

    private static final Logger logger = LogManager.getLogger(GameSceneController.class);
    private final SimpleBooleanProperty isDrawingPhase = new SimpleBooleanProperty(false);
    @FXML
    private Button hideChatButton;
    private GameControllerView gameControllerView;
    @FXML
    private HBox drawArea;
    @FXML
    private Text playerPrompt;
    @FXML
    private Button ChatSideBarButton;
    @FXML
    private Text playerName;

    @FXML
    private GridPane playerBoardGridPane;

    @FXML
    private HBox handBoard;
    @FXML
    private Parent ChatSideBar;

    @FXML
    private Node pointsPanel;

    private HandController handController;
    @FXML
    private StackPane contentPane;
    private RightSidebarController rightSidebarController;
    private ChatController chatController;

    private ObservedPlayerBoard observedPlayerBoard;
    private ObservableDrawArea observedDrawArea;
    private String currentPlayerView;

    @FXML
    public void initialize() {
        StackPane pane = new StackPane();
        pane.getStyleClass().add("transparent");
        playerBoardGridPane = new GridPane();
        playerBoardGridPane.getStyleClass().add("player-board transparent");
        playerBoardGridPane.setHgap(GameCardImageFactory.getDefaultCardWidth() / 100 * -22);
        playerBoardGridPane.setVgap(GameCardImageFactory.getDefaultCardWidth() / 100 * -27);
        pane.getChildren().add(playerBoardGridPane);
        ZoomableScrollPane scrollPane = new ZoomableScrollPane(pane);
        scrollPane.getStyleClass().add("transparent");
        contentPane.getChildren().addFirst(scrollPane);


        this.observedPlayerBoard = new ObservedPlayerBoard(playerBoardGridPane);
        this.observedDrawArea = new ObservableDrawArea(drawArea, isDrawingPhase);
        this.handController = new HandController(handBoard, this);
        this.rightSidebarController = new RightSidebarController(pointsPanel, this);
        this.chatController = new ChatController(ChatSideBar, ChatSideBarButton, networkControllerMapper);
        initializePlayerBoardGridPane();
        //loadDummyData();
    }

    private void loadDummyData() {
        logger.fatal("DEBUG MODE ENABLED");
        Parser parser = new Parser();
        ArrayList<ObjectiveCard> objectiveCards = parser.getObjectiveDeck().getCards();
        ArrayList<GameCard> gameCards = parser.getResourceDeck().getCards();
        ArrayList<GameCard> goldCards = parser.getGoldDeck().getCards();

        final HashMap<Coordinate, GameCard> origPlayerBoard = new HashMap<>();
        origPlayerBoard.put(new Coordinate(0, 0), gameCards.get(0));
        origPlayerBoard.put(new Coordinate(1, 1), gameCards.get(1));
        origPlayerBoard.put(new Coordinate(-1, -1), gameCards.get(2));
        origPlayerBoard.put(new Coordinate(1, -1), gameCards.get(3));
        origPlayerBoard.put(new Coordinate(-1, 1), gameCards.get(4));


        observedDrawArea.loadData(new GlobalBoardView(goldCards.get(10), gameCards.get(10), new ArrayList<>(goldCards.subList(11, 13)), new ArrayList<>(gameCards.subList(11, 13)), new ArrayList<>(objectiveCards.subList(10, 12))));


        observedPlayerBoard.syncWithServerPlayerBoard(origPlayerBoard);
        playerPrompt.setText("Place a new card on the board");

        handController.update(new ArrayList<>(gameCards.subList(5, 8)));

        rightSidebarController.updateObjectiveCards(objectiveCards.getFirst(), new ArrayList<>(objectiveCards.subList(1, 3)));

        rightSidebarController.updateStats(new ArrayList<String>(Arrays.asList("Pier", "Marco", "Simo", "Mattia")), new ArrayList<Integer>(Arrays.asList(10, 20, 30, 40)), new GameItemStore());
        chatController.updateUsers(new ArrayList<>(Arrays.asList("Pier", "Marco", "Simo", "Mattia")));

    }

    private void initializePlayerBoardGridPane() {
        // Add column constraints to set column width
        for (int i = 0; i <= 100; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(GameCardImageFactory.getDefaultCardWidth()); // Set preferred width of the column
            playerBoardGridPane.getColumnConstraints().add(colConstraints);
        }

        // Add row constraints to set row height
        for (int i = 0; i <= 100; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(GameCardImageFactory.getDefaultCardHeight()); // Set preferred height of the row
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

                // Add the card to the player board
                observedPlayerBoard.putFromGUI(newCardCoordinates, imageView);

                didIntersect = true;
                break;
            }
        }
        imageView.setCursor(Cursor.DEFAULT);
        return didIntersect;

    }

    public void updateView(String playerName) {
        currentPlayerView = playerName;
        this.playerName.setText(playerName);

        logger.debug("Playername: {}", playerName);
        PlayerView playerView = gameControllerView.getPlayerViewByName(playerName);
        observedPlayerBoard.syncWithServerPlayerBoard(playerView.playerBoardView().playerBoard());
        updatePrivateView(gameControllerView.gameStatus());
    }

    private void updatePrivateView(GameStatusEnum gameStatus) {
        if (gameStatus == GameStatusEnum.DRAW_CARD && gameControllerView.isMyTurn(getProperty("playerName"))) {
            isDrawingPhase.set(true);
        }

        switch (gameStatus) {
            case GAME_PAUSED -> this.playerPrompt.setText("The game is paused");
            case DRAW_CARD -> this.playerPrompt.setText("Draw a card from the deck");
            case PLACE_CARD -> this.playerPrompt.setText("Place a new card on the board");
        }
        String clientPlayerName = getProperty("playerName");
        PlayerView clientPlayerView = gameControllerView.getPlayerViewByName(clientPlayerName);
        handController.update(clientPlayerView.playerHandView().hand());
        observedDrawArea.loadData(gameControllerView.gameView().globalBoardView());
        rightSidebarController.updateObjectiveCards(clientPlayerView.objectiveCard(), gameControllerView.gameView().globalBoardView().globalObjectives());
        List<String> playerNames = gameControllerView.gameView().playerViews().stream().map(PlayerView::playerName).toList();
        rightSidebarController.updateStats(playerNames, gameControllerView.gameView().playerViews().stream().map(PlayerView::playerPos).toList(), clientPlayerView.playerBoardView().gameItemStore());
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
        updateView(getProperty("playerName"));
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }

    @FXML
    private void toggleChat() {
        boolean isVisible = ChatSideBar.isVisible();
        ChatSideBar.setVisible(!isVisible);
        ChatSideBarButton.setVisible(isVisible);
        ChatSideBarButton.setManaged(isVisible);
        hideChatButton.setVisible(!isVisible);
        hideChatButton.setManaged(!isVisible);
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
                Platform.runLater(() -> updateView(getProperty("playerName")));
                break;
            case "UPDATE_VIEW":
                gameControllerView = (GameControllerView) evt.getNewValue();
                logger.debug("Received updated view");
                String clientPlayerName = getProperty("playerName");

                if (gameControllerView.isMyTurn(clientPlayerName) && !clientPlayerName.equals(currentPlayerView)) {
                    // If it is the player turn force the view on his playerBoard.
                    currentPlayerView = clientPlayerName;
                }
                Platform.runLater(() -> updateView(currentPlayerView));

                break;
            case "CHAT_MESSAGE":
                chatController.handleChatMessage((ServerToClientMessage) evt.getNewValue());
                break;
        }
    }
}
