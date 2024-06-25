package it.polimi.ingsw.gui.controllers;

/**
 * Enum representing the different controllers in the application.
 * Each enum value corresponds to a different scene in the application.
 * The enum value also stores the name of the FXML file associated with the scene.
 */
public enum ControllersEnum {
    /**
     * Represents the Home scene.
     */
    HOME("HomeScene"),

    /**
     * Represents the Start scene.
     */
    START("StartScene"),

    /**
     * Represents the Main Menu scene.
     */
    MAIN_MENU("MainMenuScene"),

    /**
     * Represents the Create Game scene.
     */
    CREATE_GAME("CreateGameScene"),

    /**
     * Represents the Join Game scene.
     */
    JOIN_GAME("JoinGameScene"),

    /**
     * Represents the Games List scene.
     */
    GAMES_LIST("GamesListScene"),

    /**
     * Represents the Waiting For Player scene.
     */
    WAITING_FOR_PLAYER("WaitingForPlayerScene"),

    /**
     * Represents the Game scene.
     */
    GAME_SCENE("GameScene"),

    /**
     * Represents the Init Place Starter Card scene.
     */
    INIT_PLACE_STARTER_CARD("InitPlaceStarterCardScene"),

    /**
     * Represents the Init Set Objective Card scene.
     */
    INIT_SET_OBJECTIVE_CARD("InitSetObjectiveCardScene"),

    /**
     * Represents the Init Select Pion scene.
     */
    INIT_SET_PION("InitSelectPionScene"),

    /**
     * Represents the Winner scene.
     */
    WINNER_SCENE("WinnerScene"),

    /**
     * Represents the Credits scene.
     */
    CREDITS("CreditsScene");

    /**
     * The name of the FXML file associated with the scene.
     */
    private final String fxmlFileName;

    /**
     * Constructor for the enum.
     *
     * @param fxmlFileName The name of the FXML file associated with the scene.
     */
    ControllersEnum(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    /**
     * Getter for the FXML file name.
     *
     * @return The name of the FXML file associated with the scene.
     */
    public String getFxmlFileName() {
        return fxmlFileName;
    }
}