package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The Connection interface represents a network connection in the application.
 * It extends the PropertyChangeNotifier interface to provide property change support.
 */
public abstract class Connection implements PropertyChangeNotifier, PropertyChangeListener {

    protected int attempts = 1;

    protected int maxAttempts = 5;

    protected long waitTime = 5000;

    protected ClientNetworkControllerMapper networkControllerMapper;

    protected String serverIp;

    protected int serverPort;

    protected PropertyChangeSupport listeners;

    protected Logger logger;

    public Connection(ClientNetworkControllerMapper networkControllerMapper, String serverIp, int serverPort) {
        this.networkControllerMapper = networkControllerMapper;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.listeners = new PropertyChangeSupport(this);
    }

    /**
     * Establishes a connection.
     */
    public abstract void connect();

    /**
     * Closes the program when it's impossible to establish a connection.
     */
    protected void quit() {
        System.exit(-1);
    }

    /**
     * This method is called when a property change event is fired.
     * It is responsible for handling the property change event.
     *
     * @param evt The property change event that was fired
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        if (changedProperty.equals("CONNECTION_CLOSED")) {
            connect();
        } else {
            logger.warn("Unknown property change event: {}", changedProperty);
        }
    }

    /**
     * Adds a PropertyChangeListener to the listeners list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listeners list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}
