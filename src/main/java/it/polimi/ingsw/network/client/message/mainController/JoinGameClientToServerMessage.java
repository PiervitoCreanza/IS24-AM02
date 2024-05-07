package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the ClientToServerMessage class and represents a specific type of client message: a request to join a game.
 * It contains the name of the game to be joined and the name of the player who is joining the game.
 */
public class JoinGameClientToServerMessage extends ClientToServerMessage {
    /**
     * Constructor for JoinGameClientToServerMessage.
     * Initializes the player action with the JOINGAME value, and sets the game name and player name.
     *
     * @param gameName   The name of the game to be joined. This cannot be null.
     * @param playerName The name of the player who is joining the game. This cannot be null.
     */
    public JoinGameClientToServerMessage(String gameName, String playerName) {
        super(PlayerActionEnum.JOIN_GAME, gameName, playerName);
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
        if (!(o instanceof JoinGameClientToServerMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.gameName, that.gameName) && Objects.equals(this.playerName, that.playerName);
    }
}