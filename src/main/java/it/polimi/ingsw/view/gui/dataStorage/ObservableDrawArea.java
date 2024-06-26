package it.polimi.ingsw.view.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an observable draw area in the game.
 */
public class ObservableDrawArea {
    /**
     * The observed game cards group.
     */
    private final ObservedGameCardsGroup observedGameCardsGroup;
    /**
     * The property for the drawing phase.
     */
    private final SimpleBooleanProperty isDrawingPhase;
    /**
     * The client network controller mapper.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * Constructor for the ObservableDrawArea class.
     * It initializes the property change listeners for each card in the draw area.
     *
     * @param drawPane the pane containing the draw area.
     * @param isDrawingPhase the property for the drawing phase.
     */
    public ObservableDrawArea(Pane drawPane, SimpleBooleanProperty isDrawingPhase) {
        this.isDrawingPhase = isDrawingPhase;
        this.clientNetworkControllerMapper = ClientNetworkControllerMapper.getInstance();
        this.observedGameCardsGroup = new ObservedGameCardsGroup(drawPane);
        this.observedGameCardsGroup.addCard("firstGoldCard");
        this.observedGameCardsGroup.addCard("firstResourceCard");
        this.observedGameCardsGroup.addCard("firstGoldFieldCard");
        this.observedGameCardsGroup.addCard("secondGoldFieldCard");
        this.observedGameCardsGroup.addCard("firstResourceFieldCard");
        this.observedGameCardsGroup.addCard("secondResourceFieldCard");


        observedGameCardsGroup.getCardNames().forEach(cardName -> {
            ObservedGameCard observedGameCard = observedGameCardsGroup.getCard(cardName);
            observedGameCard.getBoundImageView().setOnMouseClicked(event -> {
                if (updateDrawStatus()) {
                    switch (cardName) {
                        case "firstGoldCard":
                            clientNetworkControllerMapper.drawCardFromGoldDeck();
                            break;
                        case "firstResourceCard":
                            clientNetworkControllerMapper.drawCardFromResourceDeck();
                            break;
                        default:
                            clientNetworkControllerMapper.drawCardFromField(observedGameCard.getGameCard().getCardId());
                            break;
                    }
                }
            });
        });


        // Add a listener to the isDrawingPhase property to change the cursor of the cards
        isDrawingPhase.addListener((observable, oldValue, newValue) -> {
            observedGameCardsGroup.getCards().forEach(card -> card.getBoundImageView().setCursor(newValue ? Cursor.HAND : Cursor.DEFAULT));
        });
    }

    private boolean updateDrawStatus() {
        // If the draw phase is active, deactivate it
        if (isDrawingPhase.get()) {
            isDrawingPhase.set(false);
            // Return true to indicate that the draw phase was active
            return true;
        }
        // Return false to indicate that the draw phase is not active
        return false;
    }

    /**
     * Loads data into the draw area from the global board view.
     *
     * @param globalBoardView the global board view from which to load data
     */
    public void loadData(GlobalBoardView globalBoardView) {
        globalBoardView.goldFirstCard().setFlipped(true);
        observedGameCardsGroup.getCard("firstGoldCard").setGameCard(setBackSide(globalBoardView.goldFirstCard()));
        observedGameCardsGroup.getCard("firstResourceCard").setGameCard(setBackSide(globalBoardView.resourceFirstCard()));

        ArrayList<GameCard> goldFieldCards = globalBoardView.fieldGoldCards();
        observedGameCardsGroup.getCard("firstGoldFieldCard").setGameCard(getCardWithoutIllegalBounds(goldFieldCards, 0));
        observedGameCardsGroup.getCard("secondGoldFieldCard").setGameCard(getCardWithoutIllegalBounds(goldFieldCards, 1));

        ArrayList<GameCard> resourceFieldCards = globalBoardView.fieldResourceCards();
        observedGameCardsGroup.getCard("firstResourceFieldCard").setGameCard(getCardWithoutIllegalBounds(resourceFieldCards, 0));
        observedGameCardsGroup.getCard("secondResourceFieldCard").setGameCard(getCardWithoutIllegalBounds(resourceFieldCards, 1));
    }

    private GameCard setBackSide(GameCard card) {
        if (card != null) {
            card.setFlipped(true);
        }
        return card;
    }

    private GameCard getCardWithoutIllegalBounds(List<GameCard> cards, int index) {
        if (index < 0 || index >= cards.size()) {
            return null;
        }
        return cards.get(index);
    }
}