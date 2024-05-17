package it.polimi.ingsw.network.client.message;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;

import java.util.Objects;

/**
 * This abstract class represents a network message from the client.
 * It contains a player action, which is an enum representing the type of action the player has taken.
 * This class is meant to be extended by other classes that represent specific types of client messages.
 */
public abstract class ClientToServerMessage {
    /**
     * The name of the game.
     * It is final, meaning it cannot be changed once it has been set.
     */
    protected final String gameName;

    /**
     * The name of the player who sent the message.
     * It is final, meaning it cannot be changed once it has been set.
     */
    protected final String playerName;
    /**
     * The action taken by the player.
     * This is an enum value representing the type of action the player has taken.
     * It is final, meaning it cannot be changed once it has been set.
     */
    private final PlayerActionEnum playerAction;


    /**
     * Constructor for ClientToServerMessage.
     * Initializes the player action with the specified value.
     *
     * @param playerAction The action taken by the player. This cannot be null.
     * @param gameName     The name of the game. This cannot be null.
     * @param playerName   The name of the player who sent the message. This cannot be null.
     */
    public ClientToServerMessage(PlayerActionEnum playerAction, String gameName, String playerName) {
        this.playerAction = playerAction;
        this.gameName = gameName;
        this.playerName = playerName;
    }

    /**
     * Returns the action taken by the player.
     *
     * @return The action taken by the player.
     */
    public PlayerActionEnum getPlayerAction() {
        return playerAction;
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
        if (!(o instanceof ClientToServerMessage that)) return false;
        return Objects.equals(this.playerAction, that.playerAction);
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
     * Returns the name of the player.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the number of players.
     * This method is not implemented and always returns 0.
     *
     * @return The number of players, always 0.
     */
    public int getNPlayers() {
        return 0;
    }

    /**
     * Returns the color of the player.
     * This method is not implemented and always returns null.
     *
     * @return The color of the player, always null.
     */
    public PlayerColorEnum getPlayerColor() {
        return null;
    }

    /**
     * Returns the objective card of the player.
     * This method is not implemented and always returns -1.
     *
     * @return The objective card of the player, always -1.
     */
    public int getObjectiveCardId() {
        return -1;
    }

    /**
     * Returns the coordinate of the player.
     * This method is not implemented and always returns null.
     *
     * @return The coordinate of the player, always null.
     */
    public Coordinate getCoordinate() {
        return null;
    }

    /**
     * Returns the game card in the message.
     * This method is not implemented here, but it is overridden by classes that provide cards.
     *
     * @return The game card of the player, always null.
     */
    public GameCard getGameCard() {
        return null;
    }

    /**
     * Returns the game card ID in the message.
     * This method is not implemented here, but it is overridden by classes that provide card ids.
     *
     * @return The game card ID of the player, always -1.
     */
    public int getGameCardId() {
        return -1;
    }

    /**
     * Returns the flipped status of the card.
     * This method is overridden by the PlaceCardClientToServerMessage class.
     *
     * @return The flipped status of the player.
     */
    public boolean isFlipped() {
        return false;
    }

    /**
     * Returns the message of the player.
     * This method is not implemented and always returns null.
     *
     * @return The message of the player, always null.
     */
    public String getMessage() {
        return null;
    }

    /**
     * Returns the receiver of the message.
     * This method is not implemented and always returns null.
     *
     * @return The receiver of the message, always null.
     */
    public String getReceiver() {
        return null;
    }

    /**
     * Returns the timestamp of the message.
     * This method is not implemented and always returns 0.
     *
     * @return The timestamp of the message, always 0.
     */
    public long getTimestamp() {
        return 0;
    }
}