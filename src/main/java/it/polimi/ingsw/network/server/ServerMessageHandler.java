package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.MessageHandler;
import it.polimi.ingsw.network.server.message.ServerMessage;

/**
 * The ServerMessageHandler interface defines the methods that a server message handler must implement.
 * A server message handler is used to make RMI and TCP connections interchangeable.
 */
public interface ServerMessageHandler extends MessageHandler<ServerMessage> {
    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent
     */
    @Override
    void sendMessage(ServerMessage message);

    /**
     * Gets the name of the player associated with the connection.
     *
     * @return the name of the player
     */
    String getPlayerName();

    /**
     * Sets the name of the player associated with the connection.
     *
     * @param playerName the name of the player
     */
    void setPlayerName(String playerName);

    /**
     * Sets the name of the game associated with the connection.
     *
     * @param gameName the name of the game
     */
    void setGameName(String gameName);
    
    /**
     * Gets the name of the game associated with the connection.
     *
     * @return the name of the game
     */
    String getGameName();

    /**
     * Closes the connection to the client.
     */
    @Override
    void closeConnection();
}
