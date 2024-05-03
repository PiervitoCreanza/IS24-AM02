package it.polimi.ingsw.network.server.message.successMessage;

public class DeleteGameServerMessage extends SuccessServerMessage {

    public DeleteGameServerMessage(ServerActionsEnum action) {
        super(ServerActionsEnum.DELETE_GAME);
    }
}
