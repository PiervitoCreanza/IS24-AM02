package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.data.Parser;
import it.polimi.ingsw.gui.ObjectiveCardImage;
import it.polimi.ingsw.gui.dataStorage.GameCardImageFactory;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class PointsPanelController {

    @FXML
    private ImageView medalIcon1;
    @FXML
    private ImageView medalIcon2;

    @FXML
    private ListView<ObjectiveCard> objectivesList;

    private void loadDummyData() {
        Parser parser = new Parser();
        Deck<ObjectiveCard> objectiveDeck = parser.getObjectiveDeck();
        objectivesList.getItems().addAll(objectiveDeck.draw(), objectiveDeck.draw(), objectiveDeck.draw());
    }

    @FXML
    public void initialize() {
        // Set the images for the icons
        String iconPath = "/assets/images/medalIcon.png";
        Image medalImage = new Image(getClass().getResourceAsStream(iconPath));

        medalIcon1.setImage(medalImage);
        medalIcon2.setImage(medalImage);

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
        // Load the data into the ListView
        loadDummyData();

        // Adjust the height of the ListView
        adjustListViewHeight(objectivesList);
    }

    private void adjustListViewHeight(ListView<?> listView) {
        double cellHeight = GameCardImageFactory.getHeightFromWidth(150) + 20;
        listView.setPrefHeight(Region.USE_COMPUTED_SIZE);
        listView.minHeightProperty().bind(Bindings.size(listView.getItems()).multiply(cellHeight).add(25));
        listView.maxHeightProperty().bind(Bindings.size(listView.getItems()).multiply(cellHeight).add(25));
    }

    @FXML
    private void handlePlayerClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        String playerName = clickedLabel.getText();
        // Logica per reindirizzare alla vista del giocatore
        System.out.println("Clicked on player: " + playerName);
    }
}