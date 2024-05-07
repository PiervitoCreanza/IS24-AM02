package it.polimi.ingsw.network.server.message;

import java.util.Objects;

public class ErrorServerToClientMessage extends ServerToClientMessage {
    private final String errorMessage;

    public ErrorServerToClientMessage(String message) {
        super(ServerActionEnum.ERROR_MSG);
        this.errorMessage = message;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorServerToClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.errorMessage, that.errorMessage);
    }
}
