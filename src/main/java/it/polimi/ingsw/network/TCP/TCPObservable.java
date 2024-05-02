package it.polimi.ingsw.network.TCP;

/**
 * The TCPObservable interface defines the methods that a TCP observable must implement.
 * A TCP observable is responsible for managing its observers.
 */
public interface TCPObservable {
    /**
     * Adds an observer to the observable.
     *
     * @param observer the observer to be added
     */
    void addObserver(TCPObserver observer);

    /**
     * Removes an observer from the observable.
     *
     * @param observer the observer to be removed
     */
    void removeObserver(TCPObserver observer);
}