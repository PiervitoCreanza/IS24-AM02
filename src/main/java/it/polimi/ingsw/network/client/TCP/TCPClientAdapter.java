package it.polimi.ingsw.network.client.TCP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.data.ObjectiveCardAdapter;
import it.polimi.ingsw.data.SideGameCardAdapter;
import it.polimi.ingsw.model.card.gameCard.SideGameCard;
import it.polimi.ingsw.model.card.objectiveCard.ObjectiveCard;
import it.polimi.ingsw.network.TCPConnectionHandler;
import it.polimi.ingsw.network.client.ClientMessageHandler;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.adapter.ServerToClientMessageAdapter;
import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.utils.PropertyChangeNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.Socket;

/**
 * The TCPClientAdapter class is responsible for handling TCP client operations.
 * It masks the TCPConnectionHandler in order to make RMI and TCP server operations interchangeable.
 * It implements the TCPObserver and ClientMessageHandler interfaces.
 * It is UNIQUE to every RMI player.
 */
public class TCPClientAdapter implements ClientMessageHandler, PropertyChangeListener, PropertyChangeNotifier {

    /**
     * The ServerNetworkControllerMapper object is used to map network commands to actions in the game.
     */
    private final ClientNetworkControllerMapper clientNetworkControllerMapper;

    /**
     * The TCPConnectionHandler object is used to handle TCP client connections.
     */
    private final TCPConnectionHandler serverConnectionHandler;

    /**
     * The property change support.
     */
    private final PropertyChangeSupport listeners;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger(TCPClientAdapter.class);

    /**
     * Gson instance for JSON serialization and deserialization.
     * It is configured with custom type adapters for ServerToClientMessage, SideGameCard, and ObjectiveCard classes.
     * This is a final variable, meaning it cannot be changed once it has been set.
     */
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ServerToClientMessage.class, new ServerToClientMessageAdapter()) // Registering a type adapter for ServerToClientMessage class
            .registerTypeAdapter(SideGameCard.class, new SideGameCardAdapter()) // Registering a type adapter for SideGameCard class
            .registerTypeAdapter(ObjectiveCard.class, new ObjectiveCardAdapter()) // Registering a type adapter for ObjectiveCard class
            .create(); // Creating the Gson instance

    /**
     * Constructs a new TCPClientAdapter object with the specified socket and ClientNetworkControllerMapper.
     *
     * @param socket                        the socket to be used by the TCPConnectionHandler
     * @param clientNetworkControllerMapper the clientNetworkControllerMapper to be used by the TCPClientAdapter
     */
    public TCPClientAdapter(Socket socket, ClientNetworkControllerMapper clientNetworkControllerMapper) {
        this.clientNetworkControllerMapper = clientNetworkControllerMapper;
        this.serverConnectionHandler = new TCPConnectionHandler(socket);
        this.serverConnectionHandler.addPropertyChangeListener(this);
        this.serverConnectionHandler.start();
        listeners = new PropertyChangeSupport(this);
    }


    /**
     * Handles property change events.
     * This method is called when a bound property is changed.
     * The method handles the following property changes:
     * - "CONNECTION_CLOSED": Logs a warning that the connection with the server has been lost.
     * - "MESSAGE_RECEIVED": Calls the receiveMessage method with the new value of the property.
     * Any other property change is considered invalid and an error is logged.
     *
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String changedProperty = evt.getPropertyName();
        switch (changedProperty) {
            case "CONNECTION_CLOSED" -> {
                logger.warn("Connection with server lost.");
                this.listeners.firePropertyChange("CONNECTION_CLOSED", null, null);
            }
            case "MESSAGE_RECEIVED" -> this.receiveMessage((String) evt.getNewValue());
            default -> logger.error("Invalid property change.");
        }
    }

    /**
     * Receives a message from the server.
     * The method deserializes the message and calls the appropriate method in the ClientNetworkControllerMapper.
     * The method handles the following server actions:
     * - UPDATE_VIEW: Calls the receiveUpdatedView method in the ClientNetworkControllerMapper with the view contained in the message.
     * - DELETE_GAME: Calls the receiveGameDeleted method in the ClientNetworkControllerMapper with the successDeleteMessage contained in the message.
     * - GET_GAMES: Calls the receiveGameList method in the ClientNetworkControllerMapper with the games contained in the message.
     * - ERROR_MSG: Calls the receiveErrorMessage method in the ClientNetworkControllerMapper with the errorMessage contained in the message.
     * - RECEIVE_CHAT_MSG: Calls the receiveChatMessage method in the ClientNetworkControllerMapper with the playerName, chatMessage, timestamp, and directMessage contained in the message.
     * Any other server action is considered invalid and an error is logged.
     *
     * @param message the message received from the server
     */
    private void receiveMessage(String message) {
        logger.debug("Received message: {}", message);
        ServerToClientMessage receivedMessage = this.gson.fromJson(message, ServerToClientMessage.class);
        ServerActionEnum serverAction = receivedMessage.getServerAction();
        switch (serverAction) {
            case UPDATE_VIEW -> clientNetworkControllerMapper.receiveUpdatedView(receivedMessage.getView());
            case DELETE_GAME ->
                    clientNetworkControllerMapper.receiveGameDeleted(receivedMessage.getSuccessDeleteMessage());
            case GET_GAMES -> clientNetworkControllerMapper.receiveGameList(receivedMessage.getGames());
            case ERROR_MSG -> clientNetworkControllerMapper.receiveErrorMessage(receivedMessage.getErrorMessage());
            case RECEIVE_CHAT_MSG ->
                    clientNetworkControllerMapper.receiveChatMessage(receivedMessage.getPlayerName(), receivedMessage.getChatMessage(), receivedMessage.getTimestamp(), receivedMessage.isDirectMessage());
            default -> logger.error("Invalid action");
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(ClientToServerMessage message) {
        String serializedMessage = this.gson.toJson(message);
        try {
            this.serverConnectionHandler.send(serializedMessage);
        } catch (IOException e) {
            logger.error("Error sending message: {}", e.getMessage());
        }
        // Debug
        logger.debug("Message sent: {}", serializedMessage);
    }

    /**
     * Closes the connection.
     */
    @Override
    public void closeConnection() {
        // Debug
        logger.info("Close the connection.");
        this.serverConnectionHandler.closeConnection();
    }


    /**
     * Adds a PropertyChangeListener to the listener list.
     * The listener will be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be added
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * The listener will no longer be notified of property changes.
     *
     * @param listener The PropertyChangeListener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }
}