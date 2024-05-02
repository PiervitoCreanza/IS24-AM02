package it.polimi.ingsw.network.server.message;

public abstract class ServerMessage {
    public ServerActionEnum action;

    public ServerMessage(ServerActionEnum action) {
        this.action = action;
    }

    public ServerActionEnum getStatus() {
        return action;
    }
}
