package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The Connection interface represents a network connection in the application.
 * It extends the PropertyChangeNotifier interface to provide property change support.
 */
public abstract class Connection implements PropertyChangeNotifier, PropertyChangeListener {

    protected int attempts = 1;

    protected int maxAttempts = 5;

    private final ExecutorService executor;

    protected ClientNetworkControllerMapper networkControllerMapper;

    protected String serverIp;

    protected int serverPort;

    protected PropertyChangeSupport listeners;
    protected long waitTime = 10000;
    private Future<?> currentTask;

    protected Logger logger;

    public Connection(ClientNetworkControllerMapper networkControllerMapper, String serverIp, int serverPort) {
        this.networkControllerMapper = networkControllerMapper;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.listeners = new PropertyChangeSupport(this);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Establishes a connection.
     */
    protected abstract void connectionSetUp();

    public void connect() {
        attempts = 1;
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
        currentTask = executor.submit(this::connectionSetUp);
    }

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
