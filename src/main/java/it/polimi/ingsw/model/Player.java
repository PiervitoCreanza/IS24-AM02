package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ObjectiveCard.ObjectiveCard;

public class Player {
    private final String playerName;
    private int playerPos = 0;
    private final PlayerBoard playerBoard;
    private ObjectiveCard objectiveCard;
    private final Hand hand;

    public Player(String playerName) {
        this.playerName = playerName;
        playerBoard = new PlayerBoard();
        hand = new Hand();
    }

    /**
     * This method is used to get the name of the player.
     *
     * @return String This returns the name of the player.
     */

    public String getPlayerName() {
        return playerName;
    }

    /**
     * This method is used to get the position of the player.
     *
     * @return Integer This returns the position of the player.
     */
    public Integer getPlayerPos() {
        return playerPos;
    }

    /**
     * This method is used to get the PlayerBoard of the player.
     *
     * @return PlayerBoard This returns the PlayerBoard of the player.
     */
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * This method is used to get the Hand of the player.
     *
     * @return Hand This returns the Hand of the player.
     */
    public Hand getPlayerHand() {
        return hand;
    }

    /**
     * This method is used to get the ObjectiveCard of the player.
     *
     * @return ObjectiveCard This returns the ObjectiveCard of the player.
     */
    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }

    /**
     * This method is used to set the ObjectiveCard of the player.
     *
     * @param objectiveCard This is the ObjectiveCard to be set.
     */
    public void setPlayerObjective(ObjectiveCard objectiveCard) {
        this.objectiveCard = objectiveCard;
    }

    /**
     * This method is used to advance the position of the player.
     *
     * @param steps This is the number of steps to advance.
     * @return boolean This returns true if the player has surpassed the value of 20, false otherwise.
     */
    public boolean advancePlayerPos(Integer steps) {
        playerPos += steps;
        return playerPos >= 20;
    }
}