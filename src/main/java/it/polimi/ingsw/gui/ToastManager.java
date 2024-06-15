package it.polimi.ingsw.gui;

import it.polimi.ingsw.gui.dataStorage.ObservableQueue;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

public class ToastManager {
    private static final Logger logger = LogManager.getLogger(ToastManager.class);
    private static final int MAX_TOASTS = 3;
    private static ToastManager instance;
    private final Stage stage;
    private final ObservableQueue<InfoBox> toastQueue = new ObservableQueue<>();
    private final Deque<InfoBox> currentlyShownToasts = new ArrayDeque<>();

    private ToastManager(Stage stage) {
        this.stage = stage;
        toastQueue.setListener(new ObservableQueue.QueueChangeListener<InfoBox>() {
            @Override
            public void elementAdded(InfoBox element) {
                displayNextToastIfPossible();
            }
        });
    }

    public static ToastManager getInstance(Stage stage) {
        if (instance == null) {
            instance = new ToastManager(stage);
        }
        return instance;
    }

    private void displayNextToastIfPossible() {
        InfoBox nextToast = toastQueue.peek();
        if (currentlyShownToasts.size() < MAX_TOASTS && nextToast != null) {
            InfoBox lastToast = currentlyShownToasts.peekLast();
            nextToast.showBoxBelow(lastToast, this::removeToast);
            toastQueue.poll();
            currentlyShownToasts.addLast(nextToast);
        }
    }

    public void showToast(String color, String toastTitle, String toastDescription) {
        InfoBox toast = new InfoBox(stage, color, toastTitle, toastDescription);
        toastQueue.add(toast);
    }

    public void removeToast(InfoBox toast) {
        double toastHeight = toast.getToastNode().getBoundsInParent().getHeight();
        currentlyShownToasts.remove(toast);
        // Move all remaining toasts up
        for (InfoBox remainingToast : currentlyShownToasts) {
            // TODO: Make a transition if possible
            remainingToast.setY(remainingToast.getY() - toastHeight);
        }
        displayNextToastIfPossible();
    }
}
