package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.virtualView.GameControllerView;

public class ViewUpdateMessage extends SuccessServerMessage {
    private GameControllerView view;

    public ViewUpdateMessage(GameControllerView view) {
        super(ServerActionsEnum.UPDATE_VIEW);
        this.view = view;
    }

    public GameControllerView getView() {
        return view;
    }
}
