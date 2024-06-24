package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the ClientToServerMessage class and represents a specific type of client message: a request to get games.
 * It does not contain any additional data, as the action of getting games does not require any.
 */
public class GetGamesClientToServerMessage extends ClientToServerMessage {
    /**
     * Constructor for GetGamesClientToServerMessage.
     * Initializes the player action with the GETGAMES value.
     */
    public GetGamesClientToServerMessage() {
        super(PlayerActionEnum.GET_GAMES, null, null);
    }

    /**
     * Overrides the equals method from the Object class.
     * Checks if the object passed as parameter is equal to the current instance.
     *
     * @param o The object to compare with the current instance.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetGamesClientToServerMessage)) return false;
        return super.equals(o);
    }
}