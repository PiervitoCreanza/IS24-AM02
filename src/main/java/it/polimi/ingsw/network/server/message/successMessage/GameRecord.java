package it.polimi.ingsw.network.server.message.successMessage;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameRecord that)) return false;
        return this.joinedPlayers == that.joinedPlayers && this.maxAllowedPlayers == that.maxAllowedPlayers && Objects.equals(this.gameName, that.gameName);
    }
}