package it.polimi.ingsw.network.clientMessage.InGameClientMessage;

import it.polimi.ingsw.network.clientMessage.PlayerActionEnum;

public class ChoosePlayerColorClientMessage extends InGameClientMessage {
    private final String playerColor;

    public ChoosePlayerColorClientMessage(String playerColor, String gameName, String playerName) {
        super(PlayerActionEnum.CHOOSEPLAYERCOLOR, gameName, playerName);
        this.playerColor = playerColor;
    }

    public String getPlayerColor() {
        return playerColor;
    }
}
