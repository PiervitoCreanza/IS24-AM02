package it.polimi.ingsw.gui.controllers;

public enum ControllersEnum {
    HOME("HomeScene"),
    START("StartScene"),
    MAIN_MENU("MainMenuScene"),
    CREATE_GAME("CreateGameScene"),
    JOIN_GAME("JoinGameScene"),
    GAMES_LIST("GamesListScene"),
    WAITING_FOR_PLAYER("WaitingForPlayerScene"),
    GAME_SCENE("GameScene"),
    INIT_PLACE_STARTER_CARD("InitPlaceStarterCardScene"),
    INIT_SET_OBJECTIVE_CARD("InitSetObjectiveCardScene"),
    INIT_SET_PION("SelectPionScene"),
    WINNER_SCENE("WinnerScene");

    private final String fxmlFileName;

    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }


    public String getFxmlFileName() {
        return fxmlFileName;
    }
}