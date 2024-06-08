package it.polimi.ingsw.gui.dataStorage;

import it.polimi.ingsw.gui.GameCardImage;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ObservarblePlayerBoard extends HashMap<MultiSystemCoordinate, GameCardImage> implements ObservableDataStorage {


    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ObservarblePlayerBoard() {
        super();
    }

    private void notifyValueChange(String eventName, Object oldValue, Object newValue, MultiSystemCoordinate coordinate) {
        // If the value was changed, fire a property change event.
        if (oldValue == null || !oldValue.equals(newValue)) {
            pcs.firePropertyChange(eventName, null, coordinate);
        }
    }

    /**
     * Converts the key to a new key with the offset of (50, 50) and puts the value in the new key.
     *
     * @param key   The key to convert.
     * @param image The GameCardImage to put.
     * @return The previous value associated with the key, or null if there was no mapping for the key.
     */
    public GameCardImage putFromGUI(MultiSystemCoordinate key, GameCardImage image) {
        GameCardImage oldImage = super.put(key, image);
        notifyValueChange("putFromGUI", oldImage, image, key);
        return oldImage;
    }

    public GameCardImage putFromNetwork(MultiSystemCoordinate coordinate, GameCardImage image) {
        GameCardImage oldImage = super.put(coordinate, image);
        notifyValueChange("putFromNetwork", oldImage, image, coordinate);
        return oldImage;
    }

    public void loadData(HashMap<Coordinate, GameCard> data) {
        // Add all the new cards to the board
        for (Coordinate key : data.keySet()) {
            MultiSystemCoordinate multiSystemCoordinate = new MultiSystemCoordinate().setCoordinateInModelSystem(key);
            // If the key is already present, skip it (Cards cannot be changed)
            if (get(multiSystemCoordinate) != null) {
                continue;
            }

            putFromNetwork(multiSystemCoordinate, new GameCardImage(data.get(key)));
        }

        // Remove all the cards that are not present in the new data. This is to prevent the local board from having cards that are not present in the model.
        Set<MultiSystemCoordinate> keys = new HashSet<>(super.keySet());
        for (MultiSystemCoordinate key : keys) {
            if (!data.containsKey(key.getCoordinateInModelSystem())) {
                remove(key);
                pcs.firePropertyChange("remove", null, key);
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
