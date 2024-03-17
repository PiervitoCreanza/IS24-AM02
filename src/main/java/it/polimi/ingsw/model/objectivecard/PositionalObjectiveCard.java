package it.polimi.ingsw.model.objectivecard;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.PlayerBoard;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Class for the PositionalObjectiveCard that use position of the card to calculate the point
 */

public class PositionalObjectiveCard extends ObjectiveCard{
    private final ArrayList<PositionalData> positionRequired;

    public PositionalObjectiveCard(int pointsWon, ArrayList<PositionalData> positionRequired) {
        super(pointsWon);
        this.positionRequired = positionRequired;
    }

    /**
     * This method calculates and returns the points won by the player.
     * It checks if the player's game cards match the positional data required by the objective card.
     * @param playerboard The player's game board.
     * @return The number of points won by the player.
     */

    @Override
    public int getPoints(PlayerBoard playerboard) {
        int numOfMach = 0;                          //variable for count how many times player made the configuration
        CardColor firstColor = positionRequired.get(0).getColor();
        ArrayList<Point> pointsCanMach = playerboard.getGameCards().stream()                 //Create a stream of GameCards
                                        .filter(x -> x.getColor() == firstColor)            //Filtering by the color that I want
                                        .map(x -> playerboard.getGameCardPosition(x))       //and at the end obtain the point of this card
                                        .collect(Collectors.toCollection(ArrayList::new));
        HashSet<Point> pointsAlreadyUsed = new HashSet<>();             //List of points that are used for a mach

        for (Point point : pointsCanMach) {                                 //For every possible point that is founded, try to follow the
            ArrayList<Point> pointsMaybeUsed = new ArrayList<>();           //coordinate of Hashmap in positionRequired
            //pointsMaybeUsed is an ArrayList that contains the points which, if correct,
            // will be recorded in pointsAlreadyUsed
            pointsMaybeUsed.add(point);

            //Inizialization of pointsMaybeUsed
            Point temp = new Point();
            //Support variable
            for (int i = 1; i < positionRequired.size(); i++) {
                temp.x = point.x + positionRequired.get(i).getPoint().x;
                temp.y = point.y + positionRequired.get(i).getPoint().y;
                //Move according to the coordinates marked on the hashmap
                if(pointsAlreadyUsed.contains(temp)){
                    break;
                }
                if(playerboard.getGameCard(temp).getColor() == positionRequired.get(i).getColor()){
                    pointsMaybeUsed.add(temp);
                }
            }
            if(pointsMaybeUsed.size() == 3){
                pointsAlreadyUsed.addAll(pointsMaybeUsed);
                numOfMach++;
            }
            //Rispetto a point devo muovermi di positionRequired.get(1).
        }
        return numOfMach*this.pointsWon;
    }
}

