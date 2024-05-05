package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.RMIClientActions;

import java.rmi.RemoteException;

public class RMIClientAdapter implements ClientMessageHandler {
    private final RMIClientActions serverStub;

    private final String clientIpAddress;

    private final int clientPortNumber;

    public RMIClientAdapter(RMIClientActions serverStub, String clientIpAddress, int clientPortNumber) {
        this.serverStub = serverStub;
        this.clientIpAddress = clientIpAddress;
        this.clientPortNumber = clientPortNumber;
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    public void sendMessage(ClientMessage message) {
        //1. getGames()
        //This method invokes the remote getGames method
        //it's like: "Hey server, I just triggered you with the getGames action, call the method on me to give me those games!"
        //TODO: Listeners?
        PlayerActionEnum playerAction = message.getPlayerAction();
        try {
            switch (playerAction) {
                //TODO: will have to save the IP address of the server on the ClientAsAServer, maybe we could get it from the stub/registry?
                case GET_GAMES -> serverStub.getGames(this.clientIpAddress, this.clientPortNumber);
                case CREATE_GAME ->
                        serverStub.createGame(this.clientIpAddress, this.clientPortNumber, message.getGameName(), message.getPlayerName(), message.getNPlayers());
                case DELETE_GAME ->
                        serverStub.deleteGame(this.clientIpAddress, this.clientPortNumber, message.getGameName());
                case JOIN_GAME ->
                        serverStub.joinGame(this.clientIpAddress, this.clientPortNumber, message.getGameName(), message.getPlayerName());
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
