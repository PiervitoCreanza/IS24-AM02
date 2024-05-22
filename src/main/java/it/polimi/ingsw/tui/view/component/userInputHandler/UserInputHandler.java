package it.polimi.ingsw.tui.view.component.userInputHandler;

import it.polimi.ingsw.tui.view.component.InputPromptComponent;
import it.polimi.ingsw.tui.view.component.userInputHandler.menu.commands.MenuCommand;

import java.util.ArrayList;

/**
 * This class is responsible for handling user input. It prompts the user for input and validates it.
 * It uses an instance of InputPromptComponent to prompt the user and an instance of UserInputValidator to validate the input.
 */
public class UserInputHandler implements MenuCommand {

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
        if (input != null && validator.validate(input)) {
            this.input = input;
            return true;
        }
        return false;
    }

    /**
     * Handles the user's input.
     *
     * @param input The user's input
     */
    @Override
    public void handleInput(String input) {
        if (!validate(input)) {
            print();
        }
    }

    /**
     * Checks if the input has been filled.
     *
     * @return true if the input has been filled, false otherwise
     */
    @Override
    public boolean isNotWaitingInput() {
        return input != null;
    }

    /**
     * Returns the list of inputs. After the call, the saved input is cleared.
     *
     * @return the list of inputs. In this case it is a single element.
     */
    @Override
    public ArrayList<String> getInputs() {
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add(input);
        // Clear the saved input to accept new user inputs.
        input = null;
        return inputs;
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