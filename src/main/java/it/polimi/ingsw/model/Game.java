package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.store.Store;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameView;
import it.polimi.ingsw.network.virtualView.VirtualViewable;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Class that represents a single game in the system.
 */
public class Game implements VirtualViewable<GameView> {

    /**
     * Represents the name of the game.
     * This is a unique identifier for each game instance.
     */
    private final String gameName;

    /**
     * Represents the maximum number of players in the game.
     * This is the total number of players that can join a game.
     */
    private final int maxAllowedPlayers;

    /**
     * Represents the list of players in the game.
     * This is an ArrayList that contains all the Player objects that are currently participating in the game.
     */
    private final ArrayList<Player> players;
    /**
     * Represents the global board of the game.
     * This is a GlobalBoard object that contains all the global elements of the game, such as the decks and objectives.
     */
    private final GlobalBoard globalBoard;
    /**
     * Represents the winners of the game.
     * This is an ArrayList that contains the Player objects who won the game.
     * The winners are determined by the calculateWinners() method.
     */
    private ArrayList<Player> winners;
    /**
     * Represents the current player in the game.
     * This is a Player object that represents the player whose turn it is to play.
     */
    private Player currentPlayer;

    /**
     * Constructor for Game. Initializes a new game with the specified parameters.
     *
     * @param gameName          The name of the game.
     * @param maxAllowedPlayers The maximum number of players in the game.
     * @param playerName        The name of the player creating the game, he will also be the first player.
     * @throws IllegalArgumentException if the number of players is not between 2 and 4 and if the game name is invalid
     */
    public Game(String gameName, int maxAllowedPlayers, String playerName) {
        if (gameName == null || gameName.isBlank()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }
        this.gameName = gameName;
        if (maxAllowedPlayers < 2 || maxAllowedPlayers > 4)
            throw new IllegalArgumentException("Players must be between 2-4");
        this.maxAllowedPlayers = maxAllowedPlayers;
        this.players = new ArrayList<>();
        this.globalBoard = new GlobalBoard();
        this.players.add(instanceNewPlayer(playerName));
        this.currentPlayer = players.getFirst();
        this.winners = new ArrayList<>();
    }

    /**
     * This is a constructor for the Game class. It initializes a new game with the specified parameters.
     * It is only used for testing purpose.
     *
     * @param gameName          The name of the game.
     * @param maxAllowedPlayers The maximum number of players in the game.
     * @param playerName        The name of the player creating the game, he will also be the first player.
     * @param globalBoard       The global board of the game.
     * @throws IllegalArgumentException if the number of players is not between 2 and 4 and if the game name is invalid
     */
    public Game(String gameName, int maxAllowedPlayers, String playerName, GlobalBoard globalBoard) {
        if (gameName == null || gameName.isBlank()) {
            throw new IllegalArgumentException("Game name cannot be null or empty");
        }
        this.gameName = gameName;
        if (maxAllowedPlayers < 2 || maxAllowedPlayers > 4)
            throw new IllegalArgumentException("Players must be between 2-4");
        this.maxAllowedPlayers = maxAllowedPlayers;
        this.players = new ArrayList<>();
        this.globalBoard = globalBoard;
        this.players.add(instanceNewPlayer(playerName));
        this.currentPlayer = players.getFirst();
    }

    /**
     * This method creates a new player with the specified name.
     * It draws two objective cards from the global board's objective deck,
     * draws a starter card from the global board's starter deck and creates a new
     * Player object with the specified name, objective cards and starter card.
     * If an exception occurs while creating the player, the drawn objective cards and starter card
     * are added back to the global board's decks.
     *
     * @param playerName The name of the player to be created.
     * @return The Player object that represents the new player.
     */
    private Player instanceNewPlayer(String playerName) {
        ArrayList<ObjectiveCard> drawnObjectives = new ArrayList<>(List.of(globalBoard.getObjectiveDeck().draw(), globalBoard.getObjectiveDeck().draw()));
        GameCard starterCard = globalBoard.getStarterDeck().draw();
        try {
            return new Player(playerName, drawnObjectives, starterCard);
        } catch (Exception e) {
            drawnObjectives.forEach(objectiveCard -> globalBoard.getObjectiveDeck().addCard(objectiveCard));
            globalBoard.getStarterDeck().addCard(starterCard);
            throw e;
        }
    }

    /**
     * Returns the name of the game.
     *
     * @return The name of the game.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return ArrayList of Player objects.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the player with the specified name.
     *
     * @param playerName The name of the player to find.
     * @return The Player object that represents the player with the specified name.
     * @throws NullPointerException     if the playerName is null.
     * @throws IllegalArgumentException if a player with the specified name does not exist.
     */
    public Player getPlayer(String playerName) {
        Objects.requireNonNull(playerName, "The player name can't be NULL");
        Optional<Player> chosenPlayer = players.stream().filter(player -> player.getPlayerName().equals(playerName)).findFirst();
        if (chosenPlayer.isEmpty())
            throw new IllegalArgumentException("Player with name \"" + playerName + "\" doesn't exists");
        return chosenPlayer.get();
    }

    /**
     * Check if a player is disconnected.
     *
     * @param playerName The name of the player to check.
     * @return true if the player is connected, false otherwise.
     */
    public boolean isPlayerDisconnected(String playerName) {
        try {
            return !getPlayer(playerName).isConnected();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns the global board of the game.
     *
     * @return The GlobalBoard object that represents the global board of the game.
     */
    public GlobalBoard getGlobalBoard() {
        return globalBoard;
    }

    /**
     * Adds a new player to the game.
     * This method draws two objective cards from the global board's objective deck,
     * draws a starter card from the global board's starter deck and creates a new
     * Player object with the specified name, objective cards and starter card.
     * The new player is added to the list of players in the game.
     *
     * @param playerName The name of the player to be added.
     * @throws RuntimeException         if the maximum number of players has been reached.
     * @throws NullPointerException     if the playerName is null.
     * @throws IllegalArgumentException if a player with the same name already exists.
     */
    public void addPlayer(String playerName) {
        if (players.size() >= maxAllowedPlayers)
            throw new RuntimeException("Maximum number of players already reached");
        if (players.stream().map(Player::getPlayerName).anyMatch(name -> name.equals(playerName)))
            throw new IllegalArgumentException("A player with the same name, already exists");
        players.add(instanceNewPlayer(playerName));
    }

    /**
     * Returns the current player in the game.
     *
     * @return The Player object that represents the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the index of the current player in the list of connected players.
     *
     * @return The index of the current player.
     */
    public int getCurrentPlayerIndexAmongConnected() {
        return getConnectedPlayers().indexOf(currentPlayer);
    }

    /**
     * Checks if the current player is the last player in the game among the connected users.
     *
     * @return true if the provided player is the last player in the game, false otherwise.
     */
    public boolean isLastPlayerAmongConnected() {
        return getCurrentPlayerIndexAmongConnected() == getConnectedPlayers().size() - 1;
    }

    /**
     * Returns players that are connected.
     *
     * @return ArrayList of Player objects that represents the players that are connected.
     */
    public ArrayList<Player> getConnectedPlayers() {
        return players.stream().filter(Player::isConnected).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns players that are disconnected.
     *
     * @return ArrayList of Player objects that represents the players that are disconnected.
     */
    public ArrayList<Player> getDisconnectedPlayers() {
        return players.stream().filter(player -> !player.isConnected()).collect(Collectors.toCollection(ArrayList::new));
    }

    /*
    Ex. getPlayers = [p1,p2,p3,p4]
    connectedPlayers = [p2]
    currentPlayer = p3
    1. getFirstConnectedPlayerAfter(p3) return getFirstConnectedPlayerAfter(p4)
    2. getFirstConnectedPlayerAfter(p4) return getFirstConnectedPlayerAfter(p1)
    3. getFirstConnectedPlayerAfter(p1) return p2
     */
    private Player getFirstConnectedPlayerAfter(Player player) {
        int playerIndex = getPlayers().indexOf(player);
        Player nextPlayer = getPlayers().get((playerIndex + 1) % getPlayers().size());

        if (getConnectedPlayers().contains(nextPlayer)) {
            return nextPlayer;
        }

        // If the getConnectedPlayers() is empty we stop the recursion to avoid infinite loops.
        if (getConnectedPlayers().isEmpty())
            return null;

        return getFirstConnectedPlayerAfter(nextPlayer);
    }

    /**
     * Sets the next player in the game.
     * This method updates the currentPlayer variable to the next player in the list of players.
     */
    public void setNextPlayer() {
        // getFirstConnectedPlayerAfter is a recursive function that finds the first connected player
        // after the current player. If the current player is the last connected player, it will return the same player.
        currentPlayer = getFirstConnectedPlayerAfter(currentPlayer);
    }

    /**
     * Sets the current player in the game.
     * This method updates the currentPlayer variable to the player with the specified name.
     *
     * @param playerName The name of the player to be set as the current player.
     */
    public void setCurrentPlayer(String playerName) {
        this.currentPlayer = players.stream().filter(player -> player.getPlayerName().equals(playerName)).findFirst().get();
    }

    /**
     * Checks if the game has started by checking if enough players have joined the game.
     *
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return (players.size() == maxAllowedPlayers);
    }

    /**
     * This method checks if any player's position is greater than or equal to 20.
     * It also checks if both the goldDeck and resourceDeck are empty.
     * If so, the game can start its ending phase.
     *
     * @return true if a player has more than 20 points or both decks are empty, false otherwise.
     */
    public boolean isLastRound() {
        return players.stream().anyMatch(player -> player.getPlayerPos() >= 20) || (globalBoard.isGoldDeckEmpty() && globalBoard.isResourceDeckEmpty());
    }

    /**
     * Calculate the winner(s) of the game, first it checks who got more points, then,
     * if there's a tie, it checks who has completed more objectives. If it's still a tie
     * it just returns an array containing all winners. This array is then saved in the
     * winners attribute.
     */
    public void calculateWinners() {
        //Initialize a store that will contain for each player the number of objective cards he won.
        HashMap<Player, Integer> tempMap = new HashMap<>(maxAllowedPlayers);
        players.forEach(player -> tempMap.put(player, 0));
        Store<Player> objectiveCardsWon = new Store<>(tempMap);

        ArrayList<ObjectiveCard> objectives = globalBoard.getGlobalObjectives();

        for (Player player : players) {
            PlayerBoard playerBoard = player.getPlayerBoard();
            ObjectiveCard playerObjective = player.getObjectiveCard();
            objectives.add(playerObjective);
            for (ObjectiveCard objective : objectives) {
                int pointsWon = objective.getPoints(playerBoard);
                //If pointsWon aren't 0, we won an objective, so we increase the counter and advance the player.
                if (pointsWon != 0) {
                    objectiveCardsWon.increment(player, 1);
                    player.advancePlayerPos(pointsWon);
                }
            }
            //We remove the current player objective, so only the two global objectives remain.
            objectives.remove(playerObjective);
        }
        //A player wins if he has the highest score, if there's a tie the player who has completed more objectives wins.
        //If they both completed the same number of objectives, they are both winners.

        //We calculate the highest score reached by a player.
        int highestPlayerScore = players.stream().map(Player::getPlayerPos).max(Integer::compare).orElse(0);
        //We filter by players with the highest score, we can't use a simple max() because a tie is possible.
        ArrayList<Player> tempWinners = players.stream().filter(player -> player.getPlayerPos() == highestPlayerScore).collect(Collectors.toCollection(ArrayList::new));

        //We calculate the maximum number of objective cards won by players, it is needed to resolve the tie
        int maxObjectiveCardsWon = tempWinners.stream().map(objectiveCardsWon::get).max(Integer::compare).orElse(0);
        //We filter by players with the maxObjectiveCardsWon, and we save the array, a tie is still possible.
        this.winners = tempWinners.stream().filter(player -> objectiveCardsWon.get(player) == maxObjectiveCardsWon).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the winners of the game.
     * This method retrieves the winners variable which represents the list of players who won the game.
     * The winners are determined by the calculateWinners() method.
     *
     * @return ArrayList of Player objects that represents the winners of the game.
     */
    public ArrayList<Player> getWinners() {
        return winners;
    }

    /**
     * Returns the available player colors.
     *
     * @return ArrayList of PlayerColorEnum that represents the available player colors.
     */
    public ArrayList<PlayerColorEnum> getAvailablePlayerColors() {
        HashSet<PlayerColorEnum> unavailableColors = players.stream().map(Player::getPlayerColor).collect(Collectors.toCollection(HashSet::new));
        // Add NONE to the unavailable colors to avoid choosing it
        unavailableColors.add(PlayerColorEnum.NONE);
        return PlayerColorEnum.stream().filter(color -> !unavailableColors.contains(color)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Chooses the color for a player.
     *
     * @param playerColor the color to be chosen.
     * @param playerName  the player who is choosing the color.
     * @throws IllegalArgumentException if the color is already taken.
     */
    public void choosePlayerColor(String playerName, PlayerColorEnum playerColor) {
        if (players.stream().anyMatch(p -> p.getPlayerColor().equals(playerColor))) {
            throw new IllegalArgumentException("Color already chosen by another player");
        }
        getPlayer(playerName).setPlayerColor(playerColor);
    }

    /**
     * Returns the maximum number of players allowed in the game.
     *
     * @return The maximum number of players that can join a game.
     */
    public int getMaxAllowedPlayers() {
        return maxAllowedPlayers;
    }

    /**
     * Returns the virtual view of the game.
     *
     * @return GameView This returns the virtual view of the game.
     */
    @Override
    public GameView getVirtualView() {
        return new GameView(gameName, currentPlayer.getPlayerName(), globalBoard.getVirtualView(), players.stream().map(Player::getVirtualView).collect(Collectors.toList()), winners.stream().map(Player::getPlayerName).collect(Collectors.toCollection(ArrayList::new)), getAvailablePlayerColors());
    }

    /**
     * Removes a player from the game.
     * This method removes the player with the specified name from the list of players in the game.
     * It also adds the player's objective cards back to the global board's objective deck and the player's starter card back to the global board's starter deck.
     * If the player is the current player, the next player is set as the current player.
     *
     * @param playerName The name of the player to be removed.
     */
    public void removePlayer(String playerName) {
        Player player = getPlayer(playerName);
        player.getChoosableObjectives().forEach(objectiveCard -> globalBoard.getObjectiveDeck().addCard(objectiveCard));
        globalBoard.getStarterDeck().addCard(player.getPlayerBoard().getStarterCard());
        players.remove(player);
        if (!players.isEmpty() && player.equals(currentPlayer)) {
            setNextPlayer();
        }
    }

    /**
     * Returns the game record of the game.
     * It contains all the details needed by the lobby to display the game.
     *
     * @return GameRecord This returns the game record of the game.
     */
    public GameRecord getGameRecord() {
        return new GameRecord(gameName, players.size(), maxAllowedPlayers);
    }
}
