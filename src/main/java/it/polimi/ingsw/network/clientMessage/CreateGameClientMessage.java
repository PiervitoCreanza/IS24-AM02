package it.polimi.ingsw.network.clientMessage;

public class CreateGameClientMessage extends ClientNetworkMessage {
    private final String gameName;
    private final int nPlayers;
    private final String playerName;

    public CreateGameClientMessage(String gameName, int nPlayers, String playerName) {
        super(PlayerActionEnum.CREATEGAME);
        this.gameName = gameName;
        this.nPlayers = nPlayers;
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public String getPlayerName() {
        return playerName;
    }
}
