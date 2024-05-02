package it.polimi.ingsw.network.clientMessage;

public class JoinGameClientMessage extends ClientNetworkMessage {
    private final String gameName;
    private final String playerName;

    public JoinGameClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.JOINGAME);
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
