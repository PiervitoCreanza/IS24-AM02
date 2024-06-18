package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ObjectiveCardImage {
    private final ObjectiveCard objectiveCard;
    private final java.net.URL front;

    public ObjectiveCardImage(ObjectiveCard objectiveCard) {
        this.objectiveCard = objectiveCard;
        int cardId = objectiveCard.getCardId();
        String paddedCardId = String.format("%03d", cardId);
        this.front = getClass().getResource("/" + paddedCardId + "_front.png");


    }

    public ImageView getImageView() {
        Image image = new Image(front.toString(), true);
        ImageView imageView = new ImageView(image);
        imageView.preserveRatioProperty().set(true);
        imageView.setFitWidth(350);
        imageView.getStyleClass().add("card");
        return imageView;
    }

    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }

    public int getCardId() {
        return objectiveCard.getCardId();
    }


}
