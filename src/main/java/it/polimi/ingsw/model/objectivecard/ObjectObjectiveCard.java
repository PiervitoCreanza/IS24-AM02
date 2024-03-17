package it.polimi.ingsw.model.objectivecard;

import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.GameObjectStore;
import it.polimi.ingsw.model.PlayerBoard;
import java.util.ArrayList;

/**
 * Class for the ObjectiveCard that use object of the player to calculate the point
 */

public class ObjectObjectiveCard extends ObjectiveCard{
    private final GameObjectStore multiplier;
    public ObjectObjectiveCard(int pointsWon, GameObjectStore multiplier) {
        super(pointsWon);
        this.multiplier = multiplier;
    }

    /**
     * Calculate the points earned based on the number of object that the playerBoard has
     * @param playerBoard of the player
     * @return points earned by this card
     */

    @Override
    public int getPoints(PlayerBoard playerBoard) {
        ArrayList<GameObject> gameObjects = multiplier.getNonEmptyKeys();
        return gameObjects.stream().map(x -> playerBoard.getGameObjectAmount(x)/multiplier.get(x)).min(Integer::compareTo).orElse(0)*this.pointsWon;
    }
}
