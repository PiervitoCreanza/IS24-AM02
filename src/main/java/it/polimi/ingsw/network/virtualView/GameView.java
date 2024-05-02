package it.polimi.ingsw.network.virtualView;

import java.util.List;

/**
 * The GameView class represents the view of a game.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 * This class holds a string representing the current player, a GlobalBoardView object, and a list of PlayerView objects.
 */
public record GameView(String currentPlayer, GlobalBoardView globalBoardView, List<PlayerView> playerViews) {
}
