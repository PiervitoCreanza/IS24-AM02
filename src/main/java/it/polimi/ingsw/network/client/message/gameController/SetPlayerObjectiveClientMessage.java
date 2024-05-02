package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

public class SetPlayerObjectiveClientMessage extends InGameClientMessage {
    private final ObjectiveCard objectiveCard;

    public SetPlayerObjectiveClientMessage(String gameName, String playerName, ObjectiveCard objectiveCard) {
        super(PlayerActionEnum.SETPLAYEROBJECTIVE, gameName, playerName);
        this.objectiveCard = objectiveCard;
    }

    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }
}
