package it.polimi.ingsw.network.server.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.card.gameCard.SerializableBooleanProperty;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.client.message.adapter.ServerToClientMessageAdapter;
import it.polimi.ingsw.network.server.ServerNetworkControllerMapper;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import it.polimi.ingsw.parsing.adapters.ObjectiveCardAdapter;
import it.polimi.ingsw.parsing.adapters.SerializableBooleanPropertyAdapter;
import it.polimi.ingsw.parsing.adapters.SideGameCardAdapter;

import java.beans.PropertyChangeEvent;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class PersistenceTest {

    public static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(SerializableBooleanProperty.class, new SerializableBooleanPropertyAdapter())
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter()) // Registering a type adapter for ServerToClientMessage class
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter()) // Registering a type adapter for SideGameCard class
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter()) // Registering a type adapter for ObjectiveCard class
            .create();
    public static Persistence persistence;
    public static JsonObject updateView;
    public static ArrayList<Thread> threads = new ArrayList<>();

    public static void setUp() {
        MainController mainController = new MainController();
        persistence = new Persistence(mainController, new ServerNetworkControllerMapper(mainController));
        try {
            Reader reader = new FileReader("src/test/java/it/polimi/ingsw/network/server/persistence/view.json");
            updateView = JsonParser.parseReader(reader).getAsJsonObject();

        } catch (Exception e) {
            throw new IllegalArgumentException("File not found!");
        }
    }

    public static PropertyChangeEvent saveEvent(int i) {
        JsonObject editedView = updateView.deepCopy();
        editedView.getAsJsonObject("gameView").addProperty("gameName", String.valueOf(i));
        return new PropertyChangeEvent(editedView, "SAVE", null, gson.fromJson(editedView, GameControllerView.class));
    }

    public static PropertyChangeEvent deleteEvent(int i) {
        return new PropertyChangeEvent(i, "DELETE", null, String.valueOf(i));
    }

    public static void main(String[] args) throws InterruptedException {
        setUp();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> persistence.propertyChange(saveEvent(finalI)));
            threads.add(thread);
            thread.start();
        }
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> persistence.propertyChange(deleteEvent(finalI)));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Thread.sleep(30000);
    }
}
