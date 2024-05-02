package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;

import java.util.ArrayList;

/**
 * The GlobalBoardView class represents the view of the global board in the game.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 */
public record GlobalBoardView(GameCard goldFirstCard, GameCard resourceFirstCard, ArrayList<GameCard> fieldGoldCards,
                              ArrayList<GameCard> fieldResourceCards) {
}
