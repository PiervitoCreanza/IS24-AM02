package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;
import it.polimi.ingsw.network.server.RMIClientActions;

import java.rmi.RemoteException;

public class RMIClientAsAClient implements ClientMessageHandler {
    private final String ipAddress;
    private final int portNumber;
    private final RMIClientActions stub;

    public RMIClientAsAClient(RMIClientActions stub, String ipAddress, int portNumber) {
        this.stub = stub;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ClientMessage message) throws RemoteException {
        //1. getGames()
        //This method invokes the remote getGames method
        //it's like: "Hey server, I just triggered you with the getGames action, call the method on me to give me those games!"
        //TODO: Listeners?
        PlayerActionEnum playerAction = message.getPlayerAction();
        switch (playerAction) {
            //TODO: will have to save the IP address of the server on the ClientAsAServer, maybe we could get it from the stub/registry?
            case GET_GAMES -> stub.getGames("localhost", this.portNumber);
            case CREATE_GAME ->
                    stub.createGame(this.ipAddress, this.portNumber, message.getGameName(), message.getPlayerName(), message.getNPlayers());
            case DELETE_GAME -> stub.deleteGame(message.getGameName());
            case JOIN_GAME ->
                    stub.joinGame(this.ipAddress, this.portNumber, message.getGameName(), message.getPlayerName());
            case CHOOSE_PLAYER_COLOR ->
                    stub.choosePlayerColor(message.getGameName(), message.getPlayerName(), message.getPlayerColor());
            case SET_PLAYER_OBJECTIVE ->
                    stub.setPlayerObjective(message.getGameName(), message.getPlayerName(), message.getObjectiveCard());
            case PLACE_CARD ->
                    stub.placeCard(message.getGameName(), message.getPlayerName(), message.getCoordinate(), message.getGameCard());
            case DRAW_CARD_FROM_FIELD ->
                    stub.drawCardFromField(message.getGameName(), message.getPlayerName(), message.getGameCard());
            case DRAW_CARD_FROM_RESOURCE_DECK ->
                    stub.drawCardFromResourceDeck(message.getGameName(), message.getPlayerName());
            case DRAW_CARD_FROM_GOLD_DECK -> stub.drawCardFromGoldDeck(message.getGameName(), message.getPlayerName());
            case SWITCH_CARD_SIDE ->
                    stub.switchCardSide(message.getGameName(), message.getPlayerName(), message.getGameCard());
        }
    }

    /**
     * Closes the connection to the client.
     */
    @Override
    public void closeConnection() throws RemoteException {

    }
}
