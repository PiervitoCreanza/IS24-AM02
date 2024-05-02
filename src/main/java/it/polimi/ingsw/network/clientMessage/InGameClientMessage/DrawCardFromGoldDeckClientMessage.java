package it.polimi.ingsw.network.clientMessage.InGameClientMessage;

import it.polimi.ingsw.network.clientMessage.PlayerActionEnum;

public class DrawCardFromGoldDeckClientMessage extends InGameClientMessage {
    public DrawCardFromGoldDeckClientMessage(PlayerActionEnum playerAction, String gameName, String playerName) {
        super(PlayerActionEnum.DRAWCARDFROMGOLDDECK, gameName, playerName);
    }
}
