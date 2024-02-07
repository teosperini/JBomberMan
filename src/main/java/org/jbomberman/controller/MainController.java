package org.jbomberman.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.jbomberman.model.MainModel;
import org.jbomberman.model.User;
import org.jbomberman.utils.BackgroundMusic;
import org.jbomberman.view.ViewUtilities;
import org.jbomberman.view.GameView;
import org.jbomberman.view.MenuView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.List;

/**
 * The MainController is responsible to handle the interaction of the user
 * with the application being the intermediate between the view and the model
 * The constructor is private due to the class implementing the Singleton pattern
 */
public class MainController {
    // These two constants determinate the size of the game
    public static final int DX=17;
    public static final int DY=12;

    MenuView menuView;
    MainModel model;
    GameView gameView;

    Stage stage;
    Scene scene;

    private Timeline mobMovement;

    private boolean moving = false; // This is to avoid concurrent inputs from the player
    private boolean pause = false; // This is to pause the entire game

    private static MainController instance;

    private MainController() {
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method creates the model and initialize the view of the menu adding the view as an
     * observer for the model.
     * Then it initialize the scene
     */
    public void initialize(){
        model = new MainModel(DX, DY);

        menuView = new MenuView();
        menuView.initialize();

        model.addObserver(menuView);

        Parent root = menuView.getMenu();
        scene = new Scene(root, ViewUtilities.WIDTH, ViewUtilities.HEIGHT);
        stage.setScene(scene);
        stage.show();

        BackgroundMusic.playMenuMusic();
    }


    //################# KEYS AND GAME METHODS ##############//

    /**
     * This method is responsible to handle keyboard events in the game
     * It performs different actions based on the key pressed:
     * - movePlayer, that tells the model to try to move the player in the keyCode direction
     * - releaseBomb and explodeBomb, that respectively release the bomb and explode the bomb
     *    when the timer ends
     * @param keyEvent The KeyEvent representing the key pressed by the user
     */
    public void handleGameKeyEvent(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();

        if (keyEvent.getCode() == KeyCode.TAB) {
            keyEvent.consume();
        } else if (keyCode == KeyCode.ESCAPE){
            pauseController();
        } else if (!pause && !moving){
            if (keyCode == KeyCode.SPACE) {
                if (model.releaseBomb()) {
                    PauseTransition bombTimer = new PauseTransition(Duration.millis(1750));
                    bombTimer.setOnFinished(actionEvent -> model.explodeBomb());
                    bombTimer.play();
                }
            } else {
                model.movePlayer(keyCode);
            }
        }

    }

    /**
     * This method pause the entire game
     */
    public void pauseController() {
        pause = true;
        gameView.pauseView();
        mobMovement.pause();
    }

    /**
     * This method resume the entire game
     */
    public void resumeController() {
        pause = false;
        gameView.resumeView();
        mobMovement.play();
    }

    public void setMoving(boolean bool) {
        moving = bool;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * This is the timeline that controls the movement of the enemies
     */
    private void setTimeline(){
        mobMovement = new Timeline(
                new KeyFrame(Duration.seconds(1), event ->{
                    if (!pause){
                        model.moveEnemies();
                    }
                })
        );
        mobMovement.setCycleCount(Animation.INDEFINITE);
        mobMovement.play();
    }


    //############# LEVEL METHODS ##############//

    /**
     * This method is called when the user presses the Play button, initializing the model,
     * the view of the game, and starting the timeline responsible to move the enemies
     */
    public void playButtonPressed() {
        if (BackgroundMusic.isMenuPlaying()) {
            BackgroundMusic.stopMenuMusic();
        }
        model.initialize();
        model.deleteObservers();

        gameView = new GameView();

        model.addObserver(gameView);
        model.notifyModelReady();

        if (!BackgroundMusic.isPlaying()) {
            BackgroundMusic.playGameMusic();
        } else {
            BackgroundMusic.stopGameMusic();
            BackgroundMusic.playGameMusic();
        }

        scene.setRoot(gameView.getGame());
        gameView.getFocus();

        pause = false;
        moving = false;
        setTimeline();
    }

    /**
     * It is called when the player choose to go to the next level
     */
    public void nextLevel() {
        model.reset();
        model.nextLevel();
        playButtonPressed();
    }

    /**
     * Irreversibly stops the game, preparing it for the after-game Panes
     */
    public void endMatch(){
        if (BackgroundMusic.isPlaying()) {
            BackgroundMusic.stopGameMusic();
        }
        mobMovement.stop();
        pause = true;
    }

    /**
     * Returns to the main menu, resetting the model
     */
    public void quitMatch() {
        if (BackgroundMusic.isPlaying()) {
            BackgroundMusic.stopGameMusic();
        }
        model.save();
        scene.setRoot(menuView.getMenu());
        BackgroundMusic.playMenuMusic();

        model.deleteObservers();
        model.addObserver(menuView);


        model.reset();
        model.resetGame();
    }

    //############## LEADERBOARD ###############//

    /**
     * This method is called when the player insert his name in the text field
     * @param player is the name of the player
     */
    public void newPlayer(String player){
        model.setPlayer(player);
    }

    public List<User> loadLeaderboard() {
        return model.getLeaderboard();
    }

    public void stopMusic(){
        BackgroundMusic.stopGameMusic();
    }
    public void playMusic() {
        BackgroundMusic.playGameMusic();
    }

    //############## CLOSE THE WINDOW #############//

    public void gameExit() {
        stage.close();
    }
    //##################### TEST ####################//

    //TODO remove after test

    public void removeBlocks() {
        model.removeRandom();
    }

    //###############################################//
}