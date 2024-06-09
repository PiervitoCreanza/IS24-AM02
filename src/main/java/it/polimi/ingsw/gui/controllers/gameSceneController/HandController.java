package it.polimi.ingsw.gui.controllers.gameSceneController;

import it.polimi.ingsw.gui.dataStorage.ObservedGameCard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class HandController {

    private static final Logger logger = LogManager.getLogger(HandController.class);

    private final GameSceneController sceneController;

    private final ArrayList<ObservedGameCard> cardCells;


    public HandController(Pane handPane, GameSceneController gameSceneController) {
        this.sceneController = gameSceneController;
        this.cardCells = new ArrayList<>();

        this.cardCells.add(new ObservedGameCard((ImageView) handPane.lookup("#firstCard"), this::makeCardDraggable));
        this.cardCells.add(new ObservedGameCard((ImageView) handPane.lookup("#secondCard"), this::makeCardDraggable));
        this.cardCells.add(new ObservedGameCard((ImageView) handPane.lookup("#thirdCard"), this::makeCardDraggable));


    }

    private void makeCardDraggable(ImageView gameCardImageView) {
        final Coordinate dragDelta = new Coordinate(0, 0);
        final Coordinate originalPosition = new Coordinate(0, 0);

        gameCardImageView.setOnMousePressed(mouseEvent -> {
            // Obtain the gameCardImage associated with the card
            GameCard boundGameCard = (GameCard) gameCardImageView.getUserData();

            // If the right mouse button is pressed, switch the card
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                logger.debug("Right mouse button pressed");
                // Switch the card side and update the image
                boundGameCard.switchSide();
                return;
            }

            originalPosition.setLocation(gameCardImageView.getLayoutX(), gameCardImageView.getLayoutY());
            dragDelta.setLocation(gameCardImageView.getLayoutX() - mouseEvent.getSceneX(), gameCardImageView.getLayoutY() - mouseEvent.getSceneY());
            gameCardImageView.setCursor(Cursor.MOVE);
        });

        gameCardImageView.setOnMouseReleased(mouseEvent -> {
            // If the right mouse button is released, do nothing
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                return;
            }

            // Obtain the hand position of the card
            Pane gameCardContainer = (Pane) gameCardImageView.getParent();

            boolean didIntersect = sceneController.handleCardDrop(gameCardImageView, gameCardContainer);


            if (didIntersect) {
                logger.debug("Card was dropped on a valid target");
                // Remove the card from the hand
                GameCard boundGameCard = (GameCard) gameCardImageView.getUserData();
                getObservedGameCard(boundGameCard).ifPresent(ObservedGameCard::removeGameCard);
                // Remove handlers
                gameCardImageView.setOnMouseReleased(null);
                gameCardImageView.setOnMousePressed(null);
                gameCardImageView.setOnMouseDragged(null);
                gameCardImageView.setOnMouseEntered(null);
            } else {
                logger.debug("Card was not dropped on a valid target");


            }
            // Reset the card position to put it back in the hand.
            gameCardImageView.setLayoutX(originalPosition.getX());
            gameCardImageView.setLayoutY(originalPosition.getY());
        });

        gameCardImageView.setOnMouseDragged(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                // The drag event can be triggered only by the right mouse button.
                return;
            }
            gameCardImageView.setLayoutX(mouseEvent.getSceneX() + dragDelta.getX());
            gameCardImageView.setLayoutY(mouseEvent.getSceneY() + dragDelta.getY());
        });
        gameCardImageView.setOnMouseEntered(mouseEvent -> {
            gameCardImageView.setCursor(Cursor.DEFAULT);
        });
    }


    public void update(ArrayList<GameCard> hand) {
        ArrayList<GameCard> updatedCards = new ArrayList<>(hand);
        ArrayList<ObservedGameCard> freeBoundNodes = new ArrayList<>(cardCells);

        for (ObservedGameCard boundDataElement : cardCells) {
            GameCard card = boundDataElement.getGameCard();
            // If the currently displayed card is still in the hand, the displayed card does not need to be updated.
            if (hand.contains(card)) {
                updatedCards.remove(card);
                freeBoundNodes.remove(boundDataElement);
            }
        }

        for (int i = 0; i < Math.min(freeBoundNodes.size(), updatedCards.size()); i++) {
            freeBoundNodes.get(i).setGameCard(updatedCards.get(i));
        }
    }

    private Optional<ObservedGameCard> getObservedGameCard(GameCard gameCard) {
        return cardCells.stream().filter(observedGameCard -> Objects.equals(observedGameCard.getGameCard(), gameCard)).findFirst();
    }

}
