package it.polimi.ingsw.network.server.message.successMessage;

/**
 * Class representing a game record. This class contains all the informations needed to display a game in the lobby.
 */
public record GameRecord(String gameName, int joinedPlayers, int maxAllowedPlayers) {
    /**
     * Method that checks if the game is full.
     *
     * @return true if the game is full, false otherwise.
     */
    public boolean isFull() {
        return joinedPlayers == maxAllowedPlayers;
    }
}