package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The PlayerView class represents the view of a player.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 */
public record PlayerView(String playerName, int playerPos, ObjectiveCard objectiveCard,
                         ArrayList<ObjectiveCard> choosableObjectives, boolean isConnected, GameCard starterCard,
                         PlayerHandView playerHandView, PlayerBoardView playerBoardView) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerView that)) return false;
        return Objects.equals(this.playerName, that.playerName) && this.playerPos == that.playerPos && Objects.equals(this.objectiveCard, that.objectiveCard) && Objects.equals(this.choosableObjectives, that.choosableObjectives) && this.isConnected == that.isConnected && Objects.equals(this.starterCard, that.starterCard) && Objects.equals(this.playerHandView, that.playerHandView) && Objects.equals(this.playerBoardView, that.playerBoardView);
    }
}
