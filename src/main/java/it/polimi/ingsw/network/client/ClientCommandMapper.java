package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.rmi.RemoteException;
import java.util.HashSet;


public class ClientCommandMapper implements ServerActions {

    private ClientMessageHandler messageHandler;

    private GameControllerView view;


    public ClientCommandMapper() {
    }

    /* ***************************************
     * METHODS INVOKED BY THE CLIENT ON THE SERVER
     * ***************************************/

    void getGames() {
        messageHandler.sendMessage(new GetGamesClientMessage());
    }

    void createGame(String gameName, String playerName, int nPlayers) {
        messageHandler.sendMessage(new CreateGameClientMessage(gameName, playerName, nPlayers));
    }

    void deleteGame(String gameName, String playerName) {
        messageHandler.sendMessage(new DeleteGameClientMessage(gameName, playerName));
    }

    void joinGame(String gameName, String playerName) {
        messageHandler.sendMessage(new JoinGameClientMessage(gameName, playerName));
    }

    void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) {
        messageHandler.sendMessage(new ChoosePlayerColorClientMessage(gameName, playerName, playerColor));
    }

    void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        messageHandler.sendMessage(new PlaceCardClientMessage(gameName, playerName, coordinate, card));
    }

    void drawCardFromField(String gameName, String playerName, GameCard card) {
        messageHandler.sendMessage(new DrawCardFromFieldClientMessage(gameName, playerName, card));
    }

    void drawCardFromResourceDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromResourceDeckClientMessage(gameName, playerName));
    }

    void drawCardFromGoldDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromGoldDeckClientMessage(gameName, playerName));
    }

    void switchCardSide(String gameName, String playerName, GameCard card) {
        messageHandler.sendMessage(new SwitchCardSideClientMessage(gameName, playerName, card));
    }

    void setPlayerObjective(String gameName, String playerName, ObjectiveCard card) {
        messageHandler.sendMessage(new SetPlayerObjectiveClientMessage(gameName, playerName, card));
    }

    /* ***************************************
     * METHODS INVOKED BY THE SERVER ON THE CLIENT
     * ***************************************/

    @Override
    public void receiveGameList(HashSet<GameRecord> games) {
        System.out.println("Received games: " + games);
        //TODO: JavaFx event trigger
    }


    @Override
    public void receiveGameDeleted(String message) {
        System.out.println(message);
        //TODO: JavaFx event trigger

    }


    @Override
    public void receiveUpdatedView(GameControllerView updatedView) {
        this.view = updatedView;
        System.out.println("Received updated view: " + updatedView);
        //TODO: JavaFx event trigger
    }

    @Override
    public void receiveErrorMessage(String errorMessage) {
        System.out.println("Received error message: " + errorMessage);

    }

    @Override
    public void heartbeat() throws RemoteException {

    }

    public void setMessageHandler(ClientMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public GameControllerView getView() {
        return view;
    }
}
