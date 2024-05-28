package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreateGameSceneController extends Controller implements PropertyChangeListener {
    public static final ControllersEnum NAME = ControllersEnum.CREATE_GAME;
    private final static Logger logger = LogManager.getLogger(CreateGameSceneController.class);

    private ClientNetworkControllerMapper networkControllerMapper;

    @FXML
    private TextField gameTextField;

    @FXML
    private TextField playerTextField;

    @FXML
    private ToggleButton player2Button;
    @FXML
    private ToggleButton player3Button;
    @FXML
    private ToggleButton player4Button;

    private ToggleGroup playerNumberGroup;
    private int nPlayers = 0;


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
    public void beforeMount() {
        logger.debug("GamesListController beforeMount");
        if (networkControllerMapper == null) {
            networkControllerMapper = getProperty("networkControllerMapper");
            networkControllerMapper.addPropertyChangeListener(this);
        }
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
        nPlayers = playerNumberGroup.getSelectedToggle() == player2Button ? 2 : playerNumberGroup.getSelectedToggle() == player3Button ? 3 : 4;
        logger.debug("Create game: {} player: {}, number of player: {}", gameName, playerName, nPlayers);
        if (gameName == null || playerName == null || nPlayers == 0)
            showErrorPopup("Please fill all the fields");
        else {
            networkControllerMapper.createGame(gameName, playerName, nPlayers);
        }
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        switchScene(getPreviousLayoutName());
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("ERROR")) {
            Platform.runLater(() -> showErrorPopup((String) evt.getNewValue()));
        }
        if (evt.getPropertyName().equals("UPDATE_VIEW")) {
            switchScene(ControllersEnum.WAITING_FOR_PLAYER, evt);
        }
    }

    public void initialize() {
        playerNumberGroup = new ToggleGroup();
        player2Button.setToggleGroup(playerNumberGroup);
        player3Button.setToggleGroup(playerNumberGroup);
        player4Button.setToggleGroup(playerNumberGroup);

        // Add mouse hover listeners for player2Button
        player2Button.setOnMouseEntered(e -> player2Button.getStyleClass().add("toggle-button-hover"));
        player2Button.setOnMouseExited(e -> player2Button.getStyleClass().remove("toggle-button-hover"));

        // Add mouse hover listeners for player3Button
        player3Button.setOnMouseEntered(e -> player3Button.getStyleClass().add("toggle-button-hover"));
        player3Button.setOnMouseExited(e -> player3Button.getStyleClass().remove("toggle-button-hover"));

        // Add mouse hover listeners for player4Button
        player4Button.setOnMouseEntered(e -> player4Button.getStyleClass().add("toggle-button-hover"));
        player4Button.setOnMouseExited(e -> player4Button.getStyleClass().remove("toggle-button-hover"));

        // Aggiungi i listener alle proprietà selectedProperty dei ToggleButton
        player2Button.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                player2Button.getStyleClass().remove("toggle-button");
                player2Button.getStyleClass().add("toggle-button-pressed");
            } else {
                player2Button.getStyleClass().remove("toggle-button-pressed");
                player2Button.getStyleClass().add("toggle-button");
            }
        });

        player3Button.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                player3Button.getStyleClass().remove("toggle-button");
                player3Button.getStyleClass().add("toggle-button-pressed");
            } else {
                player3Button.getStyleClass().remove("toggle-button-pressed");
                player3Button.getStyleClass().add("toggle-button");
            }
        });

        player4Button.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                player4Button.getStyleClass().remove("toggle-button");
                player4Button.getStyleClass().add("toggle-button-pressed");
            } else {
                player4Button.getStyleClass().remove("toggle-button-pressed");
                player4Button.getStyleClass().add("toggle-button");
            }
        });
    }
}
