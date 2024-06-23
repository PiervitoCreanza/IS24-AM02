package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.gui.components.GuiCardFactory;
import it.polimi.ingsw.gui.utils.GUIUtils;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.tui.utils.ColorsEnum;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class is responsible for controlling the right sidebar in the game scene.
 * It handles the display of objective cards, player views, and game item store.
 */
public class RightSidebarController {

    /**
     * The root node of the right sidebar UI.
     */
    private final Node root;

    /**
     * The list of objective cards.
     */
    private final ObservableList<ObjectiveCard> objectives;

    /**
     * The ListView of objective cards.
     */
    private final ListView<ObjectiveCard> objectivesList;

    /**
     * The property for the player's objective card.
     */
    private final SimpleObjectProperty<ObjectiveCard> playerObjectiveCard = new SimpleObjectProperty<>();

    /**
     * The container for the player's objective card.
     */
    private final HBox playerObjectiveContainer;

    /**
     * The property for the player views.
     */
    private final SimpleObjectProperty<List<PlayerView>> playerViews = new SimpleObjectProperty<>();

    /**
     * The VBox of players.
     */
    private final VBox playersList;

    /**
     * The VBox of players' points.
     */
    private final VBox playersPoints;

    /**
     * The label for the number of fungi.
     */
    private final Label fungiAmount;

    /**
     * The label for the number of plants.
     */
    private final Label plantAmount;

    /**
     * The label for the number of animals.
     */
    private final Label animalAmount;

    /**
     * The label for the number of insects.
     */
    private final Label insectAmount;

    /**
     * The label for the number of quills.
     */
    private final Label quillAmount;

    /**
     * The label for the number of inkwells.
     */
    private final Label inkwellAmount;

    /**
     * The label for the number of manuscripts.
     */
    private final Label manuscriptAmount;

    /**
     * The property for the currently displayed player.
     */
    private final SimpleStringProperty currentlyDisplayedPlayer;

    /**
     * The (logger) of the class.
     */
    private final Logger logger = LogManager.getLogger(RightSidebarController.class);

    /**
     * Constructor for the RightSidebarController class.
     * It initializes the root node, currently displayed player, objectives list, and other UI elements.
     * It also sets up listeners for the currently displayed player and player views.
     *
     * @param root                     The root node of the right sidebar UI.
     * @param currentlyDisplayedPlayer The currently displayed player.
     */
    public RightSidebarController(Node root, SimpleStringProperty currentlyDisplayedPlayer) {
        this.root = root;
        this.currentlyDisplayedPlayer = currentlyDisplayedPlayer;
        this.objectivesList = (ListView<ObjectiveCard>) root.lookup("#objectivesList");
        this.objectives = FXCollections.observableArrayList(new ArrayList<>());
        this.objectivesList.setItems(objectives);
        this.playersList = (VBox) root.lookup("#playersList");
        this.playersPoints = (VBox) root.lookup("#playersPoints");
        this.fungiAmount = (Label) root.lookup("#fungiAmount");
        this.plantAmount = (Label) root.lookup("#plantAmount");
        this.animalAmount = (Label) root.lookup("#animalAmount");
        this.insectAmount = (Label) root.lookup("#insectAmount");
        this.quillAmount = (Label) root.lookup("#quillAmount");
        this.inkwellAmount = (Label) root.lookup("#inkwellAmount");
        this.manuscriptAmount = (Label) root.lookup("#manuscriptAmount");
        this.playerObjectiveContainer = (HBox) root.lookup("#playerObjective");

        currentlyDisplayedPlayer.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateCurrentlyDisplayedPlayer(newValue);

            }
        });

        // Set the cell factory for the ListView
        objectivesList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ObjectiveCard> call(ListView<ObjectiveCard> param) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(ObjectiveCard item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            ImageView imageView = GuiCardFactory.createImageView(item);
                            imageView.fitWidthProperty().set(150);
                            setGraphic(imageView);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        });

        // Set the listener for the playerObjectiveCard property
        playerObjectiveCard.addListener((observableValue, oldObjective, newObjective) -> {
            if (newObjective != null) {
                this.playerObjectiveContainer.getChildren().clear();
                ImageView imageView = GuiCardFactory.createImageView(newObjective);
                imageView.fitWidthProperty().set(150);
                this.playerObjectiveContainer.getChildren().add(imageView);
            }
        });

        // Set the listener for the playerViews property
        playerViews.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Sort by descending position
                newValue.sort(Comparator.comparingInt(PlayerView::playerPos).reversed());

                // Clear the players and respective points list
                playersList.getChildren().clear();
                playersPoints.getChildren().clear();

                // Create the new list of players and their points
                newValue.forEach(playerView -> {
                    Label playerLabel = new Label(GUIUtils.truncateString(playerView.playerName()));
                    playerLabel.getStyleClass().add("player-label");

                    ColorsEnum playerColor = playerView.color().getColor();

                    // If the player has already chosen a color, in that case add the respective css class.
                    if (playerColor != null) {
                        playerLabel.getStyleClass().add(playerColor.getTextCssClassName());
                    }

                    // If the player is disconnected add a specific class
                    if (!playerView.isConnected()) {
                        playerLabel.getStyleClass().add("player-disconnected");
                    }

                    playerLabel.setOnMouseClicked(this::handlePlayerClick);
                    playerLabel.setId(playerView.playerName());
                    playersList.getChildren().addLast(playerLabel);

                    Label pointsLabel = new Label(String.valueOf(playerView.playerPos()));
                    pointsLabel.getStyleClass().add("score-label");
                    playersPoints.getChildren().addLast(pointsLabel);
                });
                updateCurrentlyDisplayedPlayer(currentlyDisplayedPlayer.get());
            }
        });

        // Adjust the height of the ListView
        adjustListViewHeight(objectivesList);
    }

    /**
     * Updates the currently displayed player.
     * It adds or removes the "selected-player" style class from player labels based on the new player name.
     *
     * @param newPlayerName The new player name.
     */
    private void updateCurrentlyDisplayedPlayer(String newPlayerName) {
        playersList.getChildren().forEach(playerLabel -> {
            if (playerLabel.getId().equals(newPlayerName)) {
                playerLabel.getStyleClass().add("selected-player");
            } else {
                playerLabel.getStyleClass().remove("selected-player");
            }
        });
    }

    /**
     * Adjusts the height of the ListView.
     * It binds the minimum and maximum height of the ListView to the size of its items.
     *
     * @param listView The ListView to adjust.
     */
    private void adjustListViewHeight(ListView<?> listView) {
        double cellHeight = GuiCardFactory.getHeightFromWidth(150) + 20;
        listView.setPrefHeight(Region.USE_COMPUTED_SIZE);
        listView.minHeightProperty().bind(Bindings.size(listView.getItems()).multiply(cellHeight).add(5));
        listView.maxHeightProperty().bind(Bindings.size(listView.getItems()).multiply(cellHeight).add(5));
    }

    /**
     * Handles the player click event.
     * It sets the currently displayed player to the clicked player.
     *
     * @param event The mouse event.
     */
    private void handlePlayerClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        String playerName = clickedLabel.getId();
        // Logica per reindirizzare alla vista del giocatore
        currentlyDisplayedPlayer.set(playerName);
    }

    /**
     * Updates the objective cards.
     * It sets the objective list to the new list of objective cards if it is different from the current one.
     *
     * @param playerObjectiveCard  The player's objective card.
     * @param globalObjectiveCards The list of global objective cards.
     */
    public void updateObjectiveCards(ObjectiveCard playerObjectiveCard, List<ObjectiveCard> globalObjectiveCards) {
        this.playerObjectiveCard.set(playerObjectiveCard);
        if (!objectives.equals(globalObjectiveCards)) {
            objectives.setAll(globalObjectiveCards);
        }
    }

    /**
     * Updates the stats.
     * It sets the player views and updates the game item store.
     *
     * @param playerViews   The list of player views.
     * @param gameItemStore The game item store.
     */
    public void updateStats(List<PlayerView> playerViews, GameItemStore gameItemStore) {
        this.playerViews.set(playerViews);

        gameItemStore.keySet().forEach(item -> {
            switch (item) {
                case FUNGI:
                    fungiAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
                case PLANT:
                    plantAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
                case ANIMAL:
                    animalAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
                case INSECT:
                    insectAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
                case QUILL:
                    quillAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
                case INKWELL:
                    inkwellAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
                case MANUSCRIPT:
                    manuscriptAmount.setText(String.valueOf(gameItemStore.get(item)));
                    break;
            }
        });
    }
}
