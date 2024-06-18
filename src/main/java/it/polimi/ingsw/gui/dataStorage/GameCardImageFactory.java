package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * This class is responsible for creating images for game cards.
 * It provides methods to create both front and back images of a game card.
 */
public class GameCardImageFactory {

    private static final double cardWidth = 234;
    private static final double cardRatio = 1.5;

    /**
     * Creates an image for a game card.
     * If the card is flipped, it creates a back image.
     * Otherwise, it creates a front image.
     *
     * @param card The game card for which the image is to be created.
     * @return The created image.
     * @throws IllegalArgumentException If the card is null.
     */
    public static Image createGameCardImage(GameCard card) {

        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        if (card.isFlipped()) {
            return createBackImage(card.getCardId());
        } else {
            return createFrontImage(card.getCardId());
        }
    }

    public static ImageView createGameCardImageView(GameCard card) {
        Image cardImage = GameCardImageFactory.createGameCardImage(card);
        return createGameCardImageViewFromImage(card, cardImage);
    }

    public static ImageView createGameCardImageViewFromImage(GameCard card, Image cardImage) {
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setFitWidth(234);
        cardImageView.setFitHeight(156);
        cardImageView.setPreserveRatio(true);
        cardImageView.setUserData(card);
        cardImageView.getStyleClass().add("card");

        return cardImageView;
    }

    /**
     * Creates a front image for a game card.
     *
     * @param cardId The ID of the game card for which the front image is to be created.
     * @return The created front image.
     * @throws IllegalArgumentException If the card image is not found.
     */
    public static Image createFrontImage(int cardId) {
        String paddedCardId = String.format("%03d", cardId);
        URL frontUrl = GameCardImageFactory.class.getResource("/" + paddedCardId + "_front.png");
        if (frontUrl == null) {
            throw new IllegalArgumentException("Card image not found");
        }
        return new Image(frontUrl.toExternalForm());
    }

    /**
     * Creates a back image for a game card.
     *
     * @param cardId The ID of the game card for which the back image is to be created.
     * @return The created back image.
     * @throws IllegalArgumentException If the card image is not found or the card ID is invalid.
     */
    public static Image createBackImage(int cardId) {
        String paddedCardId;

        // Determine the file name for the back image based on the cardId.
        if (cardId <= 10) {
            paddedCardId = "fungi";
        } else if (cardId <= 20) {
            paddedCardId = "plant";
        } else if (cardId <= 30) {
            paddedCardId = "animal";
        } else if (cardId <= 40) {
            paddedCardId = "insect";
        } else if (cardId <= 50) {
            paddedCardId = "fungi_gold";
        } else if (cardId <= 60) {
            paddedCardId = "plant_gold";
        } else if (cardId <= 70) {
            paddedCardId = "animal_gold";
        } else if (cardId <= 80) {
            paddedCardId = "insect_gold";
        } else if (cardId >= 87) {
            paddedCardId = "generic";
        } else {
            paddedCardId = String.format("%03d", cardId);
        }
        String filename = "/" + paddedCardId + "_back.png";
        URL backUrl = GameCardImageFactory.class.getResource(filename);
        if (backUrl == null) {
            throw new IllegalArgumentException("Card image " + filename + " not found");
        }
        return new Image(backUrl.toExternalForm());
    }

    /**
     * This method returns the height of the card image.
     *
     * @param width The width of the card image.
     * @return The height of the card image.
     */
    public static double getHeightFromWidth(double width) {
        return width / cardRatio;
    }

    public static double getDefaultCardWidth() {
        return cardWidth;
    }

    public static double getDefaultCardHeight() {
        return cardWidth / cardRatio;
    }
}