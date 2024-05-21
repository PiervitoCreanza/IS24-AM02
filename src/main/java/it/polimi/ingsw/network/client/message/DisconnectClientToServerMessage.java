package it.polimi.ingsw.network.client.message;

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
