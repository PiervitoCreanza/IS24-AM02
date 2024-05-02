package it.polimi.ingsw.network.server.TCP;

public interface TCPObservable {
    void addObserver(TCPObserver observer);

    void removeObserver(TCPObserver observer);
}
