package it.polimi.ingsw.network.server.virtualView;

import it.polimi.ingsw.controller.GameStatusEnum;

public record GameControllerView(GameView gameView, GameStatusEnum gameStatus) {
}
