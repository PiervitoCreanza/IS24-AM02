package it.polimi.ingsw.network.server.message;

public class ErrorServerMessage extends ServerMessage {
    private String message;

    public ErrorServerMessage(String message) {
        super(MessageStatusEnum.ERROR);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
