package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utils.Coordinate;

public class GameControllerMiddleware implements PlayerActions {
    private final GameController gameController;
    private final Game game;
    private Player firstPlayerToFinish;

    private GameStatusEnum gameStatus;

    int resourceCardsToDraw = 2;
    int goldCardsToDraw = 1;


    public GameControllerMiddleware(String gameName, int nPlayers, String playerName) {
        this.gameController = new GameController(gameName, nPlayers, playerName);
        this.game = this.gameController.getGame();
        this.gameStatus = GameStatusEnum.WAIT_FOR_PLAYERS;
    }

    private void validatePlayerTurn(String playerName) {
        if (!game.getCurrentPlayer().getPlayerName().equals(playerName)) {
            throw new IllegalStateException("It is not the turn of the player " + playerName);
        }
    }

    private void resetCardToDrawValues() {
        this.resourceCardsToDraw = 2;
        this.goldCardsToDraw = 1;
    }

    private void handleTurnFinish() {
        // If the game is in the initialization phase, the game does not end after the draw,
        // but after choosing the objective card.
        if (gameStatus == GameStatusEnum.INIT_DRAW_CARD || gameStatus == GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
            return;
        }

        // If the game is not in the last round and the current player is the first to finish,
        // the game status is set to LAST_ROUND
        if (gameStatus != GameStatusEnum.LAST_ROUND && game.isLastRound()) {
            gameStatus = GameStatusEnum.LAST_ROUND;
            firstPlayerToFinish = game.getCurrentPlayer();
        } else {
            // Else it's the next player's turn and the game status is set to PLACE_CARD
            gameStatus = GameStatusEnum.PLACE_CARD;
        }

        // Set the next player
        game.setNextPlayer();

        // Check if the game has finished
        if (gameStatus == GameStatusEnum.LAST_ROUND && game.getCurrentPlayer().equals(firstPlayerToFinish)) {
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
            gameStatus = GameStatusEnum.INIT_DRAW_CARD;
        }
    }

    @Override
    public void placeCard(String playerName, Coordinate coordinate, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD && gameStatus != GameStatusEnum.PLACE_STARTER_CARD) {
            throw new IllegalStateException("Cannot place card in current game status");
        }
        // TODO Do we have to check if the player is placing the right card (eg. starter card during initialization)?
        gameController.placeCard(playerName, coordinate, card);

        if (gameStatus == GameStatusEnum.PLACE_STARTER_CARD) {
            gameStatus = GameStatusEnum.INIT_DRAW_CARD;
        } else {
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
        handleTurnFinish();
    }

    @Override
    public void drawCardFromResourceDeck(String playerName) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD && gameStatus != GameStatusEnum.INIT_DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }

        if (gameStatus == GameStatusEnum.INIT_DRAW_CARD) {
            if (resourceCardsToDraw == 0) {
                throw new IllegalStateException("Cannot draw more than 2 resource cards");
            }
            resourceCardsToDraw--;
            if (resourceCardsToDraw == 0 && goldCardsToDraw == 0) {
                gameStatus = GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD;
            }
        }
        gameController.drawCardFromResourceDeck(playerName);
        handleTurnFinish();
    }

    @Override
    public void drawCardFromGoldDeck(String playerName) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.DRAW_CARD && gameStatus != GameStatusEnum.INIT_DRAW_CARD) {
            throw new IllegalStateException("Cannot draw card in current game status");
        }

        if (gameStatus == GameStatusEnum.INIT_DRAW_CARD) {
            if (goldCardsToDraw == 0) {
                throw new IllegalStateException("Cannot draw more than 1 gold card");
            }
            goldCardsToDraw--;
            if (resourceCardsToDraw == 0 && goldCardsToDraw == 0) {
                gameStatus = GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD;
            }
        }
        gameController.drawCardFromGoldDeck(playerName);
        handleTurnFinish();
    }

    @Override
    public void switchCardSide(String playerName, GameCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.PLACE_CARD) {
            throw new IllegalStateException("Cannot switch card side in current game status");
        }
        gameController.switchCardSide(card);
    }

    @Override
    public void setPlayerObjective(String playerName, ObjectiveCard card) {
        validatePlayerTurn(playerName);
        if (gameStatus != GameStatusEnum.INIT_CHOOSE_OBJECTIVE_CARD) {
            throw new IllegalStateException("Cannot set player objective in current game status");
        }
        gameController.setPlayerObjective(playerName, card);

        // Reset for the next player the cards to draw before switching from INIT_DRAW_CARD to INIT_CHOOSE_OBJECTIVE_CARD
        resetCardToDrawValues();

        game.setNextPlayer();

        // If the last player has chosen his objective card, the game status is set to PLACE_CARD
        if (game.getCurrentPlayerIndex() == 0) {
            gameStatus = GameStatusEnum.PLACE_CARD;
        }
    }
}
