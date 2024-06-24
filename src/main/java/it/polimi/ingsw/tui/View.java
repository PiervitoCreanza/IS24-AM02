package it.polimi.ingsw.tui;


/**
 * The ViewController interface represents the view controller in the MVC pattern.
 * It extends the PropertyChangeListener interface to listen for changes in the model.
 */
public interface View {
    /**
     * This method is used to launch the user interface.
     */
    void launchUI();
}