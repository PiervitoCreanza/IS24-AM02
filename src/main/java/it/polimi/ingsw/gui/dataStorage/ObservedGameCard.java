package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.gui.components.GameCardImageFactory;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.function.Consumer;

public class ObservedGameCard {
    private final ObjectProperty<GameCard> gameCardProperty = new SimpleObjectProperty<>();
    private final BooleanProperty isFlippedProperty = new SimpleBooleanProperty();
    private final ImageView boundImageView;
    private Consumer<ImageView> imageViewConsumer;
    private final ChangeListener<Boolean> onIsFlippedUpdate = (observable, oldValue, newValue) -> {
        // This code will be executed when the isFlippedProperty changes
        updateGameCardImage(gameCardProperty.get());
    };

    public ObservedGameCard(ImageView boundImageView) {
        this.boundImageView = Objects.requireNonNull(boundImageView, "boundImageView cannot be null.");
        // When the card is changed, bind the isFlippedProperty to the new card's isFlippedProperty
        this.gameCardProperty.addListener((observable, oldCard, newCard) -> {
            // Update the displayed image
            updateGameCardImage(newCard);
            if (newCard != null) {

                isFlippedProperty.bind(newCard.getIsFlippedProperty());
                // When the isFlippedProperty changes, execute the internalPropertyChangeListener
                isFlippedProperty.addListener(onIsFlippedUpdate);

                // Set the new card in the user data
                this.boundImageView.setUserData(newCard);

                // Call the consumer if present
                if (imageViewConsumer != null) {
                    imageViewConsumer.accept(boundImageView);
                }
            }
        });
    }

    public ObservedGameCard(ImageView boundImageView, Consumer<ImageView> imageViewConsumer) {
        this(null, boundImageView);
        this.imageViewConsumer = imageViewConsumer;
    }

    public ObservedGameCard(GameCard gameCard, ImageView boundImageView) {
        this(boundImageView);
        this.gameCardProperty.set(gameCard);
    }

    private void updateGameCardImage(GameCard gameCard) {
        if (gameCard == null) {
            boundImageView.setImage(null);
            return;
        }
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

    public ImageView getBoundImageView() {
        return boundImageView;
    }

    public ObjectProperty<GameCard> gameCardProperty() {
        return gameCardProperty;
    }
}