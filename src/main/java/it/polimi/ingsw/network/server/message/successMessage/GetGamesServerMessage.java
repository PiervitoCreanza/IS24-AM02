package it.polimi.ingsw.network.server.message.successMessage;

import java.util.HashSet;

/**
 * Network message used to send the list of games to the client.
 */
public class GetGamesServerMessage extends SuccessServerMessage {
    /**
     * The list of games.
     */
    private final HashSet<GameRecord> games;

    /**
     * Constructor for the GetGamesNetworkMessage class.
     *
     * @param games the list of games.
     */
    public GetGamesServerMessage(HashSet<GameRecord> games) {
        super(ServerActionsEnum.GET_GAMES);
        this.games = games;
    }

    /**
     * Method that returns the list of games.
     *
     * @return the set of games.
     */
    public HashSet<GameRecord> getGames() {
        return games;
    }

    /**
     * Method that returns a game record given its name.
     *
     * @param gameName the name of the game.
     * @return the game record with the given name, null if no game with the given name is found.
     */
    public GameRecord getGame(String gameName) {
        return games.stream().filter(game -> game.gameName().equals(gameName)).findFirst().orElse(null);
    }
}
