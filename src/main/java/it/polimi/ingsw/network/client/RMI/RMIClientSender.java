package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.ClientMessageHandler;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;

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
     * Constructor for the RMIClientSender class.
     * Initializes the server and client stubs.
     *
     * @param serverStub     The RMIClientToServerActions instance that represents the server stub.
     * @param thisClientStub The RMIServerToClientActions instance that represents the client stub.
     */
    public RMIClientSender(RMIClientToServerActions serverStub, RMIServerToClientActions thisClientStub) {
        this.serverStub = serverStub;
        this.thisClientStub = thisClientStub;
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
        System.out.println("Error");
    }

    /**
     * Sends a heartbeat message to the server.
     * This method is used to check if the connection is still alive.
     */
    private void heartbeat() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    serverStub.heartbeat();
                } catch (RemoteException e) {
                    closeConnection();
                    cancel();
                }
            }
        }, 0, 2500);
    }
}
