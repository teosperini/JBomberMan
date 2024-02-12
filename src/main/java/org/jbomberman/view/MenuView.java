package org.jbomberman.view;


import org.jbomberman.controller.MainController;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.jbomberman.utils.BackgroundMusic;

import static org.jbomberman.view.ViewUtilities.*;

/**
 * This is the class that creates the view of the menu
 * It does not implement Observer/Observable as it does not need
 * to be notified by the model
 */
public class MenuView extends View implements ViewUtilities{

    private final AnchorPane menu = new AnchorPane();
    private Pane mainMenu;
    private Pane leaderboard;
    LeaderboardView leader;

    private MainController controller;

    public MenuView(){
        initializeView();
    }

    /**
     * Initializes the menu
     */
    public void initializeView() {
        controller = MainController.getInstance();
        leader = new LeaderboardView();
        leaderboard = leader.getLeaderboardPane();
        createPanes();
    }

    /**
     * This method creates all the panes and the buttons in the MenuView
     */
    protected void createPanes() {
        Color color = Color.WHITE;
        //################### MAIN MENU ##################//
        mainMenu = createPane("JBomberMan", false, true);
        mainMenu.setVisible(true);

        Label mainMenuPlayButton = getButton("play", 0, color);

        Label mainMenuLeaderboardButton = getButton("leaderboard", 1, color);
        Label mainMenuExitButton = getButton("quit", 2, color);

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

        Label leaderboardBackButton = getButton("back", 4, Color.WHITE);

        leaderboardBackButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            changePane(leaderboard, mainMenu);
        });

        leaderboard.getChildren().add(leaderboardBackButton);

        menu.getChildren().addAll(mainMenu, leaderboard);
    }

    /**
     * Returns the menu
     * @return
     */
    public AnchorPane getRoot() {
        return menu;
    }
}