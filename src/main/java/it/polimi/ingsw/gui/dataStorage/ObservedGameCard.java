package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.gui.components.GuiCardFactory;
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

/**
 * This class represents an observed game card.
 * It maintains a reference to a game card and an image view, and provides methods to update and retrieve the game card.
 */
public class ObservedGameCard {
    /**
     * The game card property.
     */
    private final ObjectProperty<GameCard> gameCardProperty = new SimpleObjectProperty<>();

    /**
     * The property indicating whether the card is flipped.
     */
    private final BooleanProperty isFlippedProperty = new SimpleBooleanProperty();

    /**
     * The image view bound to this observed game card.
     */
    private final ImageView boundImageView;
    /**
     * The listener to be called when the isFlippedProperty changes.
     */
    private final ChangeListener<Boolean> onIsFlippedUpdate = (observable, oldValue, newValue) -> {
        // This code will be executed when the isFlippedProperty changes
        updateGameCardImage(gameCardProperty.get());
    };
    /**
     * The consumer to be called when the image view is updated.
     */
    private Consumer<ImageView> imageViewConsumer;

    /**
     * Constructs a new ObservedGameCard with the specified image view.
     *
     * @param boundImageView the image view to be bound to this observed game card
     */
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

    /**
     * Constructs a new ObservedGameCard with the specified image view and consumer.
     *
     * @param boundImageView    the image view to be bound to this observed game card
     * @param imageViewConsumer the consumer to be called when the image view is updated
     */
    public ObservedGameCard(ImageView boundImageView, Consumer<ImageView> imageViewConsumer) {
        this(null, boundImageView);
        this.imageViewConsumer = imageViewConsumer;
    }

    /**
     * Constructs a new ObservedGameCard with the specified game card and image view.
     *
     * @param gameCard       the game card to be observed
     * @param boundImageView the image view to be bound to this observed game card
     */
    public ObservedGameCard(GameCard gameCard, ImageView boundImageView) {
        this(boundImageView);
        this.gameCardProperty.set(gameCard);
    }

    /**
     * Updates the image of the game card.
     *
     * @param gameCard the game card whose image is to be updated
     */
    private void updateGameCardImage(GameCard gameCard) {
        if (gameCard == null) {
            boundImageView.setImage(null);
            return;
        }
        Image cardImage = GuiCardFactory.createImage(gameCard);
        boundImageView.setImage(cardImage);
    }

    /**
     * Retrieves the game card.
     *
     * @return the game card
     */
    public GameCard getGameCard() {
        return gameCardProperty.get();
    }

    /**
     * Sets the game card.
     *
     * @param gameCard the game card to be set
     */
    public void setGameCard(GameCard gameCard) {
        gameCardProperty.set(gameCard);
    }

    /**
     * Removes the game card.
     */
    public void removeGameCard() {
        gameCardProperty.set(null);
    }

    /**
     * Retrieves the image view bound to this observed game card.
     *
     * @return the image view bound to this observed game card
     */
    public ImageView getBoundImageView() {
        return boundImageView;
    }

    /**
     * Retrieves the game card property.
     *
     * @return the game card property
     */
    public ObjectProperty<GameCard> gameCardProperty() {
        return gameCardProperty;
    }
}