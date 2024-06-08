package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ObservedGameCard {
    private final ObjectProperty<GameCard> gameCardProperty = new SimpleObjectProperty<>();
    private final BooleanProperty isFlippedProperty = new SimpleBooleanProperty();
    private final ImageView boundImageView;
    private final ChangeListener<Boolean> onIsFlippedUpdate = (observable, oldValue, newValue) -> {
        // This code will be executed when the isFlippedProperty changes
        updateGameCardImage(gameCardProperty.get());
    };

    public ObservedGameCard(GameCard gameCard, ImageView boundImageView) {
        this.boundImageView = boundImageView;
        // When the card is changed, bind the isFlippedProperty to the new card's isFlippedProperty
        this.gameCardProperty.addListener((observable, oldCard, newCard) -> {
            if (newCard != null) {
                // Update the displayed image
                updateGameCardImage(newCard);
                isFlippedProperty.bind(newCard.getIsFlippedProperty());
                // When the isFlippedProperty changes, execute the internalPropertyChangeListener
                isFlippedProperty.addListener(onIsFlippedUpdate);
            }
        });
        this.gameCardProperty.set(gameCard);
    }

    private void updateGameCardImage(GameCard gameCard) {
        Image cardImage = GameCardImageFactory.createGameCardImage(gameCard);
        boundImageView.setImage(cardImage);
    }

    public GameCard getGameCard() {
        return gameCardProperty.get();
    }

    public void setGameCard(GameCard gameCard) {
        gameCardProperty.set(gameCard);
    }

    public void removeGameCard() {
        gameCardProperty.set(null);
    }

    public ObjectProperty<GameCard> gameCardProperty() {
        return gameCardProperty;
    }
}