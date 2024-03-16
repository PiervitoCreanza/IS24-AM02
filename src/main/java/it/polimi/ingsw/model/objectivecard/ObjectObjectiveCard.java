package it.polimi.ingsw.model.objectivecard;
import it.polimi.ingsw.model.GameObject;
import it.polimi.ingsw.model.PlayerBoard;
import java.util.Arrays;

/**
 * Class for the ObjectiveCard that use object of the player to calculate the point
 */

public class ObjectObjectiveCard extends ObjectiveCard{
    private final GameObject[] multiplier;
    public ObjectObjectiveCard(int pointsWon, GameObject[] multiplier) {
        super(pointsWon);
        this.multiplier = multiplier;
    }

    /**
     * Calculate the points earned based on the number of object that the playerBoard has
     * @param playerBoard of the player
     * @return points earned by this card
     */

    //There is two type of ObjectObjectiveCard
    //the first type is based on check a single object
    //the second type is based on check all the object
    @Override
    public int getPoints(PlayerBoard playerBoard) {
        int amountQuill = playerBoard.getGameObjectAmount(GameObject.QUILL);
        int amountInkwell = playerBoard.getGameObjectAmount(GameObject.INKWELL);
        int amountManuscript = playerBoard.getGameObjectAmount(GameObject.MANUSCRIPT);
        //Manage first
        if(Arrays.asList(this.multiplier).contains(null)){
            return switch (this.multiplier[0]) {
                case QUILL -> (amountQuill / 2) * pointsWon;
                case INKWELL -> (amountInkwell / 2) * pointsWon;
                case MANUSCRIPT -> (amountManuscript / 2) * pointsWon;
                default -> 0;
            };
        }
        //Manage second
        else{
            return Arrays.stream(new int[]{amountQuill, amountInkwell, amountManuscript})
                    .min().orElse(0)*this.pointsWon;
        }
    }
}
