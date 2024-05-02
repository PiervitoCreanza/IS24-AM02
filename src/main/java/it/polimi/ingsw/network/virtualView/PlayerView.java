package it.polimi.ingsw.network.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;

import java.util.ArrayList;

/**
 * The PlayerView class represents the view of a player.
 * It is a record class, which means it is an immutable data carrier with the fields specified in the record declaration.
 */
public record PlayerView(String playerName, int playerPos, ObjectiveCard objectiveCard,
                         ArrayList<ObjectiveCard> choosableObjectives, boolean isConnected, GameCard starterCard,
                         PlayerHandView playerHandView, PlayerBoardView playerBoardView) {
};
