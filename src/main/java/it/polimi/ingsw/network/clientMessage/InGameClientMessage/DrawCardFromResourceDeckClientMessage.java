package it.polimi.ingsw.network.clientMessage.InGameClientMessage;

import it.polimi.ingsw.network.clientMessage.PlayerActionEnum;

public class DrawCardFromResourceDeckClientMessage extends InGameClientMessage {
    public DrawCardFromResourceDeckClientMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DRAWCARDFROMRESOURCEDECK, gameName, playerName);
    }
}
