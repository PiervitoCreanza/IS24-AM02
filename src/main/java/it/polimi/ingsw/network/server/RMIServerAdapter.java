package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ServerActions;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;

import java.rmi.RemoteException;

public class RMIServerAdapter implements ServerMessageHandler {
    private final ServerActions stub;

    private String playerName;

    private String gameName;

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
                case UPDATE_VIEW:
                    stub.receiveUpdatedView(message.getView());
                case DELETE_GAME:
                    stub.receiveGameDeleted("Game deleted successfully");
                case GET_GAMES:
                    stub.receiveGameList(message.getGames());
                case ERROR:
                    stub.receiveErrorMessage(message.getErrorMessage());
                default:
                    System.err.print("Invalid action");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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

    /**
     * Closes the connection to the client.
     */
    @Override
    public void closeConnection() throws RemoteException {
    }
}
