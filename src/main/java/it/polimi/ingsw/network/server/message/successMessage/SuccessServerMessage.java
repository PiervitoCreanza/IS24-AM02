package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.MessageStatusEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;

public class SuccessServerMessage extends ServerMessage {
    private ServerActionsEnum action;

    public SuccessServerMessage(ServerActionsEnum action) {
        super(MessageStatusEnum.SUCCESS);
        this.action = action;
    }

    public ServerActionsEnum getAction() {
        return action;
    }
}
