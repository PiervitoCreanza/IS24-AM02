package it.polimi.ingsw.network.server.RMI;

import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.server.ServerMessageHandler;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class RMIServerSender implements ServerMessageHandler, Observable<ServerMessageHandler> {

    private final RMIServerToClientActions stub;

    private final HashSet<Observer<ServerMessageHandler>> observers = new HashSet<>();

    private String playerName;

    private String gameName;

    // This variable is used to check if the connection has been saved by the ServerNetworkControllerMapper.
    // The heartbeat will start only after the connection has been saved.
    // If an error occurs during the heartbeat, or while sending a message, this parameter will be set to false.
    // The closeConnection method will be called only one time and will notify the observers.
    private final AtomicBoolean isConnectionSaved = new AtomicBoolean(false);

    public RMIServerSender(RMIServerToClientActions stub) {
        this.stub = stub;
    }

    private void heartbeat() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isConnectionSaved.get()) {
                    cancel();
                    return;
                }
                try {
                    stub.heartbeat();
                } catch (RemoteException e) {
                    System.out.println("RMI Client: " + playerName + " disconnected. Detected when pinging.");
                    closeConnection();
                    cancel();
                }
            }
        }, 0, 2500);
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ServerToClientMessage message) {
        ServerActionEnum serverAction = message.getServerAction();
        try {
            switch (serverAction) {
                case UPDATE_VIEW -> stub.receiveUpdatedView(message.getView());
                case DELETE_GAME -> stub.receiveGameDeleted(message.getSuccessDeleteMessage());
                case GET_GAMES -> stub.receiveGameList(message.getGames());
                case ERROR_MSG -> stub.receiveErrorMessage(message.getErrorMessage());
                case CHAT_MSG -> stub.receiveChatMessage(message);
                default -> System.err.print("Invalid action\n");
            }
        } catch (RemoteException e) {
            System.out.println("RMI Client: " + playerName + " disconnected. Detected when sending message");
            closeConnection();
        }

        // Debug
        System.out.println("RMI message sent: " + message.getServerAction());
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

    /**
     * Sets the name of the game associated with the connection.
     *
     * @param gameName the name of the game
     */
    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void connectionSaved(boolean hasBeenSaved) {
        this.isConnectionSaved.set(hasBeenSaved);
        this.heartbeat(); //Comment here if you want to disable the heartbeat
    }

    /**
     * Closes the connection to the client.
     */
    @Override
    public void closeConnection() {
        if (isConnectionSaved.getAndSet(false)) {
            synchronized (observers) {
                observers.forEach(observer -> observer.notify(this));
            }
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
