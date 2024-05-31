package it.polimi.ingsw.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * This class represents an image of a card in the game.
 * It contains the URLs of the front and back images of the card.
 */
public class GameCardImage {
    private final URL front;
    private final URL back;
    private final int cardId;

    /**
     * This method loads the images for the card from resources.
     * The cardId is padded to 3 digits and used to construct the resource path.
     * If the front image is not found, an IllegalArgumentException is thrown.
     *
     * @param cardId The id of the card to load images for.
     * @throws IllegalArgumentException If the front image is not found.
     */
    public GameCardImage(int cardId) {
        this.cardId = cardId;
        String paddedCardId = String.format("%03d", cardId);
        this.front = getClass().getResource("/" + paddedCardId + "_front.png");
        this.back = getClass().getResource("/" + paddedCardId + "_back.png");

        if (this.front == null) {
            throw new IllegalArgumentException("Card image not found");
        }
    }

    private ImageView createImageView(URL imageUrl) {
        Image image = new Image(imageUrl.toString(), true);
        ImageView imageView = new ImageView(image);
        imageView.preserveRatioProperty().set(true);
        imageView.fitWidthProperty().set(234);
        imageView.getStyleClass().add("card");
        return imageView;
    }

    /**
     * This method returns an ImageView of the front of the card.
     * The image is loaded from the URL stored in the front field.
     *
     * @return An ImageView of the front of the card.
     */
    public ImageView getFront() {
        return createImageView(this.front);
    }

    /**
     * This method returns an ImageView of the back of the card.
     * The image is loaded from the URL stored in the back field.
     *
     * @return An ImageView of the back of the card.
     */
    public ImageView getBack() {
        return createImageView(this.back);
    }

    /**
     * This method returns the cardId associated with the image.
     *
     * @return The cardId associated with the image.
     */
    public int getCardId() {
        return cardId;
    }
}