package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * This class represents an image of a card in the game.
 * It contains the URLs of the front and back images of the card.
 */
public class GameCardImage {
    private final ImageView front;
    private final ImageView back;
    private static final double cardWidth = 234;
    private final int cardId;
    private final GameCard gameCard;
    private static final double cardRatio = 1.5;

    /**
     * This method loads the images for the card from resources.
     * The cardId is padded to 3 digits and used to construct the resource path.
     * If the front image is not found, an IllegalArgumentException is thrown.
     *
     * @param gameCard The card for which to load the images.
     * @throws IllegalArgumentException If the front image is not found.
     */
    public GameCardImage(GameCard gameCard) {
        this.gameCard = gameCard;
        this.cardId = gameCard.getCardId();
        String paddedCardId = String.format("%03d", cardId);
        URL frontUrl = getClass().getResource("/" + paddedCardId + "_front.png");
        if (frontUrl == null) {
            throw new IllegalArgumentException("Card image not found");
        }
        this.front = createImageView(frontUrl);

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

        URL backUrl = getClass().getResource("/" + paddedCardId + "_back.png");

        if (backUrl != null) {
            this.back = createImageView(backUrl);
        } else {
            this.back = null;
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
        gameCard.setFlipped(false);
        return front;
    }

    /**
     * This method returns an ImageView of the back of the card.
     * The image is loaded from the URL stored in the back field.
     *
     * @return An ImageView of the back of the card.
     */
    public ImageView getBack() {
        gameCard.setFlipped(true);
        return back;
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
        gameCard.switchSide();
        return getCurrentSide();
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
        if (gameCard.isFlipped()) {
            return back;
        }
        return front;
    }

    /**
     * This method returns whether the card is flipped.
     *
     * @return Whether the card is flipped.
     */
    public boolean isFlipped() {
        return gameCard.isFlipped();
    }

    /**
     * This method returns the game card associated with the image.
     *
     * @return The game card associated with the image.
     */
    public GameCard getGameCard() {
        return gameCard;
    }
}