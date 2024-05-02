package it.polimi.ingsw.network.server.message;

public class ErrorServerMessage extends ServerMessage {
    private final String message;

    public ErrorServerMessage(String message) {
        super(ServerActionEnum.ERROR);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
