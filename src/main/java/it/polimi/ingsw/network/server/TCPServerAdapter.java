package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.model.player.PlayerColorEnum;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.server.message.ServerMessage;

//TODO: implements methods
public class TCPServerAdapter implements ServerMessageHandler, ClientActions {

    @Override
    public void sendMessage(ServerMessage message) {

    }

    @Override
    public void closeConnection() {

    }

    @Override
    public void getGames() {

    }

    @Override
    public void createGame(ServerMessageHandler messageHandler, String gameName, int nPlayers, String playerName) {
        
    }

    @Override
    public void deleteGame(ServerMessageHandler messageHandler, String gameName) {

    }

    @Override
    public void joinGame(ServerMessageHandler messageHandler, String gameName, String playerName) {

    }

    @Override
    public void choosePlayerColor(ServerMessageHandler messageHandler, String gameName, String playerName, PlayerColorEnum playerColor) {

    }

    @Override
    public void placeCard(ServerMessageHandler messageHandler, String gameName, String playerName, Coordinate coordinate, GameCard card) {

    }

    @Override
    public void drawCardFromField(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) {

    }

    @Override
    public void drawCardFromResourceDeck(ServerMessageHandler messageHandler, String gameName, String playerName) {

    }

    @Override
    public void drawCardFromGoldDeck(ServerMessageHandler messageHandler, String gameName, String playerName) {

    }

    @Override
    public void switchCardSide(ServerMessageHandler messageHandler, String gameName, String playerName, GameCard card) {

    }

    @Override
    public void setPlayerObjective(ServerMessageHandler messageHandler, String gameName, String playerName, ObjectiveCard card) {

    }
}
