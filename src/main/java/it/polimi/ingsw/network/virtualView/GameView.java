package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The GameView class represents the view of a game.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 * This class holds a string representing the current player, a GlobalBoardView object, and a list of PlayerView objects.
 */
public record GameView(String currentPlayer, GlobalBoardView globalBoardView,
                       List<PlayerView> playerViews, ArrayList<Player> winners,
                       java.util.ArrayList<it.polimi.ingsw.model.player.PlayerColorEnum> availablePlayerColors) implements Serializable {

    /**
     * Returns the view of the player with the given name.
     *
     * @param player the name of the player.
     * @return the view of the player with the given name.
     */
    public PlayerView getViewByPlayer(String player) {
        return playerViews.stream().filter(p -> p.playerName().equals(player)).findFirst().get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameView that)) return false;
        return Objects.equals(this.currentPlayer, that.currentPlayer) && Objects.equals(this.globalBoardView, that.globalBoardView) && Objects.equals(this.playerViews, that.playerViews);
    }
}
