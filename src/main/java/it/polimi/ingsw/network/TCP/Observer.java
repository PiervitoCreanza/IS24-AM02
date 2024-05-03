package it.polimi.ingsw.network.TCP;

/**
 * The Observer interface is used to notify objects of changes in the observable object.
 *
 * @param <T> The type of message that the observer will receive.
 */
public interface Observer<T> {
    /**
     * Notifies the observer of a received message.
     *
     * @param message The message that was received.
     */
    void notify(T message);
}
