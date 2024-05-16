package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.component.TitleComponent;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.util.ArrayList;

public class JoinGameScene implements Displayable {
    private final DrawArea drawArea;

    private final TUIViewController controller;

    private final ArrayList<UserInputHandler> handlers = new ArrayList<>();
    private int status = 0;

    public JoinGameScene(TUIViewController controller) {
        this.drawArea = new TitleComponent("Join Game").getDrawArea();
        this.controller = controller;
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() {
        drawArea.println();
        handlers.add(new UserInputHandler("Enter game ID:", input -> input.matches("\\d+")));
        handlers.add(new UserInputHandler("Enter your nickname:", input -> !input.isEmpty()));
        handlers.get(status).print();
    }

    public void handleUserInput(String input) {
        if (input.equals("q")) {
            controller.selectScene(ScenesEnum.MAIN_MENU);
            return;
        }
        if (handlers.get(status).validate(input)) {
            status++;
            if (status >= handlers.size()) {
                controller.joinGame(Integer.parseInt(handlers.get(0).getInput()), handlers.get(1).getInput());
                return;
            }
            handlers.get(status).print();
        }
    }
}