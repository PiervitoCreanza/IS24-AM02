package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.view.component.InputPromptComponent;

/**
 * This class is responsible for handling user input. It prompts the user for input and validates it.
 * It uses an instance of InputPromptComponent to prompt the user and an instance of UserInputValidator to validate the input.
 */
public class UserInputHandler {
    /**
     * Component used to request input from the user.
     */
    private final InputPromptComponent inputRequestComponent;
    /**
     * Validator used to validate the user's input.
     */
    private final UserInputValidator validator;
    /**
     * The user's input after it has been validated.
     */
    private String input;

    /**
     * Constructs a new UserInputHandler with the specified prompt and validator.
     *
     * @param prompt    The prompt to display to the user
     * @param validator The validator to use for validating the user's input
     */
    public UserInputHandler(String prompt, UserInputValidator validator) {
        this.validator = validator;
        this.inputRequestComponent = new InputPromptComponent(prompt);
    }

    /**
     * Validates the specified input using the validator.
     * If the input is valid, it is stored in the 'input' field and the method returns true.
     * If the input is not valid, the method returns false and the 'input' field is not updated.
     *
     * @param input The input to validate
     * @return true if the input is valid, false otherwise
     */
    public boolean validate(String input) {
        if (validator.validate(input)) {
            this.input = input;
            return true;
        }
        return false;
    }

    /**
     * Returns the user's input after it has been validated.
     *
     * @return The user's input
     */
    public String getInput() {
        return input;
    }

    /**
     * Prints the input request to the user.
     */
    public void print() {
        inputRequestComponent.print();
    }
}