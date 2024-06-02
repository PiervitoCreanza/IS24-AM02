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
    private static final double cardWidth = 234;
    private final int cardId;
    private static final double cardRatio = 1.5;
    private boolean isFront = true;

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

        // Determine the file name for the back image based on the cardId.
        if (cardId < 11) {
            paddedCardId = "001";
        } else if (cardId < 21) {
            paddedCardId = "011";
        } else if (cardId < 31) {
            paddedCardId = "021";
        } else if (cardId < 41) {
            paddedCardId = "031";
        } else if (cardId < 51) {
            paddedCardId = "041";
        } else if (cardId < 66) {
            paddedCardId = "051";
        } else if (cardId < 73) {
            paddedCardId = "066";
        } else if (cardId >= 88) {
            paddedCardId = "102";
        }

        this.back = getClass().getResource("/" + paddedCardId + "_back.png");

        if (this.front == null) {
            throw new IllegalArgumentException("Card image not found");
        }
    }

    /**
     * This method returns the width of the card image.
     *
     * @param width The width of the card image.
     * @return The width of the card image.
     */
    public static double getHeightFromWidth(double width) {
        return width / cardRatio;
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

    private ImageView createImageView(URL imageUrl) {
        Image image = new Image(imageUrl.toString(), true);
        ImageView imageView = new ImageView(image);
        imageView.preserveRatioProperty().set(true);
        imageView.setFitWidth(cardWidth);
        imageView.getStyleClass().add("card");
        return imageView;
    }

    /**
     * This method returns an ImageView of the front or back of the card, depending on the current side.
     * The image is loaded from the URL stored in the front or back field.
     *
     * @return An ImageView of the front or back of the card.
     */
    public ImageView switchSide() {
        if (isFront) {
            isFront = false;
            return createImageView(this.back);
        }
        isFront = true;
        return createImageView(this.front);
    }

    /**
     * This method returns the cardId associated with the image.
     *
     * @return The cardId associated with the image.
     */
    public int getCardId() {
        return cardId;
    }

    /**
     * This method returns an ImageView of the current side of the card.
     * The image is loaded from the URL stored in the front or back field, depending on the current side.
     *
     * @return An ImageView of the current side of the card.
     */
    public ImageView getCurrentSide() {
        if (isFront) {
            return createImageView(this.front);
        }
        return createImageView(this.back);
    }
}