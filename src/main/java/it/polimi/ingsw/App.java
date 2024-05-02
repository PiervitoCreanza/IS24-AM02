package it.polimi.ingsw;

import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.client.message.ClientMessage;
import it.polimi.ingsw.network.client.message.gameController.DrawCardFromGoldDeckClientMessage;
import it.polimi.ingsw.network.client.message.gameController.PlaceCardClientMessage;
import com.google.gson.*;

/**
 * Hello world!
 */
public class App {
    /**
     * Main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        Gson gson = new Gson();
        ClientMessage clientMessage = new DrawCardFromGoldDeckClientMessage("Partitona", "Paolo");
        String jsonMessage = gson.toJson(clientMessage);
        System.out.println(jsonMessage);
    }
}
