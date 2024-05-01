package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public class RepresentedGame {


    private String gameName;


    private int maxAllowedPlayers;


    private ArrayList<Player> players = null;


    private ArrayList<Player> winners = null;


    private GlobalBoard globalBoard = null;


    private Player currentPlayer = null;


    public RepresentedGame(Game game) {
        //this.gameName = game.getGameName();
        //this.maxAllowedPlayers = game.getMaxAllowedPlayers();
        //this.players = game.getPlayers();
        //this.winners = game.getWinners();
        //this.globalBoard = game.getGlobalBoard();
        //this.currentPlayer = game.getCurrentPlayer();
    }


    public String toJSON() {
        //return new Gson().toJson(this);
        return "\uD83D\uDD32 1\uFE0F⃣ \uD83D\uDD32 \uD83D\uDD32 \uD83D\uDD32\n" +
                "1\uFE0F⃣ 2\uFE0F⃣ 1\uFE0F⃣ \uD83D\uDD32 \uD83D\uDD32\n" +
                "1\uFE0F⃣ \uD83D\uDCA3 2\uFE0F⃣ 1\uFE0F⃣ \uD83D\uDD32\n" +
                "\uD83D\uDD32 2\uFE0F⃣ \uD83D\uDCA3 2\uFE0F⃣ 1\uFE0F⃣\n" +
                "\uD83D\uDD32 1\uFE0F⃣ 1\uFE0F⃣ 2\uFE0F⃣ \uD83D\uDCA3";
    }
}
