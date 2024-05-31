package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.virtualView.GlobalBoardView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * This class represents an observable draw area in the game.
 * It implements the ObservableDataStorage interface and provides methods to load data into the draw area,
 * and to add and remove property change listeners.
 */
public class ObservableDrawArea implements ObservableDataStorage {

    private final ObservableElement<GameCard> firstGoldCard = new ObservableElement<>();
    private final ObservableElement<GameCard> firstResourceCard = new ObservableElement<>();
    private final ObservableElement<GameCard> firstGoldFieldCard = new ObservableElement<>();
    private final ObservableElement<GameCard> secondGoldFieldCard = new ObservableElement<>();
    private final ObservableElement<GameCard> firstResourceFieldCard = new ObservableElement<>();
    private final ObservableElement<GameCard> secondResourceFieldCard = new ObservableElement<>();
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor for the ObservableDrawArea class.
     * It initializes the property change listeners for each card in the draw area.
     */
    public ObservableDrawArea() {
        // Add property change listeners to the cards to propagate the events to the listeners of this class
        this.firstGoldCard.addPropertyChangeListener(evt -> {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "firstGoldCard", evt.getOldValue(), evt.getNewValue()));
        });
        this.firstResourceCard.addPropertyChangeListener(evt -> {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "firstResourceCard", evt.getOldValue(), evt.getNewValue()));
        });
        this.firstGoldFieldCard.addPropertyChangeListener(evt -> {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "firstGoldFieldCard", evt.getOldValue(), evt.getNewValue()));
        });
        this.secondGoldFieldCard.addPropertyChangeListener(evt -> {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "secondGoldFieldCard", evt.getOldValue(), evt.getNewValue()));
        });
        this.firstResourceFieldCard.addPropertyChangeListener(evt -> {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "firstResourceFieldCard", evt.getOldValue(), evt.getNewValue()));
        });
        this.secondResourceFieldCard.addPropertyChangeListener(evt -> {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "secondResourceFieldCard", evt.getOldValue(), evt.getNewValue()));
        });
    }

    /**
     * Loads data into the draw area from the global board view.
     *
     * @param globalBoardView the global board view from which to load data
     */
    public void loadData(GlobalBoardView globalBoardView) {
        firstGoldCard.loadData(globalBoardView.goldFirstCard());
        firstResourceCard.loadData(globalBoardView.resourceFirstCard());

        ArrayList<GameCard> goldFieldCards = globalBoardView.fieldGoldCards();
        firstGoldFieldCard.loadData(!goldFieldCards.isEmpty() ? goldFieldCards.getFirst() : null);
        secondGoldFieldCard.loadData(goldFieldCards.size() > 1 ? goldFieldCards.get(1) : null);

        ArrayList<GameCard> resourceFieldCards = globalBoardView.fieldResourceCards();
        firstResourceFieldCard.loadData(!resourceFieldCards.isEmpty() ? resourceFieldCards.getFirst() : null);
        secondResourceFieldCard.loadData(resourceFieldCards.size() > 1 ? resourceFieldCards.get(1) : null);
    }

    /**
     * Adds a property change listener to this data storage.
     *
     * @param listener the listener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener from this data storage.
     *
     * @param listener the listener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}