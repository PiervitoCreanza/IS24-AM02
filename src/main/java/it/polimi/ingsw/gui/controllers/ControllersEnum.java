package it.polimi.ingsw.gui.controllers;

public enum ControllersEnum {
    HOME("HomeScene"),
    START("StartScene"),
    MAIN_MENU("MainMenuScene"),
    CREATE_GAME("CreateGameScene"),
    GAMES_LIST("GamesListScene"),
    JOIN_GAME("JoinGameScene"),
    WAITING_FOR_PLAYER("WaitingForPlayerScene");

    private final String fxmlFileName;

    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }


    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
