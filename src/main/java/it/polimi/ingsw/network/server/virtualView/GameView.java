package it.polimi.ingsw.network.server.virtualView;

import java.util.ArrayList;
import java.util.List;

public record GameView(String currentPlayer, GlobalBoardView globalBoardView, List<PlayerView> playerViews) {
}
