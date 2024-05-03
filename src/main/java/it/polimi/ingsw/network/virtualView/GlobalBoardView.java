package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The GlobalBoardView class represents the view of the global board in the game.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 */
public record GlobalBoardView(GameCard goldFirstCard, GameCard resourceFirstCard, ArrayList<GameCard> fieldGoldCards,
                              ArrayList<GameCard> fieldResourceCards) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalBoardView that)) return false;
        return Objects.equals(this.goldFirstCard, that.goldFirstCard) && Objects.equals(this.resourceFirstCard, that.resourceFirstCard) && Objects.equals(this.fieldGoldCards, that.fieldGoldCards) && Objects.equals(this.fieldResourceCards, that.fieldResourceCards);
    }
}
