package it.polimi.ingsw.network.server.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;

import java.util.HashMap;

public record PlayerBoardView(HashMap<Coordinate, GameCard> playerBoard, GameItemStore gameItemStore) {
}
