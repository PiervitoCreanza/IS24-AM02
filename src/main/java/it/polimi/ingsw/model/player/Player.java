package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.PlayerView;
import it.polimi.ingsw.network.virtualView.VirtualViewable;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Player class represents a player in the game.
 * It contains the player's attributes and the PlayerBoard.
 */
public class Player implements VirtualViewable<PlayerView> {
    /**
     * The name of the player.
     */
    private final String playerName;

    /**
     * The position of the player in the game.
     * It is initialized to 0 at the start of the game.
     */
    private int playerPos = 0;

    /**
     * The player's board in the game.
     */
    private final PlayerBoard playerBoard;

    /**
     * The 2 objective cards that the player has to choose from.
     * The chosen objective card is stored in the objectiveCard attribute.
     */
    private final ArrayList<ObjectiveCard> choosableObjectives;

    /**
     * The objective card chosen by the player.
     */
    private ObjectiveCard objectiveCard;

    /**
     * The playerHand of the player, which contains the cards that the player holds.
     */
    private final PlayerHand playerHand;

    /**
     * The boolean that represents if the player is connected or not.
     */
    private boolean isConnected = true;

    /**
     * The color of the player.
     * This attribute represents the color assigned to the player in the game.
     * The color is represented as an enum of type PlayerColorEnum.
     */
    private PlayerColorEnum playerColor = PlayerColorEnum.NONE;

    /**
     * The number of placed cards.
     */
    private int numberOfPlacedCards = 0;

    /**
     * Constructor for the Player class.
     * Initializes the player's name, player board, and playerHand.
     *
     * @param playerName          The name of the player.
     * @param choosableObjectives The 2 objective cards that the player has to choose from.
     * @param starterCard         The starter card that the player receives at the beginning of the game.
     */
    public Player(String playerName, ArrayList<ObjectiveCard> choosableObjectives, GameCard starterCard) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }

        this.playerName = playerName;
        this.choosableObjectives = Objects.requireNonNull(choosableObjectives, "Drawn objectives cannot be null");
        this.playerBoard = new PlayerBoard(Objects.requireNonNull(starterCard, "Starter card cannot be null"));
        this.playerHand = new PlayerHand();
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
     * This method is used to get the PlayerHand of the player.
     *
     * @return PlayerHand This returns the PlayerHand of the player.
     */
    public PlayerHand getPlayerHand() {
        return playerHand;
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
     * @param objectiveCardId This is the ObjectiveCard to be set.
     */
    public void setPlayerObjective(int objectiveCardId) {
        ObjectiveCard choosenObjective = choosableObjectives.stream().filter(o -> o.getCardId() == objectiveCardId).findFirst().orElse(null);
        if (choosenObjective == null) {
            throw new IllegalArgumentException("Objective card must be one of the drawn objectives");
        }

        this.objectiveCard = choosenObjective;
    }

    public void forceSetPlayerObjective(ObjectiveCard objectiveCard) {
        this.objectiveCard = objectiveCard;
    }

    public void setChoosableObjectives(ArrayList<ObjectiveCard> choosableObjectives) {
        this.choosableObjectives.clear();
        this.choosableObjectives.addAll(choosableObjectives);
    }
    /**
     * This method is used to retrieve the connection status of the player.
     *
     * @return isConnected This is the player's connection status.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * This method is used to get the drawn objectives of the player.
     *
     * @return ObjectiveCard[] This returns the drawn objectives of the player.
     */
    public ArrayList<ObjectiveCard> getChoosableObjectives() {
        return choosableObjectives;
    }

    /**
     * This method is used to set the connection status of the player.
     *
     * @param connected This is the connection status to be set.
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
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

    /**
     * Sets the color of the player.
     *
     * @param playerColor The color to be set for the player. It is an enum of type PlayerColorEnum.
     */
    public void setPlayerColor(PlayerColorEnum playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * Retrieves the color of the player.
     *
     * @return PlayerColorEnum The color of the player. It is an enum of type PlayerColorEnum.
     */
    public PlayerColorEnum getPlayerColor() {
        return playerColor;
    }

    /**
     * This method is used to set the game card on the player board.
     *
     * @param coordinate The coordinate where the game card is to be set.
     * @param gameCardId The game card to be set.
     */
    public void placeGameCard(Coordinate coordinate, int gameCardId) {
        GameCard cardToPlace = playerHand.getById(gameCardId);
        if (cardToPlace == null) {
            if (playerBoard.getStarterCard().getCardId() != gameCardId) {
                throw new IllegalArgumentException("Player does not have the card in hand or it is not the starter card");
            }
            cardToPlace = playerBoard.getStarterCard();
        }
        // Set the placement index of the card.
        cardToPlace.setPlacementIndex(++numberOfPlacedCards);

        playerPos += playerBoard.placeGameCard(coordinate, cardToPlace);
        playerHand.removeCard(cardToPlace);
    }

    /**
     * Gets the virtual view provided by this object.
     *
     * @return the virtual view of type PlayerView
     */
    @Override
    public PlayerView getVirtualView() {
        return new PlayerView(playerName, playerColor, playerPos, objectiveCard, choosableObjectives, isConnected, playerBoard.getStarterCard(), playerHand.getVirtualView(), playerBoard.getVirtualView());
    }
}
