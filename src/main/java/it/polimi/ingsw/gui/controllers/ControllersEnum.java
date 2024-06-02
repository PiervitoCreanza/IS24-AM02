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
    ;

    private final String fxmlFileName;

    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }


    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
