package it.polimi.ingsw.gui.controllers.InitScene;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.gui.components.GuiCardFactory;
import it.polimi.ingsw.gui.controllers.ControllersEnum;
import it.polimi.ingsw.model.card.gameCard.GameCard;
import it.polimi.ingsw.model.utils.Coordinate;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.util.Objects;

/**
 * Controller class for the Init Place Starter Card scene in the GUI application.
 * Handles the initialization, user interactions, and communication with the server.
 */
public class InitPlaceStarterCardSceneController extends InitScene {

    private static final ControllersEnum NAME = ControllersEnum.INIT_PLACE_STARTER_CARD;

    private static final Logger logger = LogManager.getLogger(InitPlaceStarterCardSceneController.class);

    @FXML
    private StackPane frontImagePane;

    @FXML
    private StackPane backImagePane;

    @FXML
    private ImageView cardFrontImageView;

    @FXML
    private ImageView cardBackImageView;

    private GameCard gameCard;
    private String selectedSide = ""; // Variable to track the selected side

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets up event listeners for user interactions with card sides.
     */
    @FXML
    public void initialize() {
        // Call the default initialization method
        super.initialize();

        // Add listeners to the card sides
        frontImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedSide.equals("front")) {
                handleFrontImageClick();
            }
        });

        backImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedSide.equals("back")) {
                handleBackImageClick();
            }
        });
    }

    private void handleBackImageClick() {
        selectedSide = "back"; // Update the selected side
        frontImagePane.getStyleClass().remove("selected-card-image");
        backImagePane.getStyleClass().add("selected-card-image");
    }

    private void handleFrontImageClick() {
        selectedSide = "front"; // Update the selected side
        backImagePane.getStyleClass().remove("selected-card-image");
        frontImagePane.getStyleClass().add("selected-card-image");
    }

    /**
     * Loads the starter card into the scene.
     *
     * @param gameCard The GameCard object representing the starter card.
     */
    private void loadCard(GameCard gameCard) {
        this.gameCard = gameCard;
        cardFrontImageView.setImage(GuiCardFactory.createFrontImage(gameCard.getCardId()));
        cardBackImageView.setImage(GuiCardFactory.createBackImage(gameCard.getCardId()));

        // Set the default side to the front
        handleBackImageClick();
    }

    /**
     * Action method called when the user continues after selecting the card side.
     * Sends the selected side to the server and logs the action.
     */
    @FXML
    protected void continueAction() {
        // Print the selected card ID and the selected side to the console
        logger.debug("Selected Card ID: " + gameCard.getCardId() + ", Side: " + selectedSide);
        // Reset the styles after continuing
        backImagePane.getStyleClass().remove("selected-card-image");
        frontImagePane.getStyleClass().remove("selected-card-image");

        // Send the selected side to the server
        networkControllerMapper.placeCard(new Coordinate(0, 0), gameCard.getCardId(), Objects.equals(selectedSide, "back"));
    }

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
     * Called before mounting (showing) the scene.
     * Loads the starter card based on the current game state.
     *
     * @param evt The PropertyChangeEvent describing the change event.
     */
    @Override
    public void beforeMount(PropertyChangeEvent evt) {
        // Load the starter card
        String playerName = getProperty("playerName");
        GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
        GameCard starterCard = gameControllerView.getPlayerViewByName(playerName).starterCard();
        logger.debug("Loading card id: {}", starterCard.getCardId());
        loadCard(starterCard);
    }

    /**
     * Called before unmounting (switching away from) the scene.
     * Currently does nothing for this scene.
     */
    @Override
    public void beforeUnmount() {
        // No action needed before unmounting
    }

    /**
     * Handles property change events.
     * Specifically switches to the Init Set Pion scene when the game status changes to INIT_CHOOSE_PLAYER_COLOR.
     *
     * @param evt The PropertyChangeEvent describing the change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("UPDATE_VIEW")) {
            GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_PLAYER_COLOR) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_PION, evt));
            }
        }
    }
}