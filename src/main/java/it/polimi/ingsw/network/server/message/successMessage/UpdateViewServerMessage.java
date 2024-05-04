package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.util.Objects;

public class UpdateViewServerMessage extends ServerMessage {
    private final GameControllerView view;

    public UpdateViewServerMessage(GameControllerView view) {
        super(ServerActionEnum.UPDATE_VIEW);
        this.view = view;
    }

    @Override
    public GameControllerView getView() {
        return view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateViewServerMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.getView(), that.getView());
    }
}
