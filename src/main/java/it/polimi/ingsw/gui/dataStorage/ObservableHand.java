package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.gui.GameCardImage;
import it.polimi.ingsw.model.card.gameCard.GameCard;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

public class ObservableHand {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private final HashMap<Integer, GameCardImage> hand;

    public ObservableHand() {
        this.hand = new HashMap<>(3);
    }

    public void setCard(GameCard card, int index) {
        int oldCardId = -1;
        if (hand.get(index) != null) {
            oldCardId = hand.get(index).getCardId();
        }
        GameCardImage gameCardImage = new GameCardImage(card.getCardId());
        hand.put(index, gameCardImage);

        // If the card is different from the one already present, fire a property change event.
        if (card.getCardId() != oldCardId) {
            pcs.firePropertyChange("setCard", index, gameCardImage);
        }
    }

    public void removeCard(int index) {
        hand.put(index, null);
        pcs.firePropertyChange("removeCard", null, index);
    }

    public void loadData(ArrayList<GameCard> cards) {
        int i = 0;
        for (GameCard card : cards) {
            setCard(card, i++);
        }
    }

    public GameCardImage getCardImage(int index) {
        return hand.get(index);
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

}
