package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.virtualView.GameControllerView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreateGameSceneController extends Controller implements PropertyChangeListener {
    public static final ControllersEnum NAME = ControllersEnum.CREATE_GAME;
    private final static Logger logger = LogManager.getLogger(CreateGameSceneController.class);

    @FXML
    private TextField gameTextField;

    @FXML
    private TextField playerTextField;

    @FXML
    private ToggleButton twoPlayersButton;
    @FXML
    private ToggleButton threePlayersButton;
    @FXML
    private ToggleButton fourPlayersButton;

    @FXML
    private StackPane root;


    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    @Override
    public ControllersEnum getName() {
        return NAME;
    }

    /**
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {

        logger.debug("GamesListController beforeMount");

    }

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {
        networkControllerMapper.removePropertyChangeListener(this);
    }

    @FXML
    public void createGame(ActionEvent actionEvent) {
        String gameName = gameTextField.getText();
        String playerName = playerTextField.getText();
        setProperty("playerName", playerName);
        int playersNumber = 0;
        if (twoPlayersButton.isSelected()) {
            playersNumber = 2;
        } else if (threePlayersButton.isSelected()) {
            playersNumber = 3;
        } else if (fourPlayersButton.isSelected()) {
            playersNumber = 4;
        }
        logger.debug("Create game: {} player: {}, number of player: {}", gameName, playerName, playersNumber);
        if (gameName == null || playerName == null || playerName.isEmpty() || gameName.isEmpty())
            showErrorPopup("Some fields are empty", "Please insert both a game name and a player name to continue.");
        else {
            networkControllerMapper.createGame(gameName, playerName, playersNumber);
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        switchScene(getPreviousLayoutName());
    }


    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getPropertyName().equals("UPDATE_VIEW")) {
            showInfoBox("green", "Game created", "The game \"" + ((GameControllerView) evt.getNewValue()).gameView().gameName() + "\" has been successfully created");
            switchScene(ControllersEnum.WAITING_FOR_PLAYER, evt);
        }
    }

    public void initialize() {
        ToggleGroup playerNumberGroup = new ToggleGroup();
        twoPlayersButton.setToggleGroup(playerNumberGroup);
        threePlayersButton.setToggleGroup(playerNumberGroup);
        fourPlayersButton.setToggleGroup(playerNumberGroup);
        // Default selection
        twoPlayersButton.setSelected(true);

        root.onKeyPressedProperty().set(event -> {
            switch (event.getCode()) {
                case ENTER:
                    createGame(null);
                    break;
                case ESCAPE:
                    back(null);
                    break;
            }
        });
    }
}
