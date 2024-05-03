package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

public class DeleteGameClientMessage extends ClientMessage {

    public DeleteGameClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DELETE_GAME, gameName, playerName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteGameClientMessage)) return false;
        return super.equals(o);
    }
}
