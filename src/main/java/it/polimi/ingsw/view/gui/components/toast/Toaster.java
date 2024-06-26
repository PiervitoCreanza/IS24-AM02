package it.polimi.ingsw.view.gui.components.toast;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * This class manages the display of Toast notifications in the application.
 * It maintains a queue of Toasts to be displayed and ensures that no more than a specified maximum number of Toasts are displayed at once.
 */
public class Toaster {
    /**
     * Logger for this class, used to log debug and error messages.
     */
    private static final Logger logger = LogManager.getLogger(Toaster.class);

    /**
     * Maximum number of toasts that can be displayed at once.
     */
    private static final int MAX_TOASTS = 3;

    /**
     * Singleton instance of the Toaster class.
     */
    private static Toaster instance;

    /**
     * Queue to hold the toasts that are to be displayed.
     */
    private final Queue<Toast> toastQueue = new LinkedList<>();

    /**
     * Set to hold the toasts that are currently being displayed.
     */
    private final Set<Toast> currentlyShownToasts = new HashSet<>();

    /**
     * The ToastArea where the Toasts are displayed.
     */
    private final ToastArea toastArea;

    /**
     * Constructs a new Toaster.
     *
     * @param stage the stage on which the Toasts will be displayed.
     */
    private Toaster(Stage stage) {
        this.toastArea = new ToastArea(stage, this::handleToastExit);
    }

    /**
     * Returns the singleton instance of the Toaster.
     *
     * @param stage the stage on which the Toasts will be displayed.
     * @return the singleton instance of the Toaster.
     */
    public static Toaster getInstance(Stage stage) {
        if (instance == null) {
            instance = new Toaster(stage);
        }
        return instance;
    }

    /**
     * Adds a new Toast to the queue to be displayed.
     *
     * @param toastLevel       the level of the toast. It determines the style of the toast.
     * @param toastTitle       the title of the toast.
     * @param toastDescription the description of the toast.
     */
    public void showToast(ToastLevels toastLevel, String toastTitle, String toastDescription) {
        Toast toast = new Toast(toastLevel, toastTitle, toastDescription);
        toastQueue.add(toast);
        displayNextToastIfPossible();
    }

    /**
     * Displays the next Toast in the queue if the maximum number of Toasts is not currently being displayed.
     */
    private void displayNextToastIfPossible() {
        // If there are less than MAX_TOASTS toasts currently shown and there are toasts in the queue, show the next toast
        if (currentlyShownToasts.size() < MAX_TOASTS && !toastQueue.isEmpty()) {
            Toast nextToast = toastQueue.poll();
            // Show the next toast
            this.toastArea.addToast(nextToast);
            logger.debug("Toast added to toastArea");
            // Add the toast to the list of currently shown toasts
            currentlyShownToasts.add(nextToast);
        }
    }

    /**
     * Removes a Toast from the set of currently displayed Toasts.
     *
     * @param toast the Toast to be removed.
     */
    private void handleToastExit(Toast toast) {
        currentlyShownToasts.remove(toast);
        displayNextToastIfPossible();
    }
}