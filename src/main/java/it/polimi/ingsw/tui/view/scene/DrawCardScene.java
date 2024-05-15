package it.polimi.ingsw.tui.view.scene;

import it.polimi.ingsw.tui.controller.TUIViewController;
import it.polimi.ingsw.tui.view.drawer.DrawArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DrawCardScene implements Diplayable, UserInputScene {
    private final DrawArea drawArea;
    private final TUIViewController controller;

    public PlaceCardScene(TUIViewController controller) {
        this.controller = controller;
        this.drawArea = new DrawArea();
        // TODO: Implement Interface
    }

    /**
     * This method is used to display the object.
     */
    @Override
    public void display() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(drawArea);

    }
}
