package it.polimi.ingsw.network.client.message;

/**
 * This class extends the ClientMessage class and represents a specific type of client message: a request to get games.
 * It does not contain any additional data, as the action of getting games does not require any.
 */
public class GetGamesClientMessage extends ClientMessage {
    /**
     * Constructor for GetGamesClientMessage.
     * Initializes the player action with the GETGAMES value.
     */
    public GetGamesClientMessage() {
        super(PlayerActionEnum.GETGAMES);
    }
}