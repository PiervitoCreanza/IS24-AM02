package it.polimi.ingsw.tui.view.scene;

@FunctionalInterface
public interface UserInputValidator {
    boolean validate(String input);
}
