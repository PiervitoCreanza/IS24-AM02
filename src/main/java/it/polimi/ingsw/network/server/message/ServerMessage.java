package it.polimi.ingsw.network.server.message;

import it.polimi.ingsw.network.Message;

public class ServerMessage implements Message {
    public MessageStatusEnum status;

    public ServerMessage(MessageStatusEnum status) {
        this.status = status;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }
}
