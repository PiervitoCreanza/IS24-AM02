package it.polimi.ingsw.gui.controllers;

public enum ControllersEnum {
    HOME("HomeScene"),
    START("StartScene"), GAMES_LIST("GamesListScene");

    private final String fxmlFileName;

    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }


    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
