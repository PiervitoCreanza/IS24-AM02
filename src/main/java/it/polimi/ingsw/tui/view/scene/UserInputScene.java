package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.view.component.InputRequestComponent;

import java.io.BufferedReader;
import java.io.IOException;

public interface UserInputScene {
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
}
