package it.polimi.ingsw.network;

public class ClientCommandMessage {

    private boolean isValid() {
        //We are checking if the message contains valid data, GSON fills the object with default values if the JSON fields are not valid
        //In this case, the method always returns false, so the object is always considered invalid
        return false;
    }
}
