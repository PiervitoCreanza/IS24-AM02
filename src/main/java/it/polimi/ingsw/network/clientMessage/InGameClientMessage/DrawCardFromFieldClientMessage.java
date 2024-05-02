package it.polimi.ingsw.network.clientMessage.InGameClientMessage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.clientMessage.PlayerActionEnum;

public class DrawCardFromFieldClientMessage extends InGameClientMessage {
    private final GameCard gameCard;

    public DrawCardFromFieldClientMessage(String gameName, String playerName, GameCard gameCard) {
        super(PlayerActionEnum.DRAWCARDFROMFIELD, gameName, playerName);
        this.gameCard = gameCard;
    }

    public GameCard getGameCard() {
        return gameCard;
    }
}
