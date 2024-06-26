package it.polimi.ingsw.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for displaying GitHub repository information in a list cell.
 * This class is used with JavaFX to control the labels that display the name
 * and URL of a GitHub repository.
 */
public class GitHubListCellController {

    /**
     * The label that displays the name of the GitHub repository.
     */
    @FXML
    private Label githubNameLabel;

    /**
     * The label that displays the URL of the GitHub repository.
     */
    @FXML
    private Label githubUrlLabel;

    /**
     * Sets the GitHub repository information in the labels.
     *
     * @param name The name of the GitHub repository.
     * @param url  The URL of the GitHub repository.
     */
    public void setGitHubInfo(String name, String url) {
        githubNameLabel.setText(name);
        githubUrlLabel.setText(url);
    }
}