package it.polimi.ingsw.network.client.message.mainController;

import it.polimi.ingsw.network.client.message.ClientToServerMessage;
import it.polimi.ingsw.network.client.message.PlayerActionEnum;

/**
 * The DeleteGameClientToServerMessage class extends the ClientToServerMessage class.
 * It represents a message sent from the client to the server to delete a game.
 * This class is part of the network client message mainController package.
 */
public class DeleteGameClientToServerMessage extends ClientToServerMessage {

    /**
     * The constructor for the DeleteGameClientToServerMessage class.
     * It initializes the superclass with the DELETE_GAME action, the game name, and the player name.
     *
     * @param gameName   the name of the game to be deleted.
     * @param playerName the name of the player who wants to delete the game.
     */
    public DeleteGameClientToServerMessage(String gameName, String playerName) {
        super(PlayerActionEnum.DELETE_GAME, gameName, playerName);
    }

    /**
     * Overrides the equals method from the Object class.
     * It checks if the given object is equal to this DeleteGameClientToServerMessage object.
     * Two DeleteGameClientToServerMessage objects are equal if they are the same object or if they are both instances of DeleteGameClientToServerMessage and their superclass objects are equal.
     *
     * @param o the object to be compared with this DeleteGameClientToServerMessage object.
     * @return true if the given object is equal to this DeleteGameClientToServerMessage object, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteGameClientToServerMessage)) return false;
        return super.equals(o);
    }
}