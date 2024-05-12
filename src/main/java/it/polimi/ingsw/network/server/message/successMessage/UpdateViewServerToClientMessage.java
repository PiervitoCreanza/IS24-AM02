package it.polimi.ingsw.network.server.message.successMessage;

import it.polimi.ingsw.network.server.message.ServerActionEnum;
import it.polimi.ingsw.network.server.message.ServerToClientMessage;
import it.polimi.ingsw.network.virtualView.GameControllerView;

import java.util.Objects;

/**
 * This class represents a message from the server to the client that updates the view.
 * It extends the ServerToClientMessage class.
 */
public class UpdateViewServerToClientMessage extends ServerToClientMessage {
    /**
     * The view that needs to be updated.
     */
    private final GameControllerView view;

    /**
     * Constructor for UpdateViewServerToClientMessage.
     *
     * @param view The view that needs to be updated.
     */
    public UpdateViewServerToClientMessage(GameControllerView view) {
        super(ServerActionEnum.UPDATE_VIEW);
        this.view = view;
    }

    /**
     * Getter for the view.
     *
     * @return The view that needs to be updated.
     */
    @Override
    public GameControllerView getView() {
        return view;
    }

    /**
     * Overridden equals method for UpdateViewServerToClientMessage.
     * @param o The object to compare this UpdateViewServerToClientMessage to.
     * @return true if the given object is an UpdateViewServerToClientMessage with the same view.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateViewServerToClientMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.getView(), that.getView());
    }
}