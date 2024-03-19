package it.polimi.ingsw.model;

/**
 * Represents a game card in the game.
 * A game card is composed by two sides and has a specific color.
 */
public class GameCard {
    private Side currentSide;
    private Side otherSide;
    private final CardColor cardColor;

    /**
     * Constructs a new GameCard with the given sides and color.
     *
     * @param currentSide the current side of the card
     * @param otherSide the other side of the card
     * @param cardColor the color of the card
     */
    public GameCard(Side currentSide, Side otherSide, CardColor cardColor) {
        this.currentSide = currentSide;
        this.otherSide = otherSide;
        this.cardColor = cardColor;
    }

    /**
     * Returns the current side of the card.
     *
     * @return the current side of the card
     */
    public Side getCurrentSide() {
        return currentSide;
    }

    /**
     * Switches the current side with the other side of the card.
     */
    public void switchSide(){
        Side tempSide = currentSide;
        currentSide = otherSide;
        otherSide = tempSide;
    }

    /**
     * Returns the color of the card.
     *
     * @return the color of the card
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    /**
     * Returns the game item store of the card.
     * This method is a stub and needs to be implemented.
     *
     * @return a new instance of GameItemStore
     */
    public GameItemStore getGameItemStore(){
        //TODO
        //STUB
        return new GameItemStore();
    }

    /**
     * Returns the points of the card for a given player board.
     * This method is a stub and needs to be implemented.
     *
     * @param playerBoard the player board to calculate the points for
     * @return 0 as this method is a stub
     */
    public Integer getPoints(PlayerBoard playerBoard){
        //TODO
        //STUB
        return 0;
    }
}