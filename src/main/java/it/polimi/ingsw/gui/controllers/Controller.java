package it.polimi.ingsw.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Abstract class representing a controller in the application.
 * This class provides the basic functionality for switching layouts, setting properties, and managing the scene.
 */
public abstract class Controller {

    /**
     * The current scene.
     */
    private static Scene scene;
    /**
     * The name of the previously shown scene.
     */
    private ControllersEnum previousLayoutName = ControllersEnum.START;
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
     * This method is called before showing the scene.
     * It should be overridden by the subclasses to perform any necessary operations before showing the scene.
     */
    public abstract void beforeMount();


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
     * Switches the current scene to the specified new scene.
     *
     * @param nextScene the name of the scene to switch to.
     */
    public void switchScene(ControllersEnum nextScene) {
        Platform.runLater(() -> {
            beforeUnmount();
            isCurrentScene = false;

            Parent nextLayout;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/" + nextScene.getFxmlFileName() + ".fxml"));

                nextLayout = loader.load();

                Controller nextController = loader.getController();
                nextController.previousLayoutName = getName();
                nextController.isCurrentScene = true;
                nextController.beforeMount();
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not load " + nextScene + "Layout", e);
            }
            scene.setRoot(nextLayout);
        });
    }

    /**
     * Sets a property in the scene.
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
     * Returns whether the current layout is active.
     *
     * @return true if the current layout is active, false otherwise.
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
     * Sets the scene currently shown.
     * This method is used to set the scene when the application starts.
     *
     * @param scene the scene to set.
     */
    public static void setScene(Scene scene) {
        Controller.scene = scene;
    }
}