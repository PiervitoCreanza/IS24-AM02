package it.polimi.ingsw.model;

/**
 * Interface representing a card in the game.
 * This interface serves as a generic abstraction for all types of cards present in the game.
 */

public interface Card {
    int getPoints(PlayerBoard playerBoard);
}
