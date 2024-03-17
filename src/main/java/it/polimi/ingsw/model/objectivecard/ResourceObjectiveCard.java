package it.polimi.ingsw.model.objectivecard;
import it.polimi.ingsw.model.GameResource;
import it.polimi.ingsw.model.PlayerBoard;

/**
 * Class for the ObjectiveCard that use Resource of the player to calculate the point
 */

public class ResourceObjectiveCard extends ObjectiveCard{
    private final GameResource resource;
    public ResourceObjectiveCard(int pointsWon, GameResource resource) {
        super(pointsWon);
        this.resource = resource;
    }

    /**
     * Calculate the points earned based on the number of resources that the playerBoard has
     * @param playerBoard of the player
     * @return points earned by this card
     */

    @Override
    public int getPoints(PlayerBoard playerBoard) {
        return (playerBoard.getGameResourceAmount(this.resource)/3)*pointsWon;
    }
}
