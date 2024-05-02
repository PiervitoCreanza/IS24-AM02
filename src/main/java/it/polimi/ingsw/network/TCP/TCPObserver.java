package it.polimi.ingsw.network.TCP;

/**
 * The TCPObserver interface defines the methods that a TCP observer must implement.
 * A TCP observer is responsible for being notified when a message is received.
 */
public interface TCPObserver {
    /**
     * Notifies the observer of a received message.
     *
     * @param message the message that was received
     */
    void notify(String message);
}
