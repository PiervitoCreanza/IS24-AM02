package it.polimi.ingsw.view.tui.commandLine;

import it.polimi.ingsw.view.tui.controller.TUIViewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * This class is responsible for reading user input from the console.
 * It reads the input and passes it to the controller for processing.
 * It extends the Thread class to read input asynchronously.
 */
public class CLIReader extends Thread {

    /**
     * The logger.
     */
    private static final Logger logger = Logger.getLogger(CLIReader.class.getName());

    /**
     * The controller that handles the user input.
     */
    private final TUIViewController controller;

    /**
     * This constructor is used to create a new CLIReader.
     *
     * @param controller The controller that handles the user input.
     */
    public CLIReader(TUIViewController controller) {
        this.controller = controller;
    }

    /**
     * This method is used to read user input from the console.
     * It reads the input and passes it to the controller for processing.
     * It runs in a loop until the thread is interrupted.
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (!this.isInterrupted()) {
                line = reader.readLine();
                controller.handleUserInput(line);
            }
        } catch (IOException e) {
            logger.severe("Error reading from console: " + e.getMessage());
            System.exit(1);
        }
    }
}