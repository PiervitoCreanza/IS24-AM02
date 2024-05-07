package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;

import java.util.Objects;

public class DeleteGameServerToClientMessage extends ServerToClientMessage {
    private final String successDeleteMessage;

    public DeleteGameServerToClientMessage() {
        super(ServerActionEnum.DELETE_GAME);
        this.successDeleteMessage = "The game has been deleted";
    }

    @Override
    public String getSuccessDeleteMessage() {
        return this.successDeleteMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteGameServerToClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.successDeleteMessage, that.successDeleteMessage);
    }
}
