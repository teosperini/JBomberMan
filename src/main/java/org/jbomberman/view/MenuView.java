package org.jbomberman.view;


import org.jbomberman.controller.MainController;
import org.jbomberman.utils.Difficulty;
import org.jbomberman.utils.SceneManager;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

public class MenuView implements Observer{

    private final AnchorPane menu = new AnchorPane();
    private Pane mainMenu;
    private Pane options;
    private Pane leaderboard;
    LeaderboardView leader;
    Pane difficulty;

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
        mainMenu = SceneManager.createPane("JBomberMan", false, true);
        mainMenu.setVisible(true);

        Label mainMenuPlayButton = SceneManager.getButton("play", 0, color);
        Label mainMenuOptionsButton = SceneManager.getButton("options", 1, color);
        Label mainMenuLeaderboardButton = SceneManager.getButton("leaderboard", 2, color);
        Label mainMenuExitButton = SceneManager.getButton("quit", 3, color);

        mainMenuPlayButton.setOnMouseClicked(mouseEvent -> controller.playButtonPressed());
        mainMenuOptionsButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(mainMenu, options));
        mainMenuLeaderboardButton.setOnMouseClicked(mouseEvent -> {
            leader.updateScrollPane();
            leaderboard = leader.getLeaderboardPane();
            SceneManager.changePane(mainMenu, leaderboard);
        });
        mainMenuExitButton.setOnMouseClicked(mouseEvent -> controller.gameExit());

        mainMenu.getChildren().addAll(mainMenuPlayButton, mainMenuOptionsButton, mainMenuLeaderboardButton, mainMenuExitButton);

        //################# OPTIONS #################//
        options = SceneManager.createPane("Options", false, false);
        options.setVisible(false);

        Label optionsDifficultyButton = SceneManager.getButton("difficulty",1, color);
        Label optionsBackButton = SceneManager.getButton("back", 3, color);

        optionsDifficultyButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(options,difficulty));
        optionsBackButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(options, mainMenu));

        options.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                SceneManager.changePane(options, mainMenu);
            }
        });

        options.getChildren().addAll(optionsDifficultyButton, optionsBackButton);

        //############# DIFFICULTY ################//
        difficulty = SceneManager.createPane("Difficulty", false, false);

        difficulty.setVisible(false);

        Label difficultyEasy = SceneManager.getButton("easy", 0, Color.WHITE);
        Label difficultyNormal = SceneManager.getButton("normal", 1, Color.WHITE);
        Label difficultyHard = SceneManager.getButton("hard", 2, Color.WHITE);
        Label difficultyBackButton = SceneManager.getButton("back", 3, Color.WHITE);

        difficultyEasy.setOnMouseClicked(mouseEvent -> {
            controller.setDifficulty(Difficulty.EASY);
            SceneManager.changePane(difficulty, options);
        });
        difficultyNormal.setOnMouseClicked(mouseEvent -> {
            controller.setDifficulty(Difficulty.NORMAL);
            SceneManager.changePane(difficulty, options);
        });
        difficultyHard.setOnMouseClicked(mouseEvent -> {
            controller.setDifficulty(Difficulty.HARD);
            SceneManager.changePane(difficulty, options);
        });

        difficultyBackButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(difficulty, options));

        difficulty.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
                SceneManager.changePane(difficulty,options);
                keyEvent.consume();
            }
        });

        difficulty.getChildren().addAll(difficultyEasy,difficultyNormal,difficultyHard,difficultyBackButton);

        //############### LEADERBOARD ##############//

        leaderboard.setVisible(false);

        Label leaderboardBackButton = SceneManager.getButton("back", 4, Color.WHITE);

        leaderboardBackButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(leaderboard, mainMenu));

        leaderboard.getChildren().add(leaderboardBackButton);

        menu.getChildren().addAll(mainMenu, options, leaderboard, difficulty);
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