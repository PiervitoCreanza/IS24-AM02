package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.MainController;

public class NetworkMessageDispatcher {
    private MainController mainController;

    public NetworkMessageDispatcher(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
