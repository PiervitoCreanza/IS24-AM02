package it.polimi.ingsw.network.clientMessage.InGameClientMessage;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.clientMessage.PlayerActionEnum;

public class PlaceCardClientMessage extends InGameClientMessage {
    private final Coordinate coordinate;
    private final GameCard card;

    public PlaceCardClientMessage(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        super(PlayerActionEnum.PLACECARD, gameName, playerName);
        this.coordinate = coordinate;
        this.card = card;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public GameCard getCard() {
        return card;
    }
}
