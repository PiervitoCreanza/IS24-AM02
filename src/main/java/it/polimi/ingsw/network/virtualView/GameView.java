package it.polimi.ingsw.network.virtualView;

import java.util.List;
import java.util.Objects;

/**
 * The GameView class represents the view of a game.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 * This class holds a string representing the current player, a GlobalBoardView object, and a list of PlayerView objects.
 */
public record GameView(String currentPlayer, GlobalBoardView globalBoardView, List<PlayerView> playerViews) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameView that)) return false;
        return Objects.equals(this.currentPlayer, that.currentPlayer) && Objects.equals(this.globalBoardView, that.globalBoardView) && Objects.equals(this.playerViews, that.playerViews);
    }
}
