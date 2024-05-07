package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

public class DeleteGameClientToServerMessage extends ClientToServerMessage {

    public DeleteGameClientToServerMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DELETE_GAME, gameName, playerName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteGameClientToServerMessage)) return false;
        return super.equals(o);
    }
}
