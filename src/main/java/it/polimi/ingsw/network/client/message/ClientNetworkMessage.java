package it.polimi.ingsw.network.client.message;

public abstract class ClientNetworkMessage {
    private final PlayerActionEnum playerAction;

    public ClientNetworkMessage(PlayerActionEnum playerAction) {
        this.playerAction = playerAction;
    }

    public PlayerActionEnum getPlayerAction() {
        return playerAction;
    }
}
