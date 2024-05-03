package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

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
        if (!(o instanceof GetGamesClientMessage)) return false;
        return super.equals(o);
    }
}