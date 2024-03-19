package it.polimi.ingsw.model;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a side of a game card in the game.
 * A side is composed by four corners.
 */
//TODO: Managing of access to a NULL corner is delegated to the caller (?)
public class Side {
    private Corner topRight;
    private Corner topLeft;
    private Corner bottomLeft;
    private Corner bottomRight;

    /**
     * Constructs a new Side with the given corners.
     *
     * @param topRight the top right corner of the side
     * @param topLeft the top left corner of the side
     * @param bottomLeft the bottom left corner of the side
     * @param bottomRight the bottom right corner of the side
     */
    public Side(Corner topRight, Corner topLeft, Corner bottomLeft, Corner bottomRight) {
        this.topRight = topRight;
        this.topLeft = topLeft;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Returns the corner of the side based on the given position.
     *
     * @param position the position of the corner
     * @return the corner at the given position
     */
    public Corner getCorner(CornerPosition position) {
        return switch (position) {
            case TOP_RIGHT -> topRight;
            case TOP_LEFT -> topLeft;
            case BOTTOM_LEFT -> bottomLeft;
            case BOTTOM_RIGHT -> bottomRight;
        };
    }

    /**
     * Sets the corner at the given position as covered.
     *
     * @param position the position of the corner to be covered
     */
    public void setCornerCovered(CornerPosition position){
        this.getCorner(position).setCovered();
    }

    /**
     * Returns the game item store of the side.
     * This method is a stub and needs to be implemented.
     *
     * @return a new instance of GameItemStore
     */
    public GameItemStore getGameItemStore(){
        //TODO
        //STUB
        return new GameItemStore();
    }

    /**
     * Returns the points of the side for a given player board.
     * This method is a stub and needs to be implemented.
     *
     * @param playerBoard the player board to calculate the points for
     * @return a new ArrayList of Point as this method is a stub
     */
    public ArrayList<Point> getPoints(PlayerBoard playerBoard){
        //TODO: Which type is returned? It depends by the implementation of PlayerBoard
        //STUB
        return new ArrayList<Point>();
    }

    /**
     * Returns the needed game item store of the side.
     * This method is a stub and needs to be implemented.
     *
     * @return a new instance of GameItemStore
     */
    public GameItemStore getNeededItemStore(){
        //TODO
        //STUB
        return new GameItemStore();
    }
}