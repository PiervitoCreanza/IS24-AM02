package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.actions.ServerToClientActions;
import it.polimi.ingsw.network.client.message.gameController.*;
import it.polimi.ingsw.network.client.message.mainController.CreateGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.DeleteGameClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.GetGamesClientToServerMessage;
import it.polimi.ingsw.network.client.message.mainController.JoinGameClientToServerMessage;
import it.polimi.ingsw.network.server.message.successMessage.GameRecord;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;


public class ClientNetworkControllerMapper implements ServerToClientActions {

    private ClientMessageHandler messageHandler;

    private GameControllerView view;

    private final PropertyChangeSupport support;


    public ClientNetworkControllerMapper() {
        support = new PropertyChangeSupport(this);
    }

    /* ***************************************
     * METHODS INVOKED BY THE CLIENT ON THE SERVER
     * ***************************************/

    public void getGames() {
        messageHandler.sendMessage(new GetGamesClientToServerMessage());
    }

    public void createGame(String gameName, String playerName, int nPlayers) {
        messageHandler.sendMessage(new CreateGameClientToServerMessage(gameName, playerName, nPlayers));
    }

    public void deleteGame(String gameName, String playerName) {
        messageHandler.sendMessage(new DeleteGameClientToServerMessage(gameName, playerName));
    }

    public void joinGame(String gameName, String playerName) {
        messageHandler.sendMessage(new JoinGameClientToServerMessage(gameName, playerName));
    }

    public void choosePlayerColor(String gameName, String playerName, PlayerColorEnum playerColor) {
        messageHandler.sendMessage(new ChoosePlayerColorClientToServerMessage(gameName, playerName, playerColor));
    }

    public void placeCard(String gameName, String playerName, Coordinate coordinate, GameCard card) {
        messageHandler.sendMessage(new PlaceCardClientToServerMessage(gameName, playerName, coordinate, card));
    }

    public void drawCardFromField(String gameName, String playerName, GameCard card) {
        messageHandler.sendMessage(new DrawCardFromFieldClientToServerMessage(gameName, playerName, card));
    }

    public void drawCardFromResourceDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromResourceDeckClientToServerMessage(gameName, playerName));
    }

    public void drawCardFromGoldDeck(String gameName, String playerName) {
        messageHandler.sendMessage(new DrawCardFromGoldDeckClientToServerMessage(gameName, playerName));
    }

    public void switchCardSide(String gameName, String playerName, GameCard card) {
        messageHandler.sendMessage(new SwitchCardSideClientToServerMessage(gameName, playerName, card));
    }

    public void setPlayerObjective(String gameName, String playerName, ObjectiveCard card) {
        messageHandler.sendMessage(new SetPlayerObjectiveClientToServerMessage(gameName, playerName, card));
    }

    /* ***************************************
     * METHODS INVOKED BY THE SERVER ON THE CLIENT
     * ***************************************/

    @Override
    public void receiveGameList(HashSet<GameRecord> games) {
        System.out.println("Received games: " + games);
        notify("GET_GAMES", games);
        //TODO: JavaFx event trigger
    }


    @Override
    public void receiveGameDeleted(String message) {
        System.out.println(message);
        notify("GAME_DELETED", message);
        //TODO: JavaFx event trigger

    }


    @Override
    public void receiveUpdatedView(GameControllerView updatedView) {
        this.view = updatedView;
        notify("UPDATE_VIEW", updatedView);
        //PlayerBoardComponent playerBoardComponent = new PlayerBoardComponent(updatedView.gameView().getViewByPlayer(updatedView.gameView().currentPlayer()).playerBoardView().playerBoard());
        //System.out.println(playerBoardComponent);
//        System.out.println("Received updated view: " + updatedView);
//        System.out.println("Current player: " + updatedView.gameView().currentPlayer());
//        System.out.println("Current game status: " + updatedView.gameStatus());
        //TODO: JavaFx event trigger
    }

    @Override
    public void receiveErrorMessage(String errorMessage) {
        System.out.println("Received error message: " + errorMessage);
        notify("ERROR", errorMessage);

    }

    public void setMessageHandler(ClientMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public GameControllerView getView() {
        return view;
    }

    /**
     * Adds a PropertyChangeListener to the ClientNetworkCommandMapper.
     *
     * @param listener the PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the ClientNetworkCommandMapper.
     *
     * @param listener the PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Notifies of a message.
     *
     * @param message the message to be sent.
     */
    private void notify(String propertyName, Object message) {
        support.firePropertyChange(propertyName, null, message);
    }
}
