package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.network.client.message.PlayerActionEnum;

public class DrawCardFromGoldDeckClientMessage extends InGameClientMessage {
    public DrawCardFromGoldDeckClientMessage(PlayerActionEnum playerAction, String gameName, String playerName) {
        super(PlayerActionEnum.DRAWCARDFROMGOLDDECK, gameName, playerName);
    }
}
