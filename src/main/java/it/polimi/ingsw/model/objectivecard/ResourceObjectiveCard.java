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
        return switch (this.resource) {
            case PLANT -> (playerBoard.getgameResourceAmount(GameResource.PLANT) / 3) * pointsWon;
            case ANIMAL -> (playerBoard.getgameResourceAmount(GameResource.ANIMAL) / 3) * pointsWon;
            case FUNGI -> (playerBoard.getgameResourceAmount(GameResource.FUNGI) / 3) * pointsWon;
            case INSECT -> (playerBoard.getgameResourceAmount(GameResource.INSECT) / 3) * pointsWon;
            default -> 0;
        };
    }
}
