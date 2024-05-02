package it.polimi.ingsw.network.client.message.gameController;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

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
