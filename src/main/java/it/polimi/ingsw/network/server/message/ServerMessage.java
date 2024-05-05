package it.polimi.ingsw.network.server.message;

import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.io.Serializable;
import java.util.HashSet;

public abstract class ServerMessage implements Serializable {
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

    public GameControllerView getView() {
        return null;
    }

    public HashSet<GameRecord> getGames() {
        return null;
    }

    public GameRecord getGame(String gameName) {
        return null;
    }

    public String getErrorMessage() {
        return null;
    }
}
