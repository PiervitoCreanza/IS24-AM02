package it.polimi.ingsw.network.TCP;

/**
 * The Observable interface is used to notify observers of changes in the observable object.
 */
public interface Observable<T> {
    /**
     * Adds an observer to the observable.
     *
     * @param observer the observer to be added
     */
    void addObserver(Observer<T> observer);

    /**
     * Removes an observer from the observable.
     *
     * @param observer the observer to be removed
     */
    void removeObserver(Observer<T> observer);
}