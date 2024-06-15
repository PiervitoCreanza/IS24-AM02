package it.polimi.ingsw.tui.view.component.userInputHandler;

/**
 * This is a functional interface that represents a validator for user input.
 * It contains a single method, validate, that takes a String as input and returns a boolean.
 * The validate method should return true if the input is valid, and false otherwise.
 */
@FunctionalInterface
public interface UserInputValidator {

    /**
     * Validates the provided input.
     *
     * @param input The input to validate
     * @return true if the input is valid, false otherwise
     */
    boolean validate(String input);
}