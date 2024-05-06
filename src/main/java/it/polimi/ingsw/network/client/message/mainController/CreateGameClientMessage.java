package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the ClientMessage class and represents a specific type of client message: a request to create a game.
 * It contains the name of the game to be created, the number of players, and the name of the player who is creating the game.
 */
public class CreateGameClientMessage extends ClientMessage {
    /**
     * The number of players in the game to be created.
     */
    private final int nPlayers;

    /**
     * Constructor for CreateGameClientMessage.
     * Initializes the player action with the CREATEGAME value, and sets the game name, number of players, and player name.
     *
     * @param gameName   The name of the game to be created. This cannot be null.
     * @param nPlayers   The number of players in the game to be created. This cannot be null.
     * @param playerName The name of the player who is creating the game. This cannot be null.
     */
    public CreateGameClientMessage(String gameName, int nPlayers, String playerName) {
        super(PlayerActionEnum.CREATE_GAME, gameName, playerName);
        this.nPlayers = nPlayers;
    }

    /**
     * Returns the number of players in the game to be created.
     *
     * @return The number of players in the game to be created.
     */
    @Override
    public int getNPlayers() {
        return nPlayers;
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
        if (!(o instanceof CreateGameClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return this.nPlayers == that.nPlayers && Objects.equals(this.gameName, that.gameName) && Objects.equals(this.playerName, that.playerName);
    }
}