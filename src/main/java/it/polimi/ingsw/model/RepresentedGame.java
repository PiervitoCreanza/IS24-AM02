package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

/**
 * This class represents a game in a format that can be easily serialized and deserialized.
 * It contains all the necessary information about a game, such as the game name, the maximum number of allowed players,
 * the list of players, the list of winners, the global board, and the current player.
 */
public class RepresentedGame {

    /**
     * The name of the game.
     */
    private String gameName;

    /** The maximum number of players allowed in the game. */
    private int maxAllowedPlayers;

    /** The list of players in the game. */
    private ArrayList<Player> players = null;

    /** The list of winners of the game. */
    private ArrayList<Player> winners = null;

    /** The global board of the game. */
    private GlobalBoard globalBoard = null;

    /** The current player in the game. */
    private Player currentPlayer = null;

    /**
     * This constructor creates a new RepresentedGame object from a Game object.
     * It copies all the necessary information from the Game object to the new RepresentedGame object.
     * @param game The Game object to be represented.
     */
    public RepresentedGame(Game game) {
        //TODO:We'll need to substitute RAND with GUID library to allow JSON serialization
        //this.gameName = game.getGameName();
        //this.maxAllowedPlayers = game.getMaxAllowedPlayers();
        //this.players = game.getPlayers();
        //this.winners = game.getWinners();
        //this.globalBoard = game.getGlobalBoard();
        //this.currentPlayer = game.getCurrentPlayer();
    }

    /**
     * This method serializes the RepresentedGame object into a JSON string.
     * It uses the Gson library to perform the serialization.
     * @return A JSON string representing the RepresentedGame object.
     */
    public String toJSON() {
        //return new Gson().toJson(this);
        return "\uD83D\uDD32 1\uFE0F⃣ \uD83D\uDD32 \uD83D\uDD32 \uD83D\uDD32\n" +
                "1\uFE0F⃣ 2\uFE0F⃣ 1\uFE0F⃣ \uD83D\uDD32 \uD83D\uDD32\n" +
                "1\uFE0F⃣ \uD83D\uDCA3 2\uFE0F⃣ 1\uFE0F⃣ \uD83D\uDD32\n" +
                "\uD83D\uDD32 2\uFE0F⃣ \uD83D\uDCA3 2\uFE0F⃣ 1\uFE0F⃣\n" +
                "\uD83D\uDD32 1\uFE0F⃣ 1\uFE0F⃣ 2\uFE0F⃣ \uD83D\uDCA3";
    }
}