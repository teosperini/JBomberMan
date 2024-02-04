package org.jbomberman.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.jbomberman.model.MainModel;
import org.jbomberman.model.User;
import org.jbomberman.utils.BackgroundMusic;
import org.jbomberman.utils.Difficulty;
import org.jbomberman.utils.SceneManager;
import org.jbomberman.view.GameView;
import org.jbomberman.view.MenuView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;


public class MainController {
    public static final int DX=17;
    public static final int DY=12;
    MenuView menuView;
    MainModel model;
    GameView gameView;


    Stage stage;
    Scene scene;

    private Timeline mobMovement = new Timeline();

    private boolean moving = false;
    private boolean pause = false;

    private static MainController instance;
    private Difficulty difficulty = Difficulty.NORMAL;

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


    public void initialize(){
        //creazione model
        model = new MainModel(DX, DY);
        model.setLevel(1);
        //difficulty = Difficulty.NORMAL;
        //model.setDifficulty(difficulty);

        //creazione menu
        menuView = new MenuView();
        menuView.initialize();

        model.addObserver(menuView);

        Parent root = menuView.getMenu();
        scene = new Scene(root, SceneManager.WIDTH, SceneManager.HEIGHT);
        stage.setScene(scene);
        stage.show();
        System.out.println("la larghezza dello stage è " + stage.getWidth());
    }

    //################ NEW GAME ################//

    public void newPlayer(String player){
        model.setPlayer(player);
    }
    public ArrayList<User> loadLeaderboard() {
        // TODO caricherà i dati dal model quando ci sarà il file json
        //  alla fine di ogni partita sarà richiesto di inserire il nome (altrimenti "guest")
        //  e verrà salvato insieme al punteggio raggiunto e al livello raggiunto (completato)
        return model.getLeaderboard();
    }

    //HANDLING OF THE KEY-EVENTS IN GAME

    public void handleGameKeyEvent(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        //il tasto tab fa perdere il focus alla gameBoard, quindi l'ho escluso
        if (keyEvent.getCode() == KeyCode.TAB) {
            keyEvent.consume();
        } else if (keyCode == KeyCode.ESCAPE){
            pauseController();
        } else if (!pause && !moving){
            if (keyCode == KeyCode.SPACE) {
                // if space is pressed we try to release a bomb
                if (model.releaseBomb()) {
                    // start a timer that at the end explode the bomb
                    PauseTransition bombTimer = new PauseTransition(Duration.millis(1750));
                    bombTimer.setOnFinished(actionEvent -> model.explodeBomb());
                    bombTimer.play();
                }
            } else {
                model.movePlayer(keyCode);
            }
        }

    }
    public void pauseController() {
        pause = true;
        gameView.pauseView();
        mobMovement.pause();
    }

    public void resumeController() {
        pause = false;
        gameView.resumeView();
        mobMovement.play();
    }

    public void moving(boolean bool) {
        moving = bool;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

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


    //irreversibly stops the game
    public void endMatch(){
        if (BackgroundMusic.isPlaying()) {
            BackgroundMusic.stopMusic();
        }
        mobMovement.stop();
        pause = true;
    }

    //returns to the main menu
    public void quitMatch() {
        model.save();
        scene.setRoot(menuView.getMenu());

        model.deleteObservers();
        model.addObserver(menuView);


        model.reset();
        model.setLevel(1);
        model.resetGame();
    }

    public void playButtonPressed() {
        // preparing the model
        model.initialize();

        //deleting all the previous observers
        model.deleteObservers();

        //preparing the view
        gameView = new GameView();
        model.addObserver(gameView);
        model.notifyModelReady();
        gameView.initialize();

        if (!BackgroundMusic.isPlaying()) {
            BackgroundMusic.playMusic();
        } else {
            BackgroundMusic.stopMusic();
            BackgroundMusic.playMusic();
        }

        //setting the scene
        scene.setRoot(gameView.getGame());
        gameView.getFocus();

        //setting the controller
        pause = false;
        moving = false;
        setTimeline();
    }

    public void restart(){
        model.reset();
        model.setLevel(1);
        model.resetGame();
        playButtonPressed();
    }

    public void nextLevel(){
        model.reset();
        model.setLevel(2);
        playButtonPressed();
    }

    public void stopMusic(){
        BackgroundMusic.stopMusic();
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

    public void setNick(String nickname) {
        model.setShownNickname(nickname);
    }

    //###############################################//
}