package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * GuiCardFactory is a utility class that provides methods for creating Image and ImageView objects for cards.
 * It also provides methods for getting the default card width and height.
 */
public class GuiCardFactory {

    /**
     * The default width of a card.
     */
    private static final double cardWidth = 234;

    /**
     * The ratio of the width to the height of a card.
     */
    private static final double cardRatio = 1.5;

    /**
     * The default height of a card, calculated as the width divided by the ratio.
     */
    private static final double cardHeight = cardWidth / cardRatio;


    /**
     * Creates an Image representing the given Card.
     *
     * @param card The card linked to the image.
     * @return The created image.
     * @throws IllegalArgumentException If the card is null.
     */
    public static Image createImage(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        return createFrontImage(card.getCardId());
    }

    /**
     * Creates an Image representing the given GameCard.
     * If the card is flipped, it creates a back image.
     * Otherwise, it creates a front image.
     *
     * @param card The game card linked to the image.
     * @return The created image.
     * @throws IllegalArgumentException If the card is null.
     */
    public static Image createImage(GameCard card) {

        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        if (card.isFlipped()) {
            return createBackImage(card.getCardId());
        } else {
            return createFrontImage(card.getCardId());
        }
    }

    /**
     * Creates an ImageView representing the selected Card.
     *
     * @param card The card linked to the image.
     * @return The created ImageView.
     */
    public static ImageView createImageView(Card card) {
        Image cardImage = GuiCardFactory.createImage(card);
        return createImageView(card, cardImage);
    }

    /**
     * Creates an ImageView representing the selected GameCard.
     * If the card is flipped, it creates a back image.
     * Otherwise, it creates a front image.
     *
     * @param card The GameCard linked to the image.
     * @return The created ImageView.
     */
    public static ImageView createImageView(GameCard card) {
        Image cardImage = GuiCardFactory.createImage(card);
        return createImageView(card, cardImage);
    }

    /**
     * Creates an ImageView surrounding the provided cardImage and linked to the provided card.
     *
     * @param card      The card linked to the image.
     * @param cardImage The source image of the card.
     * @return The created ImageView.
     */
    public static ImageView createImageView(Card card, Image cardImage) {
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setFitWidth(cardWidth);
        cardImageView.setFitHeight(cardHeight);
        cardImageView.setPreserveRatio(true);
        cardImageView.setUserData(card);
        cardImageView.getStyleClass().add("card");
        return cardImageView;
    }

    /**
     * Creates a front image for a Card.
     *
     * @param cardId The ID of the card linked to the front image.
     * @return The created front image.
     * @throws IllegalArgumentException If the card image is not found.
     */
    public static Image createFrontImage(int cardId) {
        String paddedCardId = String.format("%03d", cardId);
        URL frontUrl = GuiCardFactory.class.getResource("/" + paddedCardId + "_front.png");
        if (frontUrl == null) {
            throw new IllegalArgumentException("Card image not found");
        }
        return new Image(frontUrl.toExternalForm());
    }

    /**
     * Creates a back image for a game card.
     *
     * @param cardId The ID of the card linked to the back image.
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
        URL backUrl = GuiCardFactory.class.getResource(filename);
        if (backUrl == null) {
            throw new IllegalArgumentException("Card image " + filename + " not found");
        }
        return new Image(backUrl.toExternalForm());
    }

    /**
     * This method returns the height of the card image given its width.
     *
     * @param width The width of the card image.
     * @return The height of the card image.
     */
    public static double getHeightFromWidth(double width) {
        return width / cardRatio;
    }

    /**
     * This method returns the default width of the card image.
     *
     * @return The width of the card image.
     */
    public static double getDefaultCardWidth() {
        return cardWidth;
    }

    /**
     * This method returns the default height of the card image.
     *
     * @return The height of the card image.
     */
    public static double getDefaultCardHeight() {
        return cardHeight;
    }
}
