package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.view.component.InputRequestComponent;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The UserInputScene interface provides methods for getting and validating user input.
 */
public interface UserInputScene {

    /**
     * Gets and validates user input.
     * It prompts the user with a message, reads the user's input, and validates it using the provided validator.
     * If the user enters "q", it returns null.
     * If the user's input is not valid, it prompts the user again until valid input is entered.
     *
     * @param prompt    the prompt message to display to the user
     * @param validator the validator to use for validating the user's input
     * @param reader    the BufferedReader to use for reading the user's input
     * @return the user's input if it is valid, or null if the user entered "q"
     * @throws IOException if an I/O error occurs
     */
    static String getAndValidateInput(String prompt, UserInputValidator validator, BufferedReader reader) throws IOException {
        String input;
        InputRequestComponent inputRequestComponent = new InputRequestComponent(prompt);
        do {
            inputRequestComponent.print();
            input = reader.readLine();
            if ("q".equals(input)) {
                return null;
            }
        } while (!validator.validate(input));
        return input;
    }

    /**
     * Gets and validates user input.
     * It reads the user's input and validates it using the provided validator.
     * If the user enters "q", it returns null.
     * If the user's input is not valid, it prompts the user again until valid input is entered.
     *
     * @param validator the validator to use for validating the user's input
     * @param reader    the BufferedReader to use for reading the user's input
     * @return the user's input if it is valid, or null if the user entered "q"
     * @throws IOException if an I/O error occurs
     */
    static String getAndValidateInput(UserInputValidator validator, BufferedReader reader) throws IOException {
        String input;
        boolean firstAttempt = true;
        do {
            if (!firstAttempt) {
                System.err.println("Invalid input.");
            }
            input = reader.readLine();
            if ("q".equals(input)) {
                return null;
            }
            firstAttempt = false;
        } while (!validator.validate(input));
        return input;
    }
}