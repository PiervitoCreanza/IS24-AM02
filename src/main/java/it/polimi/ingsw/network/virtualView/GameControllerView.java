package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.controller.GameStatusEnum;

/**
 * The GameControllerView class represents the view of a game controller.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 * This class holds a GameView object and a GameStatusEnum object.
 */
public record GameControllerView(
        GameView gameView,
        GameStatusEnum gameStatus, boolean isLastRound) {
}