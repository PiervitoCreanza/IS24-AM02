package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.components.GuiCardFactory;
import it.polimi.ingsw.gui.components.ZoomableScrollPane;
import it.polimi.ingsw.gui.components.toast.ToastLevels;
import it.polimi.ingsw.gui.controllers.Controller;
import it.polimi.ingsw.gui.controllers.ControllersEnum;
import it.polimi.ingsw.gui.dataStorage.ObservableDrawArea;
import it.polimi.ingsw.gui.dataStorage.ObservedPlayerBoard;
import it.polimi.ingsw.gui.utils.GUIUtils;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import it.polimi.ingsw.network.virtualView.PlayerView;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

import static it.polimi.ingsw.gui.utils.GUIUtils.capitalizeFirstLetter;

/**
 * This class is responsible for controlling the right sidebar in the game scene.
 * It handles the display of objective cards, player views, and game item store.
 */
public class GameSceneController extends Controller implements PropertyChangeListener {

    /**
     * The name of the controller.
     */
    private static final ControllersEnum NAME = ControllersEnum.GAME_SCENE;

    /**
     * The (logger) of the class.
     */
    private static final Logger logger = LogManager.getLogger(GameSceneController.class);

    /**
     * The property that indicates if the game is in the drawing phase.
     */
    private final SimpleBooleanProperty isDrawingPhase = new SimpleBooleanProperty(false);

    /**
     * The root pane of the scene.
     */
    @FXML
    private StackPane rootPane;

    /**
     * The button to hide the chat sidebar.
     */
    @FXML
    private Button hideChatButton;

    /**
     * The property that indicates if the game is in the drawing phase.
     */
    private SimpleObjectProperty<GameControllerView> gameControllerView;

    /**
     * The draw area of the game scene.
     */
    @FXML
    private HBox drawArea;

    /**
     * The text that prompts the player.
     */
    @FXML
    private Text playerPrompt;

    /**
     * The button to show the chat sidebar.
     */
    @FXML
    private Button ChatSideBarButton;

    /**
     * The name of the player.
     */
    @FXML
    private Text playerName;

    /**
     * The grid pane of the player board.
     */
    @FXML
    private GridPane playerBoardGridPane;

    /**
     * The hand board of the player.
     */
    @FXML
    private HBox handBoard;

    /**
     * The chat sidebar.
     */
    @FXML
    private Parent ChatSideBar;

    /**
     * The right sidebar.
     */
    @FXML
    private Node rightSidebar;

    /**
     * The hand controller.
     */
    private HandController handController;

    /**
     * The content pane of the scene.
     */
    @FXML
    private StackPane contentPane;

    /**
     * The right sidebar controller.
     */
    private RightSidebarController rightSidebarController;

    /**
     * The chat controller.
     */
    private ChatController chatController;

    /**
     * The observed player board.
     */
    private ObservedPlayerBoard observedPlayerBoard;

    /**
     * The observed draw area.
     */
    private ObservableDrawArea observedDrawArea;

    /**
     * The property that indicates the currently displayed player.
     */
    private SimpleStringProperty currentlyDisplayedPlayer;

    /**
     * This class is responsible for controlling the right sidebar in the game scene.
     * It handles the display of objective cards, player views, and game item store.
     */
    @FXML
    public void initialize() {
        StackPane pane = new StackPane();
        pane.getStyleClass().add("transparent");
        playerBoardGridPane = new GridPane();
        playerBoardGridPane.getStyleClass().add("player-board transparent");
        playerBoardGridPane.setHgap(GuiCardFactory.getDefaultCardWidth() / 100 * -22);
        playerBoardGridPane.setVgap(GuiCardFactory.getDefaultCardWidth() / 100 * -27);
        pane.getChildren().add(playerBoardGridPane);
        ZoomableScrollPane scrollPane = new ZoomableScrollPane(pane);
        scrollPane.getStyleClass().add("transparent");
        contentPane.getChildren().addFirst(scrollPane);

        this.currentlyDisplayedPlayer = new SimpleStringProperty();
        this.currentlyDisplayedPlayer.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && gameControllerView.get() != null) {
                updateView(newValue);
            }
        });

        this.gameControllerView = new SimpleObjectProperty<>();
        this.gameControllerView.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && currentlyDisplayedPlayer.get() != null) {
                updateView(currentlyDisplayedPlayer.get());
            }
        });


        this.observedPlayerBoard = new ObservedPlayerBoard(playerBoardGridPane);
        this.observedDrawArea = new ObservableDrawArea(drawArea, isDrawingPhase);
        this.handController = new HandController(handBoard, this);
        this.rightSidebarController = new RightSidebarController(rightSidebar, currentlyDisplayedPlayer);
        this.chatController = new ChatController(ChatSideBar, ChatSideBarButton, networkControllerMapper);

        rootPane.onKeyPressedProperty().set(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showToast(ToastLevels.ERROR, "Disconnecting...", "You are disconnecting from the server.");
                networkControllerMapper.closeConnection();
            }
        });

        initializePlayerBoardGridPane();
        //loadDummyData();
    }

    /**
     * This method is used to load data into the game scene.
     * It is used for testing purposes.
     */
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

        //rightSidebarController.updateStats(new ArrayList<String>(Arrays.asList("Pier", "Marco", "Simo", "Mattia")), new ArrayList<Integer>(Arrays.asList(10, 20, 30, 40)), new GameItemStore());
        chatController.updateUsers(new ArrayList<>(Arrays.asList("Pier", "Marco", "Simo", "Mattia")), null);

    }

    /**
     * This method is used to initialize the player board grid pane.
     * It sets the column and row constraints for the grid pane.
     */
    private void initializePlayerBoardGridPane() {
        // Add column constraints to set column width
        for (int i = 0; i <= 100; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(GuiCardFactory.getDefaultCardWidth()); // Set the preferred width of the column
            playerBoardGridPane.getColumnConstraints().add(colConstraints);
        }

        // Add row constraints to set row height
        for (int i = 0; i <= 100; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(GuiCardFactory.getDefaultCardHeight()); // Set preferred height of the row
            playerBoardGridPane.getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * Handles the dropping of a card on the player board.
     * It checks if the card intersects with any existing cards on the player board and adds the card to the player board if it does.
     *
     * @param imageView The image view of the card.
     * @param parent    The parent pane of the card.
     * @return True if the card was added to the player board, false otherwise.
     */
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
                    newCardCoordinates.x -= 1;
                    newCardCoordinates.y -= 1;
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
                    newCardCoordinates.x -= 1;
                    newCardCoordinates.y += 1;
                } else {
                    // The card is in the top right corner of the player board card
                    logger.debug("Top right corner");

                    // Set the new card Coordinates
                    newCardCoordinates.x += 1;
                    newCardCoordinates.y -= 1;
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

    /**
     * Updates the view of the game scene based on the given player name.
     *
     * @param playerName The name of the player.
     */
    public void updateView(String playerName) {
        logger.debug("Playername: {}", playerName);
        GameControllerView gameControllerView = this.gameControllerView.get();

        PlayerView playerView = gameControllerView.getPlayerViewByName(playerName);
        observedPlayerBoard.syncWithServerPlayerBoard(playerView.playerBoardView().playerBoard());
        updatePrivateView(gameControllerView.gameStatus(), gameControllerView);
    }

    /**
     * Updates the private view of the game scene based on the given game status and game controller view.
     *
     * @param gameStatus         The game status.
     * @param gameControllerView The game controller view.
     */
    private void updatePrivateView(GameStatusEnum gameStatus, GameControllerView gameControllerView) {
        if (gameStatus == GameStatusEnum.DRAW_CARD && gameControllerView.isMyTurn(getProperty("playerName"))) {
            isDrawingPhase.set(true);
        }

        switch (gameStatus) {
            case GAME_PAUSED -> this.playerPrompt.setText("The game is paused");
            case DRAW_CARD -> this.playerPrompt.setText("is drawing a card...");
            case PLACE_CARD -> this.playerPrompt.setText("is placing a new card...");
            case INIT_CHOOSE_OBJECTIVE_CARD -> this.playerPrompt.setText("is choosing the objective card...");
            case INIT_PLACE_STARTER_CARD -> this.playerPrompt.setText("is choosing the starter card...");
            case INIT_CHOOSE_PLAYER_COLOR -> this.playerPrompt.setText("is choosing the player color...");
        }
        String clientPlayerName = getProperty("playerName");
        this.playerName.setText(GUIUtils.truncateString(gameControllerView.getCurrentPlayerView().playerName(), 20));
        PlayerView clientPlayerView = gameControllerView.getPlayerViewByName(clientPlayerName);
        handController.update(clientPlayerView.playerHandView().hand());
        observedDrawArea.loadData(gameControllerView.gameView().globalBoardView());
        rightSidebarController.updateObjectiveCards(clientPlayerView.objectiveCard(), gameControllerView.gameView().globalBoardView().globalObjectives());
        List<String> playerNames = gameControllerView.gameView().playerViews().stream().map(PlayerView::playerName).toList();
        rightSidebarController.updateStats(gameControllerView.getPlayerViews(), clientPlayerView.playerBoardView().gameItemStore());
        ArrayList<String> opponentPlayers = new ArrayList<>(playerNames);
        opponentPlayers.remove(clientPlayerName);
        chatController.updateUsers(opponentPlayers, clientPlayerName);
    }

    /**
     * Checks if the card placement is allowed.
     *
     * @return true if the card placement is allowed, false otherwise.
     */
    public boolean isCardPlacementForbidden() {
        return !gameControllerView.get().isMyTurn(getProperty("playerName")) || isDrawingPhase.get();
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
        gameControllerView.set((GameControllerView) evt.getNewValue());
        currentlyDisplayedPlayer.set(getProperty("playerName"));
    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

    }

    /**
     * Toggles the visibility of the chat sidebar.
     */
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
        // TODO Check if error works
        super.propertyChange(evt);
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case "ERROR":
                // Reset the view
                Platform.runLater(() -> updateView(getProperty("playerName")));
                break;
            case "UPDATE_VIEW":
                GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
                GameControllerView oldGameControllerView = this.gameControllerView.get();
                Platform.runLater(() -> this.gameControllerView.set(gameControllerView));
                logger.debug("Received updated view");
                String clientPlayerName = getProperty("playerName");

                if (gameControllerView.isMyTurn(clientPlayerName) && !clientPlayerName.equals(currentlyDisplayedPlayer.get())) {
                    // If it is the player turn force the view on his playerBoard.
                    Platform.runLater(() -> currentlyDisplayedPlayer.set(clientPlayerName));
                }

                if (gameControllerView.gameStatus() == GameStatusEnum.GAME_OVER) {
                    Platform.runLater(() -> switchScene(ControllersEnum.WINNER_SCENE, evt));
                }

                if (gameControllerView.gameStatus() == GameStatusEnum.PLACE_CARD && !gameControllerView.isMyTurn(clientPlayerName)) {
                    showToast(ToastLevels.INFO, GUIUtils.truncateString(capitalizeFirstLetter(gameControllerView.getCurrentPlayerView().playerName())), "It's " + GUIUtils.truncateString(capitalizeFirstLetter(gameControllerView.getCurrentPlayerView().playerName())) + "'s turn");
                }

                if (gameControllerView.isMyTurn(clientPlayerName) && !gameControllerView.gameStatus().equals(GameStatusEnum.GAME_OVER)) {
                    if (!gameControllerView.isLastRound()) {
                        switch (gameControllerView.gameStatus()) {
                            case DRAW_CARD ->
                                    showToast(ToastLevels.WARNING, "Your turn", "It's your turn to draw a card.");
                            case PLACE_CARD ->
                                    showToast(ToastLevels.WARNING, "Your turn", "It's your turn to place a card.");
                            default -> showToast(ToastLevels.WARNING, "Your turn", "It's your turn.");
                        }
                        return;
                    }

                    if (gameControllerView.remainingRoundsToEndGame() == 1 || (oldGameControllerView.remainingRoundsToEndGame() != gameControllerView.remainingRoundsToEndGame() && oldGameControllerView.gameStatus().equals(GameStatusEnum.PLACE_CARD))) {
                        showToast(ToastLevels.WARNING, "Second last round", "This is your second last round of the game.");
                    } else {
                        showToast(ToastLevels.WARNING, "Last round", "This is your last round of the game.");
                    }
                }


                break;
            case "CHAT_MESSAGE":
                logger.debug("Received chat message");
                ServerToClientMessage message = (ServerToClientMessage) evt.getNewValue();
                Platform.runLater(() -> chatController.handleChatMessage(message));
                // Open the chat if the message is a global message or if the message is to the player
                if (!ChatSideBar.isVisible()) {
                    toggleChat();
                }
                break;
        }
    }
}
