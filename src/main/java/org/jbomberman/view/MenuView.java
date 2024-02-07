package org.jbomberman.view;


import org.jbomberman.controller.MainController;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.jbomberman.utils.BackgroundMusic;

import java.util.Observable;
import java.util.Observer;

public class MenuView implements Observer{

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
        buttons();
    }


    private void buttons() {
        Color color = Color.WHITE;
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
     * Updates the menu
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        /*
        if (arg instanceof UpdateInfo updateInfo) {
            UpdateType updateType = updateInfo.getUpdateType();

            switch (updateType) {
                case PAUSE -> {
                    options.setVisible(true);
                    options.requestFocus();
                }
                case END_PAUSE -> {
                    profile.setVisible(false);
                    options.setVisible(false);
                    menu.requestFocus();
                }
                case GAME_EXIT -> {
                    controller.gameExit();
                }
                case PROFILE_LOADER -> {
                    profile.setVisible(true);
                    profile.requestFocus();
                }
            }
        }

         */
    }

    /**
     * Returns the menu
     * @return
     */
    public AnchorPane getMenu() {
        return menu;
    }
}