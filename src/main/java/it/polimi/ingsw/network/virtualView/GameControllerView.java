package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.controller.GameStatusEnum;

import java.io.Serializable;
import java.util.Objects;

/**
 * The GameControllerView class represents the view of a game controller.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 * This class holds a GameView object and a GameStatusEnum object.
 */
public record GameControllerView(GameView gameView, GameStatusEnum gameStatus,
                                 boolean isLastRound, int remainingRoundsToEndGame) implements Serializable {

    /**
     * Returns the view of the current player.
     *
     * @return the view of the current player.
     */
    public PlayerView getCurrentPlayerView() {
        return gameView.getViewByPlayer(gameView.currentPlayer());
    }

    /**
     * Returns the view of the player with the given name.
     *
     * @param player the name of the player.
     * @return the view of the player with the given name.
     */
    public PlayerView getPlayerViewByName(String player) {
        return gameView.getViewByPlayer(player);
    }

    /**
     * Returns true if it is the turn of the player with the given name.
     *
     * @param player the name of the player.
     * @return true if it is the turn of the player with the given name.
     */
    public boolean isMyTurn(String player) {
        return gameView.currentPlayer().equals(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameControllerView that)) return false;
        return Objects.equals(this.gameView, that.gameView) && this.gameStatus == that.gameStatus && this.isLastRound == that.isLastRound;
    }
}