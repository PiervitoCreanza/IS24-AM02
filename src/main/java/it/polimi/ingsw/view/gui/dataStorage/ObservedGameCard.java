package it.polimi.ingsw.view.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.view.gui.components.GuiCardFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.function.Consumer;

/**card.
 * It contains the logic for handling changes in the game card and
 *  * Class representing an observed game  rendering them in the GUI.
 */
public class ObservedGameCard {
    /**
     * Property representing the game card.
     * It is used to bind the game card to the ImageView.
     */
    private final ObjectProperty<GameCard> gameCardProperty = new SimpleObjectProperty<>();

    /**
     * Property representing whether the card is flipped.
     * It is used to bind the isFlippedProperty of the game card to the ImageView.
     */
    private final BooleanProperty isFlippedProperty = new SimpleBooleanProperty();

    /**
     * ImageView representing the game card.
     */
    private final ImageView boundImageView;

    /**
     * Consumer for the ImageView. It is called when the ImageView is updated.
     * This is useful for updating other components when the ImageView is updated.
     */
    private Consumer<ImageView> imageViewConsumer;


    /**
     * Constructor for the ObservedGameCard class.
     * It initializes the boundImageView and sets up listeners for changes in the gameCardProperty and isFlippedProperty.
     *
     * @param boundImageView the ImageView representing the game card
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
                isFlippedProperty.addListener((observableProperty, oldValue, newValue) -> {
                    // This code will be executed when the isFlippedProperty changes
                    updateGameCardImage(gameCardProperty.get());
                });

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
     * Constructor for the ObservedGameCard class.
     * It initializes the boundImageView and imageViewConsumer.
     *
     * @param boundImageView    the ImageView representing the game card
     * @param imageViewConsumer the Consumer for the ImageView
     */
    public ObservedGameCard(ImageView boundImageView, Consumer<ImageView> imageViewConsumer) {
        this(null, boundImageView);
        this.imageViewConsumer = imageViewConsumer;
    }

    /**
     * Constructor for the ObservedGameCard class.
     * It initializes the boundImageView and gameCardProperty.
     *
     * @param gameCard       the game card
     * @param boundImageView the ImageView representing the game card
     */
    public ObservedGameCard(GameCard gameCard, ImageView boundImageView) {
        this(boundImageView);
        this.gameCardProperty.set(gameCard);
    }

    /**
     * Method to update the image of the game card.
     *
     * @param gameCard the game card
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
     * Method to get the game card.
     *
     * @return the game card
     */
    public GameCard getGameCard() {
        return gameCardProperty.get();
    }

    /**
     * Method to set the game card.
     *
     * @param gameCard the game card
     */
    public void setGameCard(GameCard gameCard) {
        gameCardProperty.set(gameCard);
    }

    /**
     * Method to remove the game card.
     */
    public void removeGameCard() {
        gameCardProperty.set(null);
    }

    /**
     * Method to get the ImageView representing the game card.
     *
     * @return the ImageView representing the game card
     */
    public ImageView getBoundImageView() {
        return boundImageView;
    }
}