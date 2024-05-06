package it.polimi.ingsw.network.server.message;

import java.util.Objects;

public class ErrorServerMessage extends ServerMessage {
    private final String message;

    public ErrorServerMessage(String message) {
        super(ServerActionEnum.ERROR);
        this.message = message;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorServerMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.getErrorMessage(), that.getErrorMessage());
    }
}
