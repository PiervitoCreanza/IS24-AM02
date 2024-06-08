package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;

public class GuiGameCard {
    private static final double cardWidth = 234;
    private static final double cardRatio = 1.5;
    //NodeDataBinding<GameCard> boundDataElement;
    private final URL front;
    private final URL back;
    GameCard gameCard;
    private URL currentSide;

    public GuiGameCard(GameCard gameCard, Pane boundNode) {
        this.gameCard = gameCard;

        int cardId = gameCard.getCardId();
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

        //boundDataElement = new NodeDataBinding<>(boundNode, (card) -> createImageView());
    }

    private ImageView createImageView() {
        Image image = new Image(currentSide.toString(), true);
        ImageView imageView = new ImageView(image);
        imageView.preserveRatioProperty().set(true);
        imageView.setFitWidth(cardWidth);
        imageView.getStyleClass().add("card");
        imageView.setUserData(gameCard);
        return imageView;
    }

    private void updateCurrentSide() {
        if (gameCard.isFlipped()) {
            this.currentSide = this.back;
        } else {
            this.currentSide = this.front;
        }
        //boundDataElement.forceUpdate();
    }

    public void switchSide() {
        gameCard.switchSide();
        updateCurrentSide();
    }

}
