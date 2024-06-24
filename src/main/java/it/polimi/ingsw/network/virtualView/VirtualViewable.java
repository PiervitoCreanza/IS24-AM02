package it.polimi.ingsw.network.virtualView;

/**
 * The VirtualViewable interface represents an object that can provide a virtual view.
 * This interface is a part of the network server's virtual view system.
 *
 * @param <T> the type of the virtual view that this object can provide
 */
public interface VirtualViewable<T> {
    /**
     * Gets the virtual view provided by this object.
     *
     * @return the virtual view of type T
     */
    T getVirtualView();
}