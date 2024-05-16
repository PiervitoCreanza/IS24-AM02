package it.polimi.ingsw.tui.commandLine;

import it.polimi.ingsw.tui.controller.TUIViewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class CLIReader extends Thread {

    private static final Logger logger = Logger.getLogger(CLIReader.class.getName());
    private final TUIViewController controller;

    public CLIReader(TUIViewController controller) {
        this.controller = controller;
    }

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