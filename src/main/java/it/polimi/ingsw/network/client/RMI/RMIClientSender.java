package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.ClientMessageHandler;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The RMIClientSender class is responsible for sending messages to the server.
 */
public class RMIClientSender implements ClientMessageHandler {
    /**
     * The RMIClientToServerActions instance that represents the server stub.
     * This stub is used to perform actions on the server.
     */
    private final RMIClientToServerActions serverStub;

    /**
     * The RMIServerToClientActions instance that represents the client stub.
     * This stub is used to perform actions on the client.
     */
    private final RMIServerToClientActions thisClientStub;

    /**
     * Listeners that will be notified when a message is received.
     */
    private final PropertyChangeSupport listeners;

    private Timer heartbeatTimer;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(RMIClientSender.class);

    /**
     * Constructor for the RMIClientSender class.
     * Initializes the server and client stubs.
     *
     * @param serverStub     The RMIClientToServerActions instance that represents the server stub.
     * @param thisClientStub The RMIServerToClientActions instance that represents the client stub.
     */
    public RMIClientSender(RMIClientToServerActions serverStub, RMIServerToClientActions thisClientStub) {
        this.serverStub = serverStub;
        this.thisClientStub = thisClientStub;
        this.listeners = new PropertyChangeSupport(this);
        heartbeat();
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    public void sendMessage(ClientToServerMessage message) {
        PlayerActionEnum playerAction = message.getPlayerAction();
        try {
            switch (playerAction) {
                case GET_GAMES -> serverStub.getGames(thisClientStub);
                case CREATE_GAME ->
                        serverStub.createGame(this.thisClientStub, message.getGameName(), message.getPlayerName(), message.getNPlayers());
                case DELETE_GAME ->
                        serverStub.deleteGame(thisClientStub, message.getGameName(), message.getPlayerName());
                case JOIN_GAME ->
                        serverStub.joinGame(this.thisClientStub, message.getGameName(), message.getPlayerName());
                case CHOOSE_PLAYER_COLOR ->
                        serverStub.choosePlayerColor(message.getGameName(), message.getPlayerName(), message.getPlayerColor());
                case SET_PLAYER_OBJECTIVE ->
                        serverStub.setPlayerObjective(message.getGameName(), message.getPlayerName(), message.getObjectiveCardId());
                case PLACE_CARD ->
                        serverStub.placeCard(message.getGameName(), message.getPlayerName(), message.getCoordinate(), message.getGameCardId(), message.isFlipped());
                case DRAW_CARD_FROM_FIELD ->
                        serverStub.drawCardFromField(message.getGameName(), message.getPlayerName(), message.getGameCard());
                case DRAW_CARD_FROM_RESOURCE_DECK ->
                        serverStub.drawCardFromResourceDeck(message.getGameName(), message.getPlayerName());
                case DRAW_CARD_FROM_GOLD_DECK ->
                        serverStub.drawCardFromGoldDeck(message.getGameName(), message.getPlayerName());
                case SWITCH_CARD_SIDE ->
                        serverStub.switchCardSide(message.getGameName(), message.getPlayerName(), message.getGameCardId());
                case SEND_CHAT_MSG ->
                        serverStub.chatMessageSender(message.getGameName(), message.getPlayerName(), message.getMessage(), message.getReceiver(), message.getTimestamp());
                case DISCONNECT -> serverStub.disconnect(message.getGameName(), message.getPlayerName());
            }
        } catch (RemoteException e) {
            closeConnection();
        }
    }

    /**
     * Closes the connection to the client.
     */
    @Override
    public void closeConnection() {
        heartbeatTimer.cancel();
        listeners.firePropertyChange("CONNECTION_CLOSED", null, null);
    }

    /**
     * Sends a heartbeat message to the server.
     * This method is used to check if the connection is still alive.
     */
    private void heartbeat() {
        heartbeatTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    serverStub.heartbeat();
                } catch (RemoteException e) {
                    logger.error("RMI Server Unreachable - detected when pinging.");
                    closeConnection();
                }
            }
        }, 0, 5000);
    }

    /**
     * Adds a PropertyChangeListener to the listeners list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listeners list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}
