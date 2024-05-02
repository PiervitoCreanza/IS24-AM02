package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;

public class UpdateViewServerMessage extends ServerMessage {
    private final GameControllerView view;

    public UpdateViewServerMessage(GameControllerView view) {
        super(ServerActionEnum.UPDATE_VIEW);
        this.view = view;
    }

    public GameControllerView getView() {
        return view;
    }
}
