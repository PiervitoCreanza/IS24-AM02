package it.polimi.ingsw.gui.controllers;

public enum ControllersEnum {
    HOME("HomeScene"),
    START("StartScene"),
    GAMES_LIST("GamesListScene"),
    MAIN_MENU("MainMenuScene"),
    CREATE_GAME("CreateGameScene"),
    JOIN_GAME("JoinGameScene");

    private final String fxmlFileName;

    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }


    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
