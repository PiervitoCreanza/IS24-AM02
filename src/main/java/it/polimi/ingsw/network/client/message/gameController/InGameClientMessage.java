package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.ClientNetworkMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This abstract class extends the ClientNetworkMessage class and represents a specific type of client message: an in-game message.
 * It contains the name of the game and the name of the player who is sending the message.
 * This class is meant to be extended by other classes that represent specific types of in-game client messages.
 */
public abstract class InGameClientMessage extends ClientNetworkMessage {
    /**
     * The name of the game.
     */
    private final String gameName;

    /**
     * The name of the player who is sending the message.
     */
    private final String playerName;

    /**
     * Constructor for InGameClientMessage.
     * Initializes the player action with the specified value, and sets the game name and player name.
     *
     * @param playerAction The action taken by the player. This cannot be null.
     * @param gameName     The name of the game. This cannot be null.
     * @param playerName   The name of the player who is sending the message. This cannot be null.
     */
    public InGameClientMessage(PlayerActionEnum playerAction, String gameName, String playerName) {
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
}