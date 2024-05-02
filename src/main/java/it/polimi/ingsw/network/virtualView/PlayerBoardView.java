package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.model.utils.store.GameItemStore;

import java.util.HashMap;

/**
 * The PlayerBoardView class represents the view of a player's board in the game.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 */
public record PlayerBoardView(HashMap<Coordinate, GameCard> playerBoard, GameItemStore gameItemStore) {
}
