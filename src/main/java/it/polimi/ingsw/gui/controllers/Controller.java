package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.controller.GameStatusEnum;
import it.polimi.ingsw.gui.components.ErrorDialog;
import it.polimi.ingsw.gui.components.toast.ToastLevels;
import it.polimi.ingsw.gui.components.toast.Toaster;
import it.polimi.ingsw.gui.utils.GUIUtils;
import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import it.polimi.ingsw.network.virtualView.GameControllerView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import static it.polimi.ingsw.gui.utils.GUIUtils.capitalizeFirstLetter;

/**
 * Abstract class representing a controller in the application.
 * This class provides the basic functionality for switching layouts, setting properties, and managing the scene.
 */
public abstract class Controller implements PropertyChangeListener {

    /**
     * The current scene.
     */
    private static Scene scene;

    /**
     * The current stage.
     */
    private static Stage stage;

    protected static ErrorDialog alert;
    private static final Logger logger = LogManager.getLogger(Controller.class);
    /**
     * The name of the previously shown scene.
     */
    private ControllersEnum previousLayoutName = ControllersEnum.START;

    protected static ClientNetworkControllerMapper networkControllerMapper = ClientNetworkControllerMapper.getInstance();

    private static boolean isClassAlreadyInstantiated = false;
    private static boolean showConnectedPlayers = true;
    protected static final ObservableSet<String> connectedPlayers = FXCollections.observableSet();

    // This static block is used to initialize the connectedPlayers listener only once.
    // The Controller class is extended multiple times, but the listener should be added only once.
    {
        if (!isClassAlreadyInstantiated) {
            isClassAlreadyInstantiated = true;
            // This listener gets notified when a player connects or disconnects.
            connectedPlayers.addListener((SetChangeListener<String>) change -> {
                // If the connected players updates should not be shown, return.
                if (!showConnectedPlayers) return;

                if (change.wasAdded()) {
                    logger.debug("Player connected: {}", change.getElementAdded());
                    showToast(ToastLevels.SUCCESS, "Player connected", capitalizeFirstLetter(GUIUtils.truncateString(change.getElementAdded())) + " has just joined the game.");
                } else if (change.wasRemoved()) {
                    logger.debug("Player disconnected: {}", change.getElementRemoved());
                    showToast(ToastLevels.ERROR, "Player disconnected", capitalizeFirstLetter(GUIUtils.truncateString(change.getElementRemoved())) + " has just left the game.");
                }
            });
        }
    }

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    public abstract ControllersEnum getName();

    /**
     * This method is called before switching to a new scene.
     * It should be overridden by the subclasses to perform any necessary operations before switching to a new scene.
     */
    public abstract void beforeUnmount();

    /**
     * Returns the name of the previous layout.
     *
     * @return the name of the previous layout.
     */
    public ControllersEnum getPreviousLayoutName() {
        return previousLayoutName;
    }

    /**
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     * If the switchScene was caused by a property change, the event is passed as an argument.
     *
     * @param evt the property change event that caused the switch.
     */
    public abstract void beforeMount(PropertyChangeEvent evt);

    /**
     * Switches the current scene to the specified new scene.
     *
     * @param nextScene     the name of the scene to switch to.
     * @param previousEvent the event that caused the switch if any.
     */
    public void switchScene(ControllersEnum nextScene, PropertyChangeEvent previousEvent) {
        privateSwitchScene(nextScene, previousEvent);
    }

    /**
     * Switches the current scene to the specified new scene.
     *
     * @param nextScene the name of the scene to switch to.
     */
    public void switchScene(ControllersEnum nextScene) {
        privateSwitchScene(nextScene, null);
    }

    /**
     * Sets a property shared among all controllers.
     *
     * @param property the name of the property.
     * @param value    the value of the property.
     */
    public void setProperty(String property, Object value) {
        Platform.runLater(() -> getScene().getProperties().put(property, value));
    }


    /**
     * Returns the value of a property in the scene.
     *
     * @param property the name of the property.
     * @param <T>      the type of the property.
     * @return the value of the property.
     */
    public <T> T getProperty(String property) {
        return (T) getScene().getProperties().get(property);
    }

    /**
     * Returns the current scene.
     *
     * @return the scene.
     */
    protected Scene getScene() {
        return scene;
    }

    /**
     * Gets the stage currently shown.
     * This method is used to set the stage when the application starts.
     *
     * @return the stage currently shown.
     */
    protected Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage currently shown.
     * This method is used to set the stage when the application starts.
     *
     * @param stage the stage to set.
     */
    public static void setStage(Stage stage) {
        Controller.stage = stage;
    }

    protected void showErrorPopup(String errorType, String message, boolean isAutoCloseable) {
        Platform.runLater(() -> {
            alert = new ErrorDialog(getStage(), Alert.AlertType.ERROR, errorType, message, isAutoCloseable);
            alert.showAndWait();
        });
    }

    private void showConnectionErrorPopup(String message) {
        Platform.runLater(() -> {
            logger.debug("Showing connection error popup");
            if (alert == null || !alert.isShowing()) {
                alert = new ErrorDialog(getStage(), Alert.AlertType.WARNING, "Connection error", message, false);
                alert.getButtonTypes().clear();
                alert.addButton("Quit", event -> System.exit(0));
                alert.addButton("Retry", event -> networkControllerMapper.connect());
                alert.show();
            }
            alert.setContentText(message);
        });
    }

    private void closeAlert() {
        Platform.runLater(() -> {
            if (alert != null) {
                alert.closeAlert();
            }
        });
    }

    /**
     * Sets the scene currently shown.
     * This method is used to set the scene when the application starts.
     *
     * @param scene the scene to set.
     */
    public static void setScene(Scene scene) {
        Controller.scene = scene;
    }

    private void privateSwitchScene(ControllersEnum nextScene, PropertyChangeEvent previousEvent) {
        Platform.runLater(() -> {
            // Call the beforeUnmount method of the current controller.
            // This method is used to perform actions right before the window is unmounted.
            beforeUnmount();

            // Disconnect from the network controller mapper.
            networkControllerMapper.removePropertyChangeListener(this);

            Parent nextLayout;
            try {
                // Load the next scene.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/" + nextScene.getFxmlFileName() + ".fxml"));
                nextLayout = loader.load();

                // Get the next scene controller.
                Controller nextController = loader.getController();

                // Set the previous layout name on the next controller.
                nextController.previousLayoutName = getName();

                // Connect the nextController to the network controller mapper.
                networkControllerMapper.addPropertyChangeListener(nextController);

                // Call the beforeMount method of the next controller.
                // This is used to perform actions right before the window is shown.
                // For example subscribing to network events.
                nextController.beforeMount(previousEvent);
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not load " + nextScene + "Layout", e);
            }

            // Display the next scene.
            scene.setRoot(nextLayout);
        });
    }

    /**
     * Shows a toast message.
     *
     * @param level   the level of the toast message.
     * @param title   the title of the toast message.
     * @param message the message of the toast message.
     */
    public void showToast(ToastLevels level, String title, String message) {
        Platform.runLater(() -> {
            Toaster.getInstance(getStage()).showToast(level, title, message);
        });
    }

    /**
     * Sets the flag indicating whether the connected players should be shown.
     *
     * @param showConnectedPlayers the flag indicating whether the connected players should be shown.
     */
    public void setShowConnectedPlayers(boolean showConnectedPlayers) {
        Controller.showConnectedPlayers = showConnectedPlayers;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Property change event received: {}", evt.getPropertyName());
        switch (evt.getPropertyName()) {
            case "ERROR" -> {
                logger.debug("Error notification received: {}", evt.getNewValue());
                showErrorPopup("Server Error", (String) evt.getNewValue(), false);
            }
            case "CONNECTION_ESTABLISHED" -> {
                logger.debug("Connection established notification received");
                closeAlert();
                showToast(ToastLevels.SUCCESS, "Connected", (String) evt.getNewValue());
                if (getName() != ControllersEnum.MAIN_MENU) {
                    switchScene(ControllersEnum.MAIN_MENU);
                }
            }
            case "CONNECTION_FAILED" -> {
                logger.debug("Connection failed notification received");
                showConnectionErrorPopup((String) evt.getNewValue());
            }
            case "GAME_DELETED" -> {
                logger.debug("Game deleted notification received");
                Platform.runLater(() -> networkControllerMapper.closeConnection());
                showToast(ToastLevels.ERROR, "Game deleted", (String) evt.getNewValue());
            }
            case "UPDATE_VIEW" -> {
                GameControllerView updatedView = (GameControllerView) evt.getNewValue();
                logger.debug("Update view notification received");

                // Sync connected players
                connectedPlayers.retainAll(updatedView.getConnectedPlayers());
                connectedPlayers.addAll(updatedView.getConnectedPlayers());

                // Show error popup if the game is paused
                if (updatedView.gameStatus() == GameStatusEnum.GAME_PAUSED) {
                    showErrorPopup("Game Paused", "You are the only online player. The game has been paused.", true);
                }
            }
        }
    }
}