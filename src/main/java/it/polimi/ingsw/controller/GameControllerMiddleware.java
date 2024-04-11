package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utils.Coordinate;

/**
 * This class represents the middleware between the GameController and the PlayerActions interface.
 * It implements the PlayerActions interface and defines the actions that a player can perform in the game.
 */
public class GameControllerMiddleware implements PlayerActions {
    private final GameController gameController;
    private final Game game;
    private Player firstPlayerToFinish;

    private GameStatusEnum gameStatus;
    private boolean isLastRound = false;

    int resourceCardsToDraw = 2;
    int goldCardsToDraw = 1;

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
     * Resets the cards to draw values.
     */
    private void resetCardToDrawValues() {
        this.resourceCardsToDraw = 2;
        this.goldCardsToDraw = 1;
    }

    /**
     * Handles the turn finish.
     */
    private void handleDrawFinish() {
        if (gameStatus == GameStatusEnum.INIT_DRAW_CARD) {
            // If the player has drawn all the cards, the game status is set to INIT_CHOOSE_OBJECTIVE_CARD (next phase)
            if (resourceCardsToDraw == 0 && goldCardsToDraw == 0) {
                gameStatus = GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD;
            }

            // If we are in the initialization phase, the turn does not end on draw, but on INIT_CHOOSE_OBJECTIVE_CARD.
            // We return to prevent the next player from being set.
            return;
        }

        // If the game is not in the last round and the current player is the first to finish,
        // the game status is set to LAST_ROUND
        if (!isLastRound && game.isLastRound()) {
            isLastRound = true;
            firstPlayerToFinish = game.getCurrentPlayer();
        }
        gameStatus = GameStatusEnum.PLACE_CARD;

        // Set the next player
        game.setNextPlayer();

        // Check if the game has finished
        if (isLastRound && game.getCurrentPlayer().getPlayerName().equals(firstPlayerToFinish.getPlayerName())) {
            game.calculateWinners();
            gameStatus = GameStatusEnum.GAME_OVER;
        }
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void joinGame(String playerName) {
        if (gameStatus != GameStatusEnum.WAIT_FOR_PLAYERS) {
            throw new IllegalStateException("Cannot join game in current game status");
        }
        gameController.joinGame(playerName);
        // If the game is ready to start, the game status is set to INIT_DRAW_CARD
        if (game.isStarted()) {
            gameStatus = GameStatusEnum.INIT_PLACE_STARTER_CARD;
        }
    }

    @Override
    public void placeCard(String playerName, Coordinate coordinate, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD && gameStatus != GameStatusEnum.INIT_PLACE_STARTER_CARD) {
            throw new IllegalStateException("Cannot place card in current game status");
        }
        // TODO Do we have to check if the player is placing the right card (eg. starter card during initialization)?
        gameController.placeCard(playerName, coordinate, card);

        // If we are in the init status the next phase is to draw the hand cards
        if (gameStatus == GameStatusEnum.INIT_PLACE_STARTER_CARD) {
            gameStatus = GameStatusEnum.INIT_DRAW_CARD;
        } else {
            // If we are not in the init status the next phase is to draw a card
            gameStatus = GameStatusEnum.DRAW_CARD;
        }
    }

    @Override
    public void drawCardFromField(String playerName, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }
        gameController.drawCardFromField(playerName, card);
        handleDrawFinish();
    }

    @Override
    public void drawCardFromResourceDeck(String playerName) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD && gameStatus != GameStatusEnum.INIT_DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }

        // If the game is in the initialization phase, the player can draw up to 2 resource cards and 1 gold card
        // Since we don't know the order in which the players will draw the cards, we have to keep track of the cards drawn
        if (gameStatus == GameStatusEnum.INIT_DRAW_CARD) {
            // The player cannot draw more than 2 resource cards
            if (resourceCardsToDraw == 0) {
                throw new IllegalStateException("Cannot draw more than 2 resource cards");
            }
            resourceCardsToDraw--;
        }
        gameController.drawCardFromResourceDeck(playerName);
        handleDrawFinish();
    }

    @Override
    public void drawCardFromGoldDeck(String playerName) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD && gameStatus != GameStatusEnum.INIT_DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }

        // If the game is in the initialization phase, the player can draw up to 2 resource cards and 1 gold card
        // Since we don't know the order in which the players will draw the cards, we have to keep track of the cards drawn
        if (gameStatus == GameStatusEnum.INIT_DRAW_CARD) {
            // The player cannot draw more than 1 gold card
            if (goldCardsToDraw == 0) {
                throw new IllegalStateException("Cannot draw more than 1 gold card");
            }
            goldCardsToDraw--;
        }
        gameController.drawCardFromGoldDeck(playerName);
        handleDrawFinish();
    }

    @Override
    public void switchCardSide(String playerName, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD) {
            throw new IllegalStateException("Cannot switch card side in current game status");
        }
        gameController.switchCardSide(playerName, card);
    }

    @Override
    public void setPlayerObjective(String playerName, ObjectiveCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
            throw new IllegalStateException("Cannot set player objective in current game status");
        }
        gameController.setPlayerObjective(playerName, card);

        // Reset the cards to draw before switching from INIT_DRAW_CARD to INIT_CHOOSE_OBJECTIVE_CARD for the next player
        resetCardToDrawValues();

        game.setNextPlayer();

        // If the last player has chosen his objective card, the game status is set to PLACE_CARD
        if (game.getCurrentPlayerIndex() == 0) {
            gameStatus = GameStatusEnum.PLACE_CARD;
        } else {
            gameStatus = GameStatusEnum.INIT_PLACE_STARTER_CARD;
        }
    }
}
