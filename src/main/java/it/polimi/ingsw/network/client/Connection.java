package it.polimi.ingsw.network.client;

import java.beans.PropertyChangeListener;

public interface Connection {

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void connect();

    void quit();

}
