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
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setFitWidth(234);
        cardImageView.setFitHeight(156);
        cardImageView.setPreserveRatio(true);
        cardImageView.setUserData(card);
        return cardImageView;
    }

    /**
     * Creates a front image for a game card.
     *
     * @param cardId The ID of the game card for which the front image is to be created.
     * @return The created front image.
     * @throws IllegalArgumentException If the card image is not found.
     */
    private static Image createFrontImage(int cardId) {
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
    private static Image createBackImage(int cardId) {
        String paddedCardId;

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
        } else {
            paddedCardId = String.format("%03d", cardId);
        }

        URL backUrl = GameCardImageFactory.class.getResource("/" + paddedCardId + "_back.png");
        if (backUrl == null) {
            throw new IllegalArgumentException("Card image not found");
        }
        return new Image(backUrl.toExternalForm());
    }
}