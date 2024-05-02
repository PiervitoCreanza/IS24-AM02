package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.virtualView.GameView;

public class ViewUpdateMessage extends SuccessServerMessage {
    private GameView view;

    public ViewUpdateMessage(GameView view) {
        super(ServerActionsEnum.UPDATE_VIEW);
        this.view = view;
    }

    public GameView getView() {
        return view;
    }
}
