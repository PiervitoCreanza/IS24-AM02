package it.polimi.ingsw.controller.gameController;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GlobalBoard;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.network.virtualView.VirtualViewable;

/**
 * This class represents the middleware between the GameController and the PlayerActions interface.
 * It implements the PlayerActions interface and defines the actions flow that a player can perform in the game.
 * <p>
 * All the actions are synchronized to avoid concurrency problems. In fact the setPlayerConnectionStatus method
 * is called when a player disconnects, and it can be called in any moment of the game. We need to be sure that
 * the game status is consistent with the player actions.
 * </p>
 */
public class GameControllerMiddleware implements PlayerActions, VirtualViewable<GameControllerView> {
    /**
     * The GameController instance.
     */
    private final GameController gameController;
    /**
     * The Game instance.
     */
    private final Game game;

    /**
     * The current game status.
     */
    private GameStatusEnum gameStatus;

    /**
     * The boolean that represents if the game is in the last round.
     */
    private boolean isLastRound = false;

    /**
     * The number of remaining rounds to end the game.
     */
    private int remainingRoundsToEndGame = 1;


    /**
     * Constructor for GameControllerMiddleware.
     * Initializes the GameController with the given game name, number of players, and player name.
     *
     * @param gameName   the name of the game.
     * @param nPlayers   the number of players in the game.
     * @param playerName the name of the player.
     */
    public GameControllerMiddleware(String gameName, int nPlayers, String playerName) {
        this.gameController = new GameController(gameName, nPlayers, playerName);
        this.game = this.gameController.getGame();
        this.gameStatus = GameStatusEnum.WAIT_FOR_PLAYERS;
    }

    /**
     * Constructor for GameControllerMiddleware.
     * Initializes the GameController with the given game controller and game.
     *
     * @param gameController the game controller.
     * @param game           the game.
     */
    public GameControllerMiddleware(GameController gameController, Game game) {
        this.gameController = gameController;
        this.game = game;
        this.gameStatus = GameStatusEnum.WAIT_FOR_PLAYERS;
    }

    /**
     * Gets the current game status.
     *
     * @return the current game status.
     */
    public synchronized GameStatusEnum getGameStatus() {
        return gameStatus;
    }

    /**
     * Sets the current game status.
     *
     * @param gameStatus the game status to be set.
     */
    public synchronized void setGameStatus(GameStatusEnum gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Validates that the player turn is correct.
     *
     * @param playerName the name of the player.
     */
    private void validatePlayerTurn(String playerName) {
        if (!game.getCurrentPlayer().getPlayerName().equals(playerName)) {
            throw new IllegalStateException("It is not the turn of the player " + playerName);
        }
    }

    /**
     * Handles the turn finish.
     */
    private void handleDrawFinish() {
        // If the game is not in the last round and the current player is the first to finish,
        // the game status is set to LAST_ROUND
        if (!isLastRound && game.isLastRound()) {
            isLastRound = true;
        }
        // Check if the game has finished
        if (isLastRound && game.isLastPlayerAmongConnected()) {
            if (remainingRoundsToEndGame == 0) {
                game.calculateWinners();
                gameStatus = GameStatusEnum.GAME_OVER;

                // Return to prevent the next player from being set
                return;
            } else {
                remainingRoundsToEndGame--;
            }
        }

        gameStatus = GameStatusEnum.PLACE_CARD;

        // Set the next player
        game.setNextPlayer();
    }

    /**
     * Gets the current game instance.
     *
     * @return the current game instance.
     */
    @Override
    public synchronized Game getGame() {
        return game;
    }

    /**
     * Joins the game with the given player name.
     *
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public synchronized void joinGame(String playerName) {
        // If the game is not in the WAIT_FOR_PLAYERS status or the player is already connected, an exception is thrown
        if (gameStatus != GameStatusEnum.WAIT_FOR_PLAYERS && !game.isPlayerDisconnected(playerName)) {
            throw new IllegalStateException("Cannot join game in current game status");
        }

        if (game.isPlayerDisconnected(playerName)) {
            // If the player was disconnected we update his connection status.
            setPlayerConnectionStatus(playerName, true);
        } else {
            // Else we add the player to the game
            gameController.joinGame(playerName);
        }

        // If the game is ready to start, the game status is set to INIT_PLACE_STARTER_CARD
        if (game.isStarted()) {
            gameStatus = GameStatusEnum.INIT_PLACE_STARTER_CARD;
        }
    }

    /**
     * Places a card on the game field.
     *
     * @param playerName the name of the player who is placing the card.
     * @param coordinate the coordinate where the card should be placed.
     * @param card       the card to be placed.
     */
    @Override
    public synchronized void placeCard(String playerName, Coordinate coordinate, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD && gameStatus != GameStatusEnum.INIT_PLACE_STARTER_CARD) {
            throw new IllegalStateException("Cannot place card in current game status");
        }


        gameController.placeCard(playerName, coordinate, card);


        // If we are in the init status the next phase is to draw the hand cards and choose the player color
        if (gameStatus == GameStatusEnum.INIT_PLACE_STARTER_CARD) {
            gameController.drawCardFromResourceDeck(playerName);
            gameController.drawCardFromResourceDeck(playerName);
            gameController.drawCardFromGoldDeck(playerName);
            // Set the next status
            gameStatus = GameStatusEnum.INIT_CHOOSE_PLAYER_COLOR;
        } else {
            // If we are not in the init status the next phase is to draw a card
            gameStatus = GameStatusEnum.DRAW_CARD;
        }
    }

    /**
     * Chooses the color for a player.
     *
     * @param playerName  the name of the player who is choosing the color.
     * @param playerColor the color to be chosen.
     */
    public synchronized void choosePlayerColor(String playerName, PlayerColorEnum playerColor) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.INIT_CHOOSE_PLAYER_COLOR) {
            throw new IllegalStateException("Cannot choose player color in current game status");
        }
        gameController.choosePlayerColor(playerName, playerColor);

        // If the last player has chosen his color, the game status is set to INIT_CHOOSE_OBJECTIVE_CARD
        gameStatus = GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD;
    }

    /**
     * Draws a card from the game field.
     *
     * @param playerName the name of the player who is drawing the card.
     * @param card       the card to be drawn.
     */
    @Override
    public synchronized void drawCardFromField(String playerName, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }
        gameController.drawCardFromField(playerName, card);
        handleDrawFinish();
    }

    /**
     * Draws a card from the resource deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public synchronized void drawCardFromResourceDeck(String playerName) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }

        gameController.drawCardFromResourceDeck(playerName);
        handleDrawFinish();
    }

    /**
     * Draws a card from the gold deck.
     *
     * @param playerName the name of the player who is drawing the card.
     */
    @Override
    public synchronized void drawCardFromGoldDeck(String playerName) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }

        gameController.drawCardFromGoldDeck(playerName);
        handleDrawFinish();
    }

    /**
     * Switches the side of a card.
     *
     * @param playerName the name of the player who is switching the card side.
     * @param card       the card whose side is to be switched.
     */

    @Override
    public synchronized void switchCardSide(String playerName, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD && gameStatus != GameStatusEnum.INIT_PLACE_STARTER_CARD && gameStatus != GameStatusEnum.DRAW_CARD) {
            throw new IllegalStateException("Cannot switch card side in current game status");
        }
        gameController.switchCardSide(playerName, card);
    }

    /**
     * Sets the objective for a player.
     *
     * @param playerName the name of the player whose objective is to be set.
     * @param card       the objective card to be set for the player.
     */
    @Override
    public synchronized void setPlayerObjective(String playerName, ObjectiveCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
            throw new IllegalStateException("Cannot set player objective in current game status");
        }
        gameController.setPlayerObjective(playerName, card);

        // If the last player has chosen his objective card, the game status is set to PLACE_CARD
        if (game.isLastPlayerAmongConnected()) {
            gameStatus = GameStatusEnum.PLACE_CARD;
        } else {
            gameStatus = GameStatusEnum.INIT_PLACE_STARTER_CARD;
        }

        game.setNextPlayer();
    }

    /**
     * Sets the connection status of a player.
     *
     * @param playerName  the name of the player whose connection status is to be set.
     * @param isConnected the connection status to be set.
     */
    public synchronized void setPlayerConnectionStatus(String playerName, boolean isConnected) {
        gameController.setPlayerConnectionStatus(playerName, isConnected);
        Player currentPlayer = game.getCurrentPlayer();
        // If the player gets disconnected during his turn
        if (currentPlayer.getPlayerName().equals(playerName) && !isConnected) {
            switch (gameStatus) {
                // These cases don't have breaks because starting from the current status
                // we want to execute all the initialization steps until the last one.
                case INIT_PLACE_STARTER_CARD:
                    placeCard(playerName, new Coordinate(0, 0), currentPlayer.getPlayerBoard().getStarterCard());
                case INIT_CHOOSE_PLAYER_COLOR:
                    choosePlayerColor(playerName, game.getAvailablePlayerColors().getFirst());
                case INIT_CHOOSE_OBJECTIVE_CARD:
                    setPlayerObjective(playerName, currentPlayer.getChoosableObjectives().getFirst());
                    // End of initialization steps, break the switch.
                    break;
                case PLACE_CARD:
                    // End the player turn without placing or drawing a card, effectively skipping the player turn.
                    handleDrawFinish();
                    break;
                case DRAW_CARD:
                    // Draw a random card from the first non-empty deck.
                    GlobalBoard globalBoard = game.getGlobalBoard();
                    if (!globalBoard.isResourceDeckEmpty()) {
                        drawCardFromResourceDeck(playerName);
                    } else if (!globalBoard.isGoldDeckEmpty()) {
                        drawCardFromGoldDeck(playerName);
                    } else if (!globalBoard.getFieldResourceCards().isEmpty()) {
                        drawCardFromField(playerName, globalBoard.getFieldResourceCards().getFirst());
                    } else {
                        drawCardFromField(playerName, globalBoard.getFieldGoldCards().getFirst());
                    }
                    break;
            }
        }

    }

    /**
     * Checks if the player is the creator of the game.
     *
     * @param playerName the name of the player.
     * @return true if the player is the creator of the game, false otherwise.
     */
    @Override
    public boolean isCreator(String playerName) {
        return gameController.isCreator(playerName);
    }

    /**
     * Gets the virtual view of the game controller.
     *
     * @return the virtual view of the game controller.
     */
    @Override
    public GameControllerView getVirtualView() {
        return new GameControllerView(game.getVirtualView(), gameStatus, isLastRound);
    }
}
