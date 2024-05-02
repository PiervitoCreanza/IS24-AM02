package it.polimi.ingsw.network.clientMessage;

public class GetGamesClientMessage extends ClientNetworkMessage {
    public GetGamesClientMessage() {
        super(PlayerActionEnum.GETGAMES);
    }
}
