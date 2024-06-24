package it.polimi.ingsw.network.client.message;

/**
 * The DisconnectClientToServerMessage class is used by the client to notify the server that the player wants to disconnect.
 * It extends the ClientToServerMessage class.
 */
public class DisconnectClientToServerMessage extends ClientToServerMessage {

    /**
     * Constructor for ClientToServerMessage.
     * Initializes the player action with the specified value.
     *
     * @param gameName   The name of the game. This cannot be null.
     * @param playerName The name of the player who sent the message. This cannot be null.
     */
    public DisconnectClientToServerMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DISCONNECT, gameName, playerName);
    }
}
