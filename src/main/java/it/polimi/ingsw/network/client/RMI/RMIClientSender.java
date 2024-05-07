package it.polimi.ingsw.network.client.RMI;

import it.polimi.ingsw.network.client.ClientMessageHandler;
import it.polimi.ingsw.network.client.actions.RMIServerToClientActions;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.actions.RMIClientToServerActions;

import java.rmi.RemoteException;

/**
 * The RMIClientSender class is responsible for sending messages to the server.
 */
public class RMIClientSender implements ClientMessageHandler {
    private final RMIClientToServerActions serverStub;

    private final RMIServerToClientActions thisClientStub;

    public RMIClientSender(RMIClientToServerActions serverStub, RMIServerToClientActions thisClientStub) {
        this.serverStub = serverStub;
        this.thisClientStub = thisClientStub;
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
                //TODO: will have to save the IP address of the server on the ClientAsAServer, maybe we could get it from the stub/registry?
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
                        serverStub.setPlayerObjective(message.getGameName(), message.getPlayerName(), message.getObjectiveCard());
                case PLACE_CARD ->
                        serverStub.placeCard(message.getGameName(), message.getPlayerName(), message.getCoordinate(), message.getGameCard());
                case DRAW_CARD_FROM_FIELD ->
                        serverStub.drawCardFromField(message.getGameName(), message.getPlayerName(), message.getGameCard());
                case DRAW_CARD_FROM_RESOURCE_DECK ->
                        serverStub.drawCardFromResourceDeck(message.getGameName(), message.getPlayerName());
                case DRAW_CARD_FROM_GOLD_DECK ->
                        serverStub.drawCardFromGoldDeck(message.getGameName(), message.getPlayerName());
                case SWITCH_CARD_SIDE ->
                        serverStub.switchCardSide(message.getGameName(), message.getPlayerName(), message.getGameCard());
            }
        } catch (RemoteException e) {
            // TODO: Handle exception properly.
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the client.
     */
    @Override
    public void closeConnection() {

    }
}
