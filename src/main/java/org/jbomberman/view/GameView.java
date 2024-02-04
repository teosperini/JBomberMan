package org.jbomberman.view;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.jbomberman.controller.MainController;
import org.jbomberman.utils.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

import static org.jbomberman.utils.SceneManager.*;

public class GameView implements Observer {

    private final MainController controller;

    public final AnchorPane gameBoard;

    //END GAME PANELS
    Pane gameContinue;
    Pane continueVictory;
    Pane gameOver;
    Pane victory;

    //PAUSE PANELS
    Pane pause;
    Pane options;

    //IMAGE VIEWS
    ImageView player;
    ImageView puBomb;
    ImageView puLife;
    ImageView puInvincible;
    ImageView exit;

    //ARRAYS
    private final List<ImageView> randomBlocks;
    private final List<ImageView> enemies;
    private final List<ImageView> coins;
    private final List<ImageView> bombExplosion;

    //BOTTOM BAR
    private final HBox bottomBar = new HBox();
    Label livesLabel;
    Label pointsLabel;
    Label timerLabel;
    Label nameLabel;

    //Label to show the score in the gameOver pane
    Label deathPointsLabel;

    private int level;
    private String nickname;

    private boolean isOn = false;

    //IMAGES
    private enum BlockImage {
        //bomb is the real bomb, fire is the power_up
        BEDROCK("definitive/static_block.png"),
        BEDROCK2("definitive/static_block2.png"),
        STONE("definitive/random_block.png"),
        STONE2("definitive/random_block2.png"),
        GRASS("definitive/background_green.png"),
        GRASS2("definitive/background_grey.png"),
        BOMBERMAN("definitive/bomberman.png"),
        DOOR("definitive/exit.png"),
        BOMB("bomb/bomb.gif"),
        ENEMY_LEFT("definitive/enemyLeft.png"),
        ENEMY_RIGHT("definitive/enemyRight.png"),
        ENEMY_DOWN("definitive/enemyDown.png"),
        ENEMY_UP("definitive/enemyUp.png"),
        ENEMY_2_LEFT("definitive/angryEnemyLeft.png"),
        ENEMY_2_RIGHT("definitive/angryEnemyRight.png"),
        ENEMY_2_DOWN("definitive/angryEnemyDown.png"),
        ENEMY_2_UP("definitive/angryEnemyUp.png"),
        FIRE("power_up/bomb.png"),
        LIFE("power_up/oneup.png"),
        INVINCIBLE("power_up/resistance.png"),
        COIN("power_up/coin.gif"),
        DESTRUCTION("random_blocks/blocks.gif"),
        ;

        private final Image image;

        BlockImage(String path) {
            image = new Image(Objects.requireNonNull(BlockImage.class.getResourceAsStream(path)));
        }

        public Image getImage() {
            return image;
        }
    }

    //############### CONSTRUCTOR AND INITIALIZE ################//
    public GameView() {
        controller = MainController.getInstance();
        gameBoard = new AnchorPane();
        randomBlocks = new ArrayList<>();
        enemies = new ArrayList<>();
        coins = new ArrayList<>();
        bombExplosion = new ArrayList<>();
        addBottomBar();
        genInGamePanels();
    }

    public void initialize() {
        gameBoard.setOnKeyPressed(controller::handleGameKeyEvent);
    }

    //####################### PANELS #######################//
    private void genInGamePanels() {
        //################# PAUSE ################//
        pause = SceneManager.createPane("Pause", true, false);
        pause.setVisible(false);

        Label pauseResumeButton = SceneManager.getButton("resume", 0, Color.WHITE);
        Label pauseOptionsButton = SceneManager.getButton("options", 1, Color.WHITE);
        Label pauseRestartButton = SceneManager.getButton("restart", 2, Color.WHITE);
        Label pauseExitButton = SceneManager.getButton("menu", 3, Color.WHITE);

        pauseResumeButton.setOnMouseClicked(mouseEvent -> controller.resumeController());
        pauseOptionsButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(pause,options));
        pauseRestartButton.setOnMouseClicked(mouseEvent -> restart());
        pauseExitButton.setOnMouseClicked(mouseEvent -> {
            controller.endMatch();
            controller.quitMatch();
        });

        pause.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
                controller.resumeController();
                keyEvent.consume();
            }
        });

        pause.getChildren().addAll(pauseResumeButton, pauseOptionsButton, pauseRestartButton, pauseExitButton);

        //################# OPTIONS ################//
        options = SceneManager.createPane("Options", true, false);
        options.setVisible(false);

        Label optionsStopMusicButton = SceneManager.getButton("stop music", 1, Color.WHITE);
        Label optionsBackButton = SceneManager.getButton("back", 2, Color.WHITE);

        optionsStopMusicButton.setOnMouseClicked(mouseEvent -> {
            controller.stopMusic();
            SceneManager.changePane(options,pause);
        });
        optionsBackButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(options, pause));

        options.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
                SceneManager.changePane(options,pause);
                keyEvent.consume();
            }
        });

        options.getChildren().addAll(optionsStopMusicButton, optionsBackButton);

        //################# GAME CONTINUE ################//

        gameContinue = SceneManager.createPane("Save your results", true,false);
        gameContinue.setVisible(false);

        TextField textField = new TextField();
        gameContinue.getChildren().addAll(textField);

        setCentred(textField);

        int maxLength = 8;

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                if (change.isAdded() && change.getControlNewText().length() > maxLength) {
                    return null; // Ignora il cambiamento se supera il limite
                }
                return change;
            });

        textField.setTextFormatter(textFormatter);

        String string = "nickname";
        textField.setPromptText(string);

        if (textField.isFocused()) {
                textField.setPromptText("");
            }
            else {
                textField.setPromptText(string);
            }

        textField.setOnKeyPressed(keyEvent -> {
                ImageView imageView = new ImageView(new Image(GameView.class.getResourceAsStream("definitive/ok.png")));
                imageView.setLayoutX(textField.getLayoutX()-SCALE_FACTOR-5);
                imageView.setLayoutY((double) SceneManager.HEIGHT / 2 - (double) SCALE_FACTOR /2);
                imageView.setFitHeight(SCALE_FACTOR);
                imageView.setFitWidth(SCALE_FACTOR);
                if (keyEvent.getCode().equals(KeyCode.ENTER)){
                    nickname = textField.getText();
                    controller.newPlayer(nickname);
                    textField.clear();
                    gameContinue.requestFocus();
                    gameContinue.getChildren().add(imageView);
                    controller.setNick(nickname);
                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
                    pauseTransition.setOnFinished(event->controller.quitMatch());
                    pauseTransition.play();
                }
            });

        //TODO se il giocatore ricomincia di propria volontà, non gli faccio salvare il
        // punteggio, altrimenti si, sia che perda sia che vinca

        //################## GAME OVER #################//
        gameOver = SceneManager.createPane("Game Over", true,false);
        gameOver.setVisible(false);

        Label gameOverContinue = SceneManager.getButton("continue", 2, Color.WHITE);

        gameOverContinue.setOnMouseClicked(mouseEvent -> {
            //gameOver.getChildren().remove(deathPointsLabel);
            SceneManager.changePane(gameOver, gameContinue);
        });

        deathPointsLabel = new Label();
        setCentred(deathPointsLabel);
        deathPointsLabel.setStyle("-fx-text-fill: white;");
        deathPointsLabel.setFont(SceneManager.CUSTOM_FONT_SMALL);

        gameOver.getChildren().addAll(gameOverContinue, deathPointsLabel);

        //################## VICTORY ###################//
        continueVictory = SceneManager.createPane("continue", true, false);
        continueVictory.setVisible(false);

        victory = SceneManager.createPane("Victory", true, false);
        victory.setVisible(false);

        Label victoryNextLevelButton = SceneManager.getButton("nextLevel", 1, Color.WHITE);
        Label victoryExitButton = SceneManager.getButton("menu", 3, Color.WHITE);
        Label victoryContinueButton = SceneManager.getButton("continue", 2, Color.WHITE);

        victoryNextLevelButton.setOnMouseClicked(mouseEvent -> controller.nextLevel());
        victoryExitButton.setOnMouseClicked(mouseEvent -> controller.quitMatch());
        victoryContinueButton.setOnMouseClicked(mouseEvent -> SceneManager.changePane(victory, gameContinue));

        victory.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
                controller.quitMatch();
            }
        });
        if (level == 1) {
            victory.getChildren().addAll(victoryNextLevelButton, victoryExitButton);
        }
        else
            victory.getChildren().add(victoryContinueButton);


        //################## GAMEBOARD ################//
        gameBoard.getChildren().addAll(pause, options , gameOver, victory, gameContinue );
    }

    private void restart() {
        controller.restart();
    }


    //#################### BOTTOM BAR ################//
    private void addBottomBar() {
        // build the bottomBar

        bottomBar.setLayoutX(0);
        bottomBar.setLayoutY((double)SCALE_FACTOR * (MainController.DY-1));
        bottomBar.setPrefHeight(SCALE_FACTOR);
        bottomBar.setPrefWidth(SceneManager.WIDTH);
        bottomBar.setStyle("-fx-background-color: grey");

        // build the labels
        livesLabel = new Label();
        pointsLabel = new Label();
        timerLabel = new Label("Tempo: 0");
        nameLabel = new Label();

        nameLabel.setFont(SceneManager.CUSTOM_FONT_SMALL);
        nameLabel.setTextFill(Color.BLACK);

        livesLabel.setFont(SceneManager.CUSTOM_FONT_SMALL);
        livesLabel.setTextFill(Color.BLACK);

        pointsLabel.setFont(SceneManager.CUSTOM_FONT_SMALL);
        pointsLabel.setTextFill(Color.BLACK);
        //pointsLabel.setAlignment(Pos.CENTER_RIGHT);

        nameLabel.setText("player: guest");
        HBox.setMargin(nameLabel, new Insets(0,0,0,20));

        //##################### TEST ####################//
        //TODO remove after test

        Button buttonBlocks = new Button();
        buttonBlocks.setOnMouseClicked(mouseEvent -> {
            controller.removeBlocks();
            mouseEvent.consume();
            gameBoard.toFront();
            gameBoard.requestFocus();
        });
        buttonBlocks.setLayoutX(40);
        buttonBlocks.setLayoutY(20);

        bottomBar.getChildren().addAll(livesLabel, buttonBlocks, pointsLabel);
        //###############################################//

        gameBoard.getChildren().add(bottomBar);
    }

    private void updateLife(int index){
        livesLabel.setText("Lives: " + index);
    }

    private void updatePoints(int totalPoints, int currentPoints, Coordinate coordinate){
        String points = "Points: " + totalPoints;
        pointsLabel.setText(points);
        deathPointsLabel.setText(points);

        Label text = SceneManager.getPoints(Integer.toString(currentPoints), coordinate);
        gameBoard.getChildren().add(text);
        text.setVisible(true);
        text.toFront();

        TranslateTransition transition = new TranslateTransition(Duration.millis(700), text);
        transition.setByY(-30);

        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(900), text);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(actionEvent -> gameBoard.getChildren().remove(text));

        ParallelTransition parallelTransition  = new ParallelTransition(transition, fadeOutTransition);
        parallelTransition.play();
    }


    //####################### GETTER #######################//
    public AnchorPane getGame() {
        return gameBoard;
    }

    public void getFocus() {
        gameBoard.setVisible(true);
        gameBoard.toFront();
        gameBoard.requestFocus();
    }

    @Override
    public void update(Observable ignored, Object arg) {
        if (arg instanceof UpdateInfo updateInfo) {
            UpdateType updateType = updateInfo.getUpdateType();

            switch (updateType) {
                case LEVEL -> level = updateInfo.getIndex();

                case LOAD_MAP -> {
                    switch (updateInfo.getSubBlock()) {
                        case GROUND_BLOCKS -> {
                            if (level == 1)
                                loader(updateInfo.getArray(), BlockImage.GRASS.getImage());
                            else
                                loader(updateInfo.getArray(), BlockImage.GRASS2.getImage());
                        }

                        case STATIC_BLOCKS -> {
                            if (level == 1)
                                loader(updateInfo.getArray(), BlockImage.BEDROCK.getImage());
                            else
                                loader(updateInfo.getArray(), BlockImage.BEDROCK2.getImage());
                        }

                        case RANDOM_BLOCKS -> {
                            if (level == 1)
                                updateInfo.getArray().forEach(coordinate -> drawImageView(coordinate, BlockImage.STONE.getImage(), randomBlocks));
                            else
                                updateInfo.getArray().forEach(coordinate -> drawImageView(coordinate, BlockImage.STONE2.getImage(), randomBlocks));
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + updateInfo.getIndex());
                    }
                }
                case LOAD_POINTS -> {
                    pointsLabel.setText("Points: "+updateInfo.getIndex());
                    deathPointsLabel.setText("Points: "+updateInfo.getIndex());
                }

                case LOAD_LIFE -> livesLabel.setText("Lives: " + updateInfo.getIndex());

                case LOAD_ENEMIES -> updateInfo.getArray().forEach(coordinate -> drawImageView(coordinate, BlockImage.ENEMY_DOWN.getImage(), enemies));

                case LOAD_COINS -> updateInfo.getArray().forEach(coordinate -> drawImageView(coordinate, BlockImage.COIN.getImage(), coins));


                case LOAD_PLAYER -> player = loadItems(updateInfo.getCoordinate(), BlockImage.BOMBERMAN.getImage());

                case LOAD_EXIT -> exit = loadItems(updateInfo.getCoordinate(), BlockImage.DOOR.getImage());

                case LOAD_POWER_UP_LIFE -> puLife = loadItems(updateInfo.getCoordinate(), BlockImage.LIFE.getImage());

                case LOAD_POWER_UP_BOMB -> puBomb = loadItems(updateInfo.getCoordinate(), BlockImage.FIRE.getImage());

                case LOAD_POWER_UP_INVINCIBLE -> puInvincible = loadItems(updateInfo.getCoordinate(), BlockImage.INVINCIBLE.getImage());

                case LOAD_NAME -> {
                    if (updateInfo.getNickname() != null)
                        nameLabel.setText("player: "+ updateInfo.getNickname());
                }

                case UPDATE_BLOCK_DESTROYED -> {
                    ImageView imageView = removeImageView(randomBlocks, updateInfo.getIndex());
                    runBlockDestructionAnimation(imageView);
                    //runBlockDestructionAnimation(new Coordinate((int)imageView.getLayoutX()/SCALE_FACTOR, (int)imageView.getLayoutY()/SCALE_FACTOR));
                }

                case UPDATE_ENEMY_DEAD -> removeImageView(enemies, updateInfo.getIndex());

                case UPDATE_COINS -> {
                    removeImageView(coins, updateInfo.getIndex());
                    BackgroundMusic.playCoin();
                }

                case UPDATE_DOOR -> {
                    BackgroundMusic.playDoor();
                    runOpeningDoorAnimation();
                }

                case UPDATE_POSITION -> position(updateInfo.getNewCoord(), updateInfo.getOldCoord(), updateInfo.getIndex(), updateInfo.getKeyCode(), updateInfo.getBoo());

                case UPDATE_RESPAWN -> respawn(updateInfo.getIndex());


                case UPDATE_POINTS -> updatePoints(updateInfo.getIndex(), updateInfo.getIndex2(), updateInfo.getCoordinate());

                case UPDATE_PU_LIFE -> doLifePowerUp(updateInfo.getIndex());


                case UPDATE_PU_BOMB -> doBombPowerUp();

                case UPDATE_PU_INVINCIBLE -> doInvinciblePowerUp(updateInfo.getBoo());

                case UPDATE_BOMB_RELEASED -> {
                    BackgroundMusic.playBomb();
                    drawBomb(updateInfo.getCoordinate());
                }

                case UPDATE_EXPLOSION -> playExplosionAnimation(updateInfo.getTriadArrayList());

                case UPDATE_ENEMY_LIFE -> {
                    ImageView woundedEnemy = enemies.get(updateInfo.getIndex());
                    woundedEnemy.setImage(BlockImage.ENEMY_2_LEFT.getImage());
                }

                case UPDATE_GAME_WIN -> gameWin();

                case UPDATE_GAME_OVER -> gameLost();

                default -> throw new IllegalStateException("Unexpected value: " + updateType);
            }
        }
    }

    private ImageView loadItems(Coordinate c, Image image) {
        ImageView item = createImageView(c, image);
        gameBoard.getChildren().add(item);
        return item;
    }


    private void gameLost() {
        updateLife(0);
        controller.endMatch();
        BackgroundMusic.playDeath();
        PauseTransition pauseGameOver = new PauseTransition(Duration.millis(400));
        pauseGameOver.setOnFinished(event -> {
            gameOver.toFront();
            gameOver.setVisible(true);
            gameOver.requestFocus();
        });
        pauseGameOver.play();
    }

    private void gameWin() {
        controller.endMatch();
        BackgroundMusic.playSuccess();
        PauseTransition pauseGameWin = new PauseTransition(Duration.millis(400));
        pauseGameWin.setOnFinished(event -> {
            victory.toFront();
            victory.setVisible(true);
            victory.requestFocus();
        });
        pauseGameWin.play();
    }
    //#################### ANIMATION AND MOVEMENT ##################//

    private void position(Coordinate newC, Coordinate oldC, int entity, KeyCode keyCode, boolean lastLife) {
        int oldX = oldC.x() * SCALE_FACTOR;
        int oldY = oldC.y() * SCALE_FACTOR;
        int newX = newC.x() * SCALE_FACTOR;
        int newY = newC.y() * SCALE_FACTOR;

        TranslateTransition transition = new TranslateTransition();
        if (oldX != newX){
            transition.setByX((double)newX-oldX);
        }else{
            transition.setByY((double)newY-oldY);
        }

        if (entity < 0) {
            controller.moving(true);
            transition.setDuration(Duration.millis(200));
            transition.setOnFinished(event ->
                    controller.moving(false)
            );
            transition.setNode(player);
        } else {
            if (level == 1 || !lastLife) {
                switch (keyCode) {
                    case LEFT -> enemies.get(entity).setImage(BlockImage.ENEMY_LEFT.getImage());
                    case RIGHT -> enemies.get(entity).setImage(BlockImage.ENEMY_RIGHT.getImage());
                    case DOWN -> enemies.get(entity).setImage(BlockImage.ENEMY_DOWN.getImage());
                    case UP -> enemies.get(entity).setImage(BlockImage.ENEMY_UP.getImage());
                }
            } else {
                switch (keyCode) {
                    case LEFT -> enemies.get(entity).setImage(BlockImage.ENEMY_2_LEFT.getImage());
                    case RIGHT -> enemies.get(entity).setImage(BlockImage.ENEMY_2_RIGHT.getImage());
                    case DOWN -> enemies.get(entity).setImage(BlockImage.ENEMY_2_DOWN.getImage());
                    case UP -> enemies.get(entity).setImage(BlockImage.ENEMY_2_UP.getImage());
                }
            }
            transition.setDuration(Duration.millis(600));
            transition.setNode(enemies.get(entity));
        }
        transition.play();
    }

    private void respawn(int index) {
        controller.moving(true);
        controller.setPause(false);
        PauseTransition pauseRespawn = getPauseRespawn();
        updateLife(index);
        pauseRespawn.play();
    }

    private PauseTransition getPauseRespawn() {
        PauseTransition pauseRespawn = new PauseTransition(Duration.millis(200));
        pauseRespawn.setOnFinished(event -> {
            player.setTranslateX(0);
            player.setTranslateY(0);
            controller.moving(false);
            controller.setPause(false);
        });
        return pauseRespawn;
    }


    //##################### PAUSE ####################//
    public void pauseView(){
        pause.toFront();
        pause.setVisible(true);
        pause.requestFocus();
    }

    public void resumeView() {
        SceneManager.changePane(pause,gameBoard);
    }


    //########################## POWER UPS ########################//
    private void doLifePowerUp(int index) {
        BackgroundMusic.playOneUp();
        updateLife(index);
        powerUPs(puLife);
    }

    private void doBombPowerUp(){
        BackgroundMusic.playBigBomb();
        powerUPs(puBomb);
    }

    private void doInvinciblePowerUp(boolean boo) {
        if (boo) {
            player.setOpacity(0.5);
            powerUPs(puInvincible);
        }else {
            player.setOpacity(1);
        }
    }

    private void powerUPs(ImageView imageView) {
        PauseTransition removePU = new PauseTransition(Duration.millis(200));
        removePU.setOnFinished(event -> gameBoard.getChildren().remove(imageView));
        removePU.play();

        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        HBox.setMargin(imageView, new Insets(5, 0, 0, 10));
        bottomBar.getChildren().add(imageView);
    }


    //###################### IMAGEVIEW METHODS ######################//

    private void drawImageView(Coordinate coordinate, Image image, List<ImageView> entities) {
        ImageView imageView = createImageView(coordinate, image);
        entities.add(imageView);
        gameBoard.getChildren().add(imageView);
    }

    ImageView currentTntImage = null;
    private void drawBomb(Coordinate coordinate) {
        currentTntImage = createImageView(coordinate, BlockImage.BOMB.getImage());
        gameBoard.getChildren().add(currentTntImage);
        player.toFront();
    }

    public void removeBomb() {
        gameBoard.getChildren().remove(currentTntImage);
        currentTntImage = null;
    }
//TODO quando si crea l'animazione, togliere l'immagine e rimetterla ogni volta è scomodo, sarebbe più comodo se cambiassimo immagine ogni volta;
// alla fine poi si toglie l'immagine, però appunto, perchè toglierla e rimetterla ogni volta?
    private ImageView removeImageView(List<ImageView> imglist, int index) {
        ImageView imgView = imglist.remove(index);
        gameBoard.getChildren().remove(imgView);
        return imgView;
    }

    private void runOpeningDoorAnimation(){
        runAnimation(exit, 1,14, "doors");
    }

    /**
     * Runs the block destruction animation at the given coordinates
     * @param
     */
    private void runBlockDestructionAnimation(ImageView imageView){
        runAnimation(imageView, 1, 6, "random_blocks");
    }

    private void runAnimation(ImageView imageView, int index, int end, String path) {
        imageView.setImage(new Image(Objects.requireNonNull(GameView.class.getResourceAsStream( path + "/" + index + ".png"))));
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(160));
        pauseTransition.setOnFinished(event -> {
            if (index < end) runAnimation(imageView, index + 1, end, path);
        });
        pauseTransition.play();
    }

    private void playExplosionAnimation(ArrayList<Triad> triadArrayList) {
        removeBomb();
        playExplosionAnimation(triadArrayList, 1);
    }

    private void playExplosionAnimation(ArrayList<Triad> triadArrayList, int i) {
        String path = "explosion/" + i;
        triadArrayList.forEach(triad -> {
            ImageView imageView;
            if (triad.getDirection().equals(Direction.CENTER)) {
                imageView = createImageView(triad.getCoordinate(), new Image(Objects.requireNonNull(GameView.class.getResourceAsStream( path + "/center.png"))));
            } else if (triad.isLast()) {
                imageView = createImageView(triad.getCoordinate(), new Image(Objects.requireNonNull(GameView.class.getResourceAsStream(path +"/" + triad.getDirection().getKeyCode() + "_external.png"))));
            } else {
                imageView = createImageView(triad.getCoordinate(), new Image(Objects.requireNonNull(GameView.class.getResourceAsStream(path +"/" + triad.getDirection().getKeyCode() + ".png"))));
            }
            bombExplosion.add(imageView);
            gameBoard.getChildren().add(imageView);
        });

        int j = i + 1;
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(150));
        pauseTransition.setOnFinished(event -> {
            removeExplosion();
            if (j < 4) playExplosionAnimation(triadArrayList, j);
        });
        pauseTransition.play();
    }

    private void removeExplosion() {
        bombExplosion.forEach(imageView -> gameBoard.getChildren().remove(imageView));
    }

    private void loader(ArrayList<Coordinate> array, Image image) {
        array.forEach(coordinate -> gameBoard.getChildren().add(createImageView(coordinate, image)));
    }
}