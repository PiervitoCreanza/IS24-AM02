package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.MessageStatusEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;

public abstract class SuccessServerMessage extends ServerMessage {
    private final ServerActionsEnum action;

    public SuccessServerMessage(ServerActionsEnum action) {
        super(MessageStatusEnum.SUCCESS);
        this.action = action;
    }

    public ServerActionsEnum getAction() {
        return action;
    }
}
