package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.view.component.InputRequestComponent;

public class UserInputHandler {
    private final InputRequestComponent inputRequestComponent;
    private final UserInputValidator validator;
    private String input;

    public UserInputHandler(String prompt, UserInputValidator validator) {
        this.validator = validator;
        this.inputRequestComponent = new InputRequestComponent(prompt);
    }

    public boolean validate(String input) {
        if (validator.validate(input)) {
            this.input = input;
            return true;
        }
        return false;
    }

    public String getInput() {
        return input;
    }

    public void print() {
        inputRequestComponent.print();
    }
}
