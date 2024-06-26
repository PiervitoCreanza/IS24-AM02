package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The Connection abstract class represents a network connection in the application.
 * It provides basic functionalities for establishing and maintaining a connection,
 * as well as handling property change events related to the connection state.
 *
 * <p>This class implements the {@link PropertyChangeNotifier} interface to support
 * property change events.
 *
 * <p>Subclasses must implement the {@link #connectionSetUp()} method to define the
 * specific connection setup logic.
 */
public abstract class Connection implements PropertyChangeNotifier, PropertyChangeListener {

    /**
     * Executor service for managing connection tasks.
     */
    private final ExecutorService executor;
    /**
     * Number of connection attempts made so far.
     */
    protected int attempts = 1;
    /**
     * Maximum number of attempts allowed to establish a connection.
     */
    protected int maxAttempts = 5;
    /**
     * Mapper for network controllers.
     */
    protected ClientNetworkControllerMapper networkControllerMapper;

    /**
     * Server IP address.
     */
    protected String serverIp;

    /**
     * Server port number.
     */
    protected int serverPort;

    /**
     * Support for property change listeners.
     */
    protected PropertyChangeSupport listeners;

    /**
     * Time to wait between connection attempts.
     */
    protected long waitTime = 10000;
    /**
     * Timer for managing connection attempts.
     */
    protected Timer connectionTrying;
    /**
     * Logger for connection-related messages.
     */
    protected Logger logger;
    /**
     * Current task for connection setup.
     */
    private Future<?> currentTask;

    /**
     * Constructs a Connection object with the specified parameters.
     *
     * @param networkControllerMapper The network controller mapper associated with this connection
     * @param serverIp                The IP address of the server to connect to
     * @param serverPort              The port number of the server to connect to
     */
    public Connection(ClientNetworkControllerMapper networkControllerMapper, String serverIp, int serverPort) {
        this.networkControllerMapper = networkControllerMapper;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.listeners = new PropertyChangeSupport(this);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Sets up the connection.
     * Subclasses must implement this method to define the specific connection setup logic.
     */
    protected abstract void connectionSetUp();

    /**
     * Initiates a connection attempt.
     */
    public void connect() {
        attempts = 1;
        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }
        currentTask = executor.submit(this::connectionSetUp);
    }

    /**
     * Terminates the program when establishing a connection becomes impossible.
     */
    protected void quit() {
        System.exit(-1);
    }

    /**
     * Handles property change events related to the connection state.
     *
     * @param evt The property change event fired
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        if (changedProperty.equals("CONNECTION_CLOSED")) {
            logger.warn("Trying to reconnect...");
            connect();
        } else {
            logger.warn("Unknown property change event: {}", changedProperty);
        }
    }

    /**
     * Adds a PropertyChangeListener to the list of listeners.
     *
     * @param listener The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the list of listeners.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}
