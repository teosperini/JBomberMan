package org.jbomberman.view;


import org.jbomberman.controller.MainController;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.jbomberman.utils.BackgroundMusic;

/**
 * This is the class that creates the view of the menu
 * It does not implement Observer/Observable as it does not need
 * to be notified by the model
 */
public class MenuView {

    private final AnchorPane menu = new AnchorPane();
    private Pane mainMenu;
    private Pane leaderboard;
    LeaderboardView leader;

    private MainController controller;

    /**
     * Initializes the menu
     */
    public void initialize() {
        controller = MainController.getInstance();
        leader = new LeaderboardView();
        leaderboard = leader.getLeaderboardPane();
        paneAndButtons();
    }

    /**
     * This method creates all the panes and the buttons in the MenuView
     */
    private void paneAndButtons() {
        Color color = Color.WHITE;
        //################### MAIN MENU ##################//
        mainMenu = ViewUtilities.createPane("JBomberMan", false, true);
        mainMenu.setVisible(true);

        Label mainMenuPlayButton = ViewUtilities.getButton("play", 0, color);

        Label mainMenuLeaderboardButton = ViewUtilities.getButton("leaderboard", 1, color);
        Label mainMenuExitButton = ViewUtilities.getButton("quit", 2, color);

        mainMenuPlayButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            controller.playButtonPressed();
        });

        mainMenuLeaderboardButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            leader.updateScrollPane();
            leaderboard = leader.getLeaderboardPane();
            ViewUtilities.changePane(mainMenu, leaderboard);
        });
        mainMenuExitButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            controller.gameExit();
        });

        mainMenu.getChildren().addAll(mainMenuPlayButton, mainMenuLeaderboardButton, mainMenuExitButton);

        //############### LEADERBOARD ##############//

        leaderboard.setVisible(false);

        Label leaderboardBackButton = ViewUtilities.getButton("back", 4, Color.WHITE);

        leaderboardBackButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            ViewUtilities.changePane(leaderboard, mainMenu);
        });

        leaderboard.getChildren().add(leaderboardBackButton);

        menu.getChildren().addAll(mainMenu, leaderboard);
    }

    /**
     * Returns the menu
     * @return
     */
    public AnchorPane getMenu() {
        return menu;
    }
}