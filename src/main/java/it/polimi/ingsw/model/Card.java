package it.polimi.ingsw.model;
/**
 * Interface representing a card in the game.
 * This interface serves as a generic abstraction for all types of cards present in the game.
 */
interface Card {
    public int getPoints(PlayerBoard playerBoard);
}
