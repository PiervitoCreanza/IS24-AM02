package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.gui.ObjectiveCardImage;
import it.polimi.ingsw.gui.dataStorage.GameCardImageFactory;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.store.GameItemStore;
import it.polimi.ingsw.network.virtualView.PlayerView;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RightSidebarController {
    private final Node root;
    private final ObservableList<ObjectiveCard> objectives;
    private final ListView<ObjectiveCard> objectivesList;
    private final SimpleObjectProperty<List<PlayerView>> playerViews = new SimpleObjectProperty<>();
    private final VBox playersList;
    private final VBox playersPoints;
    private final Label fungiAmount;
    private final Label plantAmount;
    private final Label animalAmount;
    private final Label insectAmount;
    private final Label quillAmount;
    private final Label inkwellAmount;
    private final Label manuscriptAmount;
    private final SimpleStringProperty currentlyDisplayedPlayer;
    private final Logger logger = LogManager.getLogger(RightSidebarController.class);

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
                            ImageView imageView = new ObjectiveCardImage(item).getImageView();
                            imageView.fitWidthProperty().set(150);
                            setGraphic(imageView);
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
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
                    Label playerLabel = new Label(playerView.playerName());
                    playerLabel.getStyleClass().add("player-label");

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

    private void updateCurrentlyDisplayedPlayer(String newPlayerName) {
        playersList.getChildren().forEach(playerLabel -> {
            if (playerLabel.getId().equals(newPlayerName)) {
                playerLabel.getStyleClass().add("selected-player");
            } else {
                playerLabel.getStyleClass().remove("selected-player");
            }
        });
    }

    private void adjustListViewHeight(ListView<?> listView) {
        double cellHeight = GameCardImageFactory.getHeightFromWidth(150) + 20;
        listView.setPrefHeight(Region.USE_COMPUTED_SIZE);
        listView.minHeightProperty().bind(Bindings.size(listView.getItems()).multiply(cellHeight).add(25));
        listView.maxHeightProperty().bind(Bindings.size(listView.getItems()).multiply(cellHeight).add(25));
    }

    private void handlePlayerClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        String playerName = clickedLabel.getText();
        // Logica per reindirizzare alla vista del giocatore
        currentlyDisplayedPlayer.set(playerName);
    }

    public void updateObjectiveCards(ObjectiveCard playerObjectiveCard, List<ObjectiveCard> globalObjectiveCards) {
        List<ObjectiveCard> newObjectives = new ArrayList<>();
        newObjectives.add(playerObjectiveCard);
        newObjectives.addAll(globalObjectiveCards);

        if (!objectives.equals(newObjectives)) {
            objectives.setAll(newObjectives);
        }
    }

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
