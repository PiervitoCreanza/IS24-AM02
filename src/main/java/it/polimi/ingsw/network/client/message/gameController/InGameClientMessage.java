package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.ClientNetworkMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

public abstract class InGameClientMessage extends ClientNetworkMessage {
    private final String gameName;
    private final String playerName;

    public InGameClientMessage(PlayerActionEnum playerAction, String gameName, String playerName) {
        super(playerAction);
        this.gameName = gameName;
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
