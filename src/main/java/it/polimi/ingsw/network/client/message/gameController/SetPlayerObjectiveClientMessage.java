package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * This class extends the InGameClientMessage class and represents a specific type of in-game client message: a request to set a player objective.
 * It contains the name of the game, the name of the player who is setting the objective, and the objective card to be set.
 */
public class SetPlayerObjectiveClientMessage extends InGameClientMessage {
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
        super(PlayerActionEnum.SETPLAYEROBJECTIVE, gameName, playerName);
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
}