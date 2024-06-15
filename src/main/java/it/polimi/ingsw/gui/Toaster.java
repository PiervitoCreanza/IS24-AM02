package it.polimi.ingsw.gui;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Toaster {
    private static final Logger logger = LogManager.getLogger(Toaster.class);
    private static final int MAX_TOASTS = 3;
    private static Toaster instance;
    private final Stage stage;
    private final Queue<InfoBox> toastQueue = new LinkedList<>();
    private final Deque<InfoBox> currentlyShownToasts = new ArrayDeque<>();
    private final ToastArea toastArea;


    private Toaster(Stage stage) {
        this.stage = stage;
        this.toastArea = new ToastArea(stage, this::removeToast);

    }

    public static Toaster getInstance(Stage stage) {
        if (instance == null) {
            instance = new Toaster(stage);
        }
        return instance;
    }

    private void displayNextToastIfPossible() {
        // If there are less than MAX_TOASTS toasts currently shown and there are toasts in the queue, show the next toast
        if (currentlyShownToasts.size() < MAX_TOASTS && !toastQueue.isEmpty()) {
            InfoBox lastToast = currentlyShownToasts.peekLast();
            InfoBox nextToast = toastQueue.poll();
            // Show the next toast
            this.toastArea.addToast(nextToast);
            logger.debug("Toast added to toastArea");
            // Add the toast to the list of currently shown toasts
            currentlyShownToasts.addLast(nextToast);
        }
    }

    public void showToast(String color, String toastTitle, String toastDescription) {
        InfoBox toast = new InfoBox(color, toastTitle, toastDescription);
        toastQueue.add(toast);
        displayNextToastIfPossible();
    }

    public void removeToast(InfoBox toast) {
        double toastHeight = toast.getToastNode().getBoundsInParent().getHeight();
        currentlyShownToasts.remove(toast);
        displayNextToastIfPossible();
    }
}
