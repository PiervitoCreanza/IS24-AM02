package it.polimi.ingsw.network.server.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;

import java.util.ArrayList;

public record GlobalBoardView(GameCard goldFirstCard, GameCard resourceFirstCard, ArrayList<GameCard> fieldGoldCards,
                              ArrayList<GameCard> fieldResourceCards) {
}
