package it.polimi.ingsw.network.client.message;

public class GetGamesClientMessage extends ClientNetworkMessage {
    public GetGamesClientMessage() {
        super(PlayerActionEnum.GETGAMES);
    }
}
