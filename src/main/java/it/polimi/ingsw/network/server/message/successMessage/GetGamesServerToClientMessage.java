package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;

import java.util.HashSet;
import java.util.Objects;

/**
 * Network message used to send the list of games to the client.
 */
public class GetGamesServerToClientMessage extends ServerToClientMessage {
    /**
     * The list of games.
     */
    private final HashSet<GameRecord> games;

    /**
     * Constructor for the GetGamesNetworkMessage class.
     *
     * @param games the list of games.
     */
    public GetGamesServerToClientMessage(HashSet<GameRecord> games) {
        super(ServerActionEnum.GET_GAMES);
        this.games = games;
    }

    /**
     * Method that returns the list of games.
     *
     * @return the set of games.
     */
    @Override
    public HashSet<GameRecord> getGames() {
        return games;
    }

    /**
     * Method that returns a game record given its name.
     *
     * @param gameName the name of the game.
     * @return the game record with the given name, null if no game with the given name is found.
     */
    @Override
    public GameRecord getGame(String gameName) {
        return games.stream().filter(game -> game.gameName().equals(gameName)).findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetGamesServerToClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.games, that.games);
    }
}
