package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;

public class DeleteGameServerMessage extends ServerMessage {

    public DeleteGameServerMessage(ServerActionEnum action) {
        super(ServerActionEnum.DELETE_GAME);
    }
}
