package it.polimi.ingsw.gui.controllers;

import it.polimi.ingsw.network.client.ClientNetworkControllerMapper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

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

    /**
     * The name of the previously shown scene.
     */
    private ControllersEnum previousLayoutName = ControllersEnum.START;

    protected static ClientNetworkControllerMapper networkControllerMapper;

    /**
     * A flag indicating whether the current layout is active.
     */
    private boolean isCurrentScene = true;

    /**
     * Returns the name of the controller.
     *
     * @return the name of the controller.
     */
    public abstract ControllersEnum getName();

    /**
     * Sets the network controller mapper.
     * This method is used to set the scene when the application starts.
     *
     * @param networkControllerMapper the network controller mapper.
     */
    public static void setNetworkControllerMapper(ClientNetworkControllerMapper networkControllerMapper) {
        Controller.networkControllerMapper = networkControllerMapper;
    }

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
        //TODO: newgame to mainmenu doesnt work
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
     * Returns whether the current scene is active.
     *
     * @return true if the current scene is active, false otherwise.
     */
    public boolean isCurrentScene() {
        return isCurrentScene;
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

            // Set the current scene to false.
            isCurrentScene = false;

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

                // Set the next controller as the current scene.
                nextController.isCurrentScene = true;

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
}