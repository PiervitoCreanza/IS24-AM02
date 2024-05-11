package it.polimi.ingsw.network.client.message;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
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
     * The action taken by the player.
     * This is an enum value representing the type of action the player has taken.
     */
    private final PlayerActionEnum playerAction;

    protected final String gameName;

    protected final String playerName; //sender of the message

    /**
     * Constructor for ClientToServerMessage.
     * Initializes the player action with the specified value.
     *
     * @param playerAction The action taken by the player. This cannot be null.
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

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNPlayers() {
        return 0;
    }

    public PlayerColorEnum getPlayerColor() {
        return null;
    }

    public ObjectiveCard getObjectiveCard() {
        return null;
    }

    public Coordinate getCoordinate() {
        return null;
    }

    public GameCard getGameCard() {
        return null;
    }

    public String getMessage() {
        return null;
    }

    public String getReceiver() {
        return null;
    }
}