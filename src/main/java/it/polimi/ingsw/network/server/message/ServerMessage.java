package it.polimi.ingsw.network.server.message;

public abstract class ServerMessage {
    public MessageStatusEnum status;

    public ServerMessage(MessageStatusEnum status) {
        this.status = status;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }
}
