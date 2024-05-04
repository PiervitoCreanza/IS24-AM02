package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;

import java.rmi.RemoteException;

public interface RMIClientActions extends ClientActions {
    @Override
    void getGames() throws RemoteException;

    @Override
    void createGame(ServerMessageHandler messageHandler, String gameName, int nPlayers);

    @Override
    void deleteGame(ServerMessageHandler messageHandler, String gameName);

    @Override
    void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName);

    @Override
    void choosePlayerColor(ServerMessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor);

    @Override
    void placeCard(ServerMessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card);

    @Override
    void drawCardFromField(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card);

    @Override
    void drawCardFromResourceDeck(ServerMessageHandler messageHandler, String gameName, String playerName);

    @Override
    void drawCardFromGoldDeck(ServerMessageHandler messageHandler, String gameName, String playerName);

    @Override
    void switchCardSide(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card);

    @Override
    void setPlayerObjective(ServerMessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card);
}
