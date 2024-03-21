package it.polimi.ingsw.model.ObjectiveCard;

import it.polimi.ingsw.model.CardColor;
import it.polimi.ingsw.model.PlayerBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Class for the PositionalObjectiveCard that use position of the card to calculate the point
 */

public class PositionalObjectiveCard extends ObjectiveCard {
    private final ArrayList<PositionalData> positionalData;

    public PositionalObjectiveCard(int pointsWon, ArrayList<PositionalData> positionalData) {
        super(pointsWon);
        if(positionalData == null) throw new IllegalArgumentException("positionalData must be not null");
        this.positionalData = positionalData;
    }

    /**
     * This method calculates and returns the points won by the player.
     * It checks if the player's game cards match the positional data required by the objective card.
     *
     * @param playerboard The player's game board.
     * @return The number of points won by the player.
     */

    @Override
    public int getPoints(PlayerBoard playerboard) {
        int numOfMach = 0;                          //variable to count how many times player made the configuration
        CardColor firstColor = positionalData.get(0).cardColor();
        ArrayList<Point> pointsCanMach = playerboard.getGameCards().stream()                  //Create a stream of GameCards
                .filter(x -> x.getCardColor() == firstColor)            //Filtering by the color that I want
                .map(x -> playerboard.getGameCardPosition(x).get())       //and at the end obtain the point of this card
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
            for (PositionalData position : positionalData) {
                temp.x = point.x + position.point().x;
                temp.y = point.y + position.point().y;
                //Move according to the coordinates marked on the hashmap
                if (pointsAlreadyUsed.contains(temp)) {
                    break;
                }
                if (playerboard.getGameCard(temp).isEmpty()) {
                    break;
                }
                if (playerboard.getGameCard(temp).get().getCardColor() != position.cardColor()) {
                    break;
                }
                pointsMaybeUsed.add(temp);
            }
            if (pointsMaybeUsed.size() == 3) {
                pointsAlreadyUsed.addAll(pointsMaybeUsed);
                numOfMach++;
            }
            //Rispetto a point devo muovermi di positionRequired.get(1).
        }
        return numOfMach * this.pointsWon;
    }
}

