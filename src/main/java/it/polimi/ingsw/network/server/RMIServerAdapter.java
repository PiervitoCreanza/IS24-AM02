package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.TCP.Observable;
import it.polimi.ingsw.network.TCP.Observer;
import it.polimi.ingsw.network.client.ServerActions;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class RMIServerAdapter implements ServerMessageHandler, Observable<ServerMessageHandler> {
    private final ServerActions stub;

    private String playerName;

    private String gameName;

    private final HashSet<Observer<ServerMessageHandler>> observers = new HashSet<>();

    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    public RMIServerAdapter(ServerActions stub) {
        this.stub = stub;
    }
    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerMessage message) {
        ServerActionEnum serverAction = message.getServerAction();
        try {
            switch (serverAction) {
                case UPDATE_VIEW -> stub.receiveUpdatedView(message.getView());
                case DELETE_GAME -> stub.receiveGameDeleted(message.getSuccessDeleteMessage());
                case GET_GAMES -> stub.receiveGameList(message.getGames());
                case ERROR_MSG -> stub.receiveErrorMessage(message.getErrorMessage());
                default -> System.err.print("Invalid action\n");
            }
        } catch (RemoteException e) {
            if (isConnected.get())
                closeConnection();
        }

        // Debug
        System.out.println("RMI message sent: " + message);
    }

    /**
     * Gets the name of the player associated with the connection.
     *
     * @return the name of the player
     */
    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets the name of the player associated with the connection.
     *
     * @param playerName the name of the player
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the name of the game associated with the connection.
     *
     * @return the name of the game
     */
    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public void heartbeat() {
        this.isConnected.set(true);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isConnected.get()) {
                    cancel();
                    return;
                }
                try {
                    stub.heartbeat();
                } catch (RemoteException e) {
                    System.out.println("RMI Client disconnected - detected when pinging");
                    closeConnection();
                    cancel();
                }
            }
        }, 0, 2500);
    }

    /**
     * Sets the name of the game associated with the connection.
     *
     * @param gameName the name of the game
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Closes the connection to the client.
     */
    @Override
    public void closeConnection() {
        this.isConnected.set(false);
        synchronized (observers) {
            observers.forEach(observer -> observer.notify(this));
        }
    }

    @Override
    public void addObserver(Observer<ServerMessageHandler> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer<ServerMessageHandler> observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }
}