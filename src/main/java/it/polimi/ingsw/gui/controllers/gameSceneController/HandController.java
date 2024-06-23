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

/**
 * This class is responsible for controlling the hand of cards in the game scene.
 * It handles the dragging and dropping of cards, updating the hand display and managing the list of cards.
 */
public class HandController {

    /**
     * The (logger) of the class.
     */
    private static final Logger logger = LogManager.getLogger(HandController.class);

    /**
     * The controller for the game scene.
     */
    private final GameSceneController sceneController;

    /**
     * The list of card cells in the hand.
     */
    private final ArrayList<ObservedGameCard> cardCells;

    /**
     * Constructor for the HandController class.
     * It initializes the scene controller and the list of card cells.
     * It also sets up the card cells in the hand pane.
     *
     * @param handPane            The pane where the hand of cards is displayed.
     * @param gameSceneController The controller for the game scene.
     */
    public HandController(Pane handPane, GameSceneController gameSceneController) {
        this.sceneController = gameSceneController;
        this.cardCells = new ArrayList<>();

        this.cardCells.add(new ObservedGameCard((ImageView) handPane.lookup("#firstCard"), this::makeCardDraggable));
        this.cardCells.add(new ObservedGameCard((ImageView) handPane.lookup("#secondCard"), this::makeCardDraggable));
        this.cardCells.add(new ObservedGameCard((ImageView) handPane.lookup("#thirdCard"), this::makeCardDraggable));


    }

    /**
     * Makes a card draggable.
     * It sets up the mouse event handlers for the card.
     *
     * @param gameCardImageView The image view of the card.
     */
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

    /**
     * Updates the hand of cards.
     * It updates the displayed cards based on the given list of cards.
     *
     * @param hand The list of cards in the hand.
     */
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

    /**
     * Gets the observed game card associated with the given game card.
     *
     * @param gameCard The game card.
     * @return The observed game card associated with the given game card.
     */
    private Optional<ObservedGameCard> getObservedGameCard(GameCard gameCard) {
        return cardCells.stream().filter(observedGameCard -> Objects.equals(observedGameCard.getGameCard(), gameCard)).findFirst();
    }

}