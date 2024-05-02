package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

import java.util.Objects;

/**
 * This class extends the GameControllerClientMessage class and represents a specific type of in-game client message: a request to set a player objective.
 * It contains the name of the game, the name of the player who is setting the objective, and the objective card to be set.
 */
public class SetPlayerObjectiveClientMessage extends GameControllerClientMessage {
    /**
     * The objective card to be set.
     */
    private final ObjectiveCard objectiveCard;

    /**
     * Constructor for SetPlayerObjectiveClientMessage.
     * Initializes the player action with the SETPLAYEROBJECTIVE value, and sets the game name, player name, and objective card.
     *
     * @param gameName      The name of the game. This cannot be null.
     * @param playerName    The name of the player who is setting the objective. This cannot be null.
     * @param objectiveCard The objective card to be set. This cannot be null.
     */
    public SetPlayerObjectiveClientMessage(String gameName, String playerName, ObjectiveCard objectiveCard) {
        super(PlayerActionEnum.SET_PLAYER_OBJECTIVE, gameName, playerName);
        this.objectiveCard = objectiveCard;
    }

    /**
     * Returns the objective card to be set.
     *
     * @return The objective card to be set.
     */
    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
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
        if (!(o instanceof SetPlayerObjectiveClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.objectiveCard, that.objectiveCard);
    }
}