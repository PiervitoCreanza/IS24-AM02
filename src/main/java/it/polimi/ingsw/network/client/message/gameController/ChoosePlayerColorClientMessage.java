package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.PlayerActionEnum;

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
