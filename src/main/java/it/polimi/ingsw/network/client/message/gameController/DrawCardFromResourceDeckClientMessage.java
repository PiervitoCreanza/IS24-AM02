package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.PlayerActionEnum;

public class DrawCardFromResourceDeckClientMessage extends InGameClientMessage {
    public DrawCardFromResourceDeckClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DRAWCARDFROMRESOURCEDECK, gameName, playerName);
    }
}
