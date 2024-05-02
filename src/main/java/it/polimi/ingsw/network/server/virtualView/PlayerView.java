package it.polimi.ingsw.network.server.virtualView;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;

import java.util.ArrayList;

public record PlayerView(String playerName, int playerPos, ObjectiveCard objectiveCard,
                         ArrayList<ObjectiveCard> choosableObjectives, boolean isConnected, GameCard starterCard,
                         PlayerHandView playerHandView, PlayerBoardView playerBoardView) {
};
