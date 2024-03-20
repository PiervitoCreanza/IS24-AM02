package it.polimi.ingsw.model;

import java.util.Objects;
import java.util.Optional;

public class GameCard {
    private Side currentSide;
    private Side otherSide;
    private final CardColor cardColor;


    public GameCard(Side currentSide, Side otherSide, CardColor cardColor) {
        this.currentSide = Objects.requireNonNull(currentSide, "currentSide cannot be null");
        this.otherSide = Objects.requireNonNull(otherSide, "otherSide cannot be null");
        this.cardColor = Objects.requireNonNull(cardColor, "cardColor cannot be null");
    }


    public Side getCurrentSide() {
        return currentSide;
    }


    public void switchSide() {
        Side tempSide = currentSide;
        currentSide = otherSide;
        otherSide = tempSide;
    }


    public CardColor getCardColor() {
        return cardColor;
    }

    public Optional<Corner> getCorner(CornerPosition position) {
        return currentSide.getCorner(position);
    }

    public GameItemEnum setCornerCovered(CornerPosition position) {
        return currentSide.setCornerCovered(position);
    }

    //masking underlying method
    public GameItemStore getGameItemStore() {
        return currentSide.getGameItemStore();
    }

    //masking underlying method
    public Integer getPoints(PlayerBoard playerBoard) {
        return currentSide.getPoints(playerBoard);
    }


    //masking underlying method
    public GameItemStore getNeededItemStore() {
        return currentSide.getNeededItemStore();
    }


}