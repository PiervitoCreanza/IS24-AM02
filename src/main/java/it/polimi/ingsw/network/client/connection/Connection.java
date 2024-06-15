package it.polimi.ingsw.network.client.connection;

import it.polimi.ingsw.utils.PropertyChangeNotifier;

/**
 * The Connection interface represents a network connection in the application.
 * It extends the PropertyChangeNotifier interface to provide property change support.
 */
public interface Connection extends PropertyChangeNotifier {

    /**
     * Establishes a connection.
     */
    void connect();

    /**
     * Closes the program when it's impossible to establish a connection.
     */
    void quit();
}
