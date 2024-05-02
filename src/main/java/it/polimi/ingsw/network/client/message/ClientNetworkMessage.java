package it.polimi.ingsw.network.client.message;

/**
 * This abstract class represents a network message from the client.
 * It contains a player action, which is an enum representing the type of action the player has taken.
 * This class is meant to be extended by other classes that represent specific types of client messages.
 */
public abstract class ClientNetworkMessage {
    /**
     * The action taken by the player.
     * This is an enum value representing the type of action the player has taken.
     */
    private final PlayerActionEnum playerAction;

    /**
     * Constructor for ClientNetworkMessage.
     * Initializes the player action with the specified value.
     *
     * @param playerAction The action taken by the player. This cannot be null.
     */
    public ClientNetworkMessage(PlayerActionEnum playerAction) {
        this.playerAction = playerAction;
    }

    /**
     * Returns the action taken by the player.
     *
     * @return The action taken by the player.
     */
    public PlayerActionEnum getPlayerAction() {
        return playerAction;
    }
}