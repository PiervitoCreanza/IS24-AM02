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

// Each player receives a single starterCard at the beginning of the game, and it has to choose which side to use.
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

    @FXML
    public void initialize() {
        // Call the default initialization method
        super.initialize();

        // Add listeners to the card sides
        frontImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedSide.equals("front")) {
                handleBackImageClick();
            }
        });

        backImagePane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !selectedSide.equals("back")) {
                handleFrontImageClick();
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

    private void loadCard(GameCard gameCard) {
        this.gameCard = gameCard;
        cardFrontImageView.setImage(GuiCardFactory.createFrontImage(gameCard.getCardId()));
        cardBackImageView.setImage(GuiCardFactory.createBackImage(gameCard.getCardId()));

        // Set the default side to the front
        handleBackImageClick();
    }

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
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
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
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    @Override
    public void beforeUnmount() {

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
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("UPDATE_VIEW")) {
            GameControllerView gameControllerView = (GameControllerView) evt.getNewValue();
            if (gameControllerView.gameStatus() == GameStatusEnum.INIT_CHOOSE_PLAYER_COLOR) {
                Platform.runLater(() -> switchScene(ControllersEnum.INIT_SET_PION, evt));
            }
        }
    }
}
