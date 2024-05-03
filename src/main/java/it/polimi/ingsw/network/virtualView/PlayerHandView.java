package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The PlayerHandView class represents the view of a player's hand in the game.
 * It contains a list of GameCard.
 * A player's hand can hold up to 3 cards.
 */
public record PlayerHandView(ArrayList<GameCard> hand) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerHandView that)) return false;
        return Objects.equals(this.hand, that.hand);
    }
}
