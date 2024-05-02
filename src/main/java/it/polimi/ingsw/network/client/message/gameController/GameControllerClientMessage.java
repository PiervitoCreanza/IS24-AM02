package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This abstract class extends the ClientMessage class and represents a specific type of client message: an in-game message.
 * It contains the name of the game and the name of the player who is sending the message.
 * This class is meant to be extended by other classes that represent specific types of in-game client messages.
 */
public abstract class GameControllerClientMessage extends ClientMessage {
    /**
     * The name of the game.
     */
    private final String gameName;

    /**
     * The name of the player who is sending the message.
     */
    private final String playerName;

    /**
     * Constructor for GameControllerClientMessage.
     * Initializes the player action with the specified value, and sets the game name and player name.
     *
     * @param playerAction The action taken by the player. This cannot be null.
     * @param gameName     The name of the game. This cannot be null.
     * @param playerName   The name of the player who is sending the message. This cannot be null.
     */
    public GameControllerClientMessage(PlayerActionEnum playerAction, String gameName, String playerName) {
        super(playerAction);
        this.gameName = gameName;
        this.playerName = playerName;
    }

    /**
     * Returns the name of the game.
     *
     * @return The name of the game.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the name of the player who is sending the message.
     *
     * @return The name of the player who is sending the message.
     */
    public String getPlayerName() {
        return playerName;
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
        if (!(o instanceof GameControllerClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.gameName, that.gameName) && Objects.equals(this.playerName, that.playerName);
    }
}