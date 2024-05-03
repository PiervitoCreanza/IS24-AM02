package it.polimi.ingsw.network.server.message;

public abstract class ServerMessage {
    public ServerActionEnum serverAction;

    public ServerMessage(ServerActionEnum serverAction) {
        this.serverAction = serverAction;
    }

    public ServerActionEnum getServerAction() {
        return serverAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerMessage that)) return false;
        return this.getServerAction() == that.getServerAction();
    }
}
