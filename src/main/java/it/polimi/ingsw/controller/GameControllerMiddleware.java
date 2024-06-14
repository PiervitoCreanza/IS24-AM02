package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This class represents the middleware between the GameController and the PlayerActions interface.
 * It implements the PlayerActions interface and defines the actions flow that a player can perform in the game.
 */
public class GameControllerMiddleware implements PlayerActions {
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

    private void sendErrorToClient(Exception e) {

    }

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
     * Sets the current game status.
     *
     * @param gameStatus the game status to be set.
     */
    public void setGameStatus(GameStatusEnum gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * Gets the current game status.
     *
     * @return the current game status.
     */
    public GameStatusEnum getGameStatus() {
        return gameStatus;
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
        if (isLastRound && game.isLastPlayer()) {
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
    public Game getGame() {
        return game;
    }

    /**
     * Joins the game with the given player name.
     *
     * @param playerName the name of the player who is joining the game.
     */
    @Override
    public void joinGame(String playerName) {
        if (gameStatus != GameStatusEnum.WAIT_FOR_PLAYERS) {
            throw new IllegalStateException("Cannot join game in current game status");
        }
        try {
            gameController.joinGame(playerName);
        } catch (IllegalArgumentException e) {
            sendErrorToClient(e);
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
    public void placeCard(String playerName, Coordinate coordinate, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD && gameStatus != GameStatusEnum.INIT_PLACE_STARTER_CARD) {
            throw new IllegalStateException("Cannot place card in current game status");
        }

        try {
            gameController.placeCard(playerName, coordinate, card);
        } catch (IllegalArgumentException e) {
            sendErrorToClient(e);
        }


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
    public void choosePlayerColor(String playerName, PlayerColorEnum playerColor) {
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
    public void drawCardFromField(String playerName, GameCard card) {
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
    public void drawCardFromResourceDeck(String playerName) {
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
    public void drawCardFromGoldDeck(String playerName) {
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
    public void switchCardSide(String playerName, GameCard card) {
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
    public void setPlayerObjective(String playerName, ObjectiveCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
            throw new IllegalStateException("Cannot set player objective in current game status");
        }
        gameController.setPlayerObjective(playerName, card);

        game.setNextPlayer();

        // If the last player has chosen his objective card, the game status is set to PLACE_CARD
        if (game.getCurrentPlayerIndex() == 0) {
            gameStatus = GameStatusEnum.PLACE_CARD;
        } else {
            gameStatus = GameStatusEnum.INIT_PLACE_STARTER_CARD;
        }
    }
}
