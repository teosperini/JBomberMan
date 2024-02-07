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

import static org.jbomberman.view.ViewUtilities.*;

public class GameView implements Observer {

    private final MainController controller; // The instance of the controller

    private final AnchorPane gameBoard; // The master pane, where the all the game components
                                        // are placed
    //END GAME PANES
    Pane gameContinue; // This Pane is shown when saving the player name (both death and win)
    Pane gameOver; // The game over pane
    Pane gameVictory; // The game victory pane

    //PAUSE PANES
    Pane pause; // The pause pane
    Pane options; // The options pane

    //IMAGE VIEWS
    ImageView player; // The player
    ImageView puBomb; // The larger explosion power up
    ImageView puLife; // The 1-up power up
    ImageView puInvincible; // The 10 seconds invincibility power up
    ImageView exit; // The exit door

    // List of ImageViews
    /// Each list has a correspondent List of Coordinates in the model
    private final List<ImageView> randomBlocks; // The List of the random blocks
    private final List<ImageView> enemies; // The list of the enemies
    private final List<ImageView> coins; // The list of the coins

    /// This List is for the ImageViews of the explosion (to easily remove them after the explosion)
    private final List<ImageView> bombExplosion;

    //BOTTOM BAR
    private final HBox bottomBar = new HBox();
    Label livesLabel;
    Label pointsLabel;

    //Label to show the score in the gameOver pane
    Label deathPointsLabel;
    Label victoryPointsLabel;

    private int level;
    private String nickname;

    private ImageView selectedImage;
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
        COIN("power_up/coin.gif")
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
        createGamePanels();
        initializeKeyPressed();
    }

    public void initializeKeyPressed() {
        // The event handler for the pressed keys
        gameBoard.setOnKeyPressed(controller::handleGameKeyEvent);
    }

    //####################### PANELS #######################//

    /**
     * This method initialize:
     * - The pause Pane
     * - The options Pane
     * - The gameContinue Pane
     * - The gameOver Pane
     * - The gameVictory Pane
     * and all of their buttons (except for the buttons of gameVictory, those are chosen at runtime)
     */
    private void createGamePanels() {
        //################# PAUSE ################//
        // Initializing the pause Pane
        pause = ViewUtilities.createPane("Pause", true, false);
        pause.setVisible(false);

        // Declaring and initializing the pause buttons
        Label pauseResumeButton = ViewUtilities.getButton("resume", 0, Color.WHITE);
        Label pauseOptionsButton = ViewUtilities.getButton("options", 1, Color.WHITE);
        Label pauseExitButton = ViewUtilities.getButton("menu", 2, Color.WHITE);

        // Mouse events handling
        pauseResumeButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            controller.resumeController();
        });
        pauseOptionsButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            ViewUtilities.changePane(pause,options);
        });
        pauseExitButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            ViewUtilities.changePane(pause, gameContinue);
        });

        // Keyboard events handling
        pause.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
                controller.resumeController();
                keyEvent.consume();
            }
        });

        //Adding the buttons to the pane
        pause.getChildren().addAll(pauseResumeButton, pauseOptionsButton, pauseExitButton);

        //################# OPTIONS ################//
        // Initializing the options pane
        options = ViewUtilities.createPane("Options", true, false);
        options.setVisible(false);

        // Declaring and initializing the options buttons
        Label optionsStopMusicButton = ViewUtilities.getButton("stop/resume music", 1, Color.WHITE);
        Label optionsBackButton = ViewUtilities.getButton("back", 2, Color.WHITE);

        // Mouse events handling
        optionsStopMusicButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            if (BackgroundMusic.isPlaying()) {
                controller.stopMusic();
            } else {
                controller.playMusic();
            }
            ViewUtilities.changePane(options, pause);
        });
        optionsBackButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            ViewUtilities.changePane(options, pause);
        });

        // Keyboard events handling
        options.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)){
                ViewUtilities.changePane(options,pause);
                keyEvent.consume();
            }
        });

        options.getChildren().addAll(optionsStopMusicButton, optionsBackButton);

        //################# GAME CONTINUE ################//
        // Initializing the gameContinue pane
        gameContinue = ViewUtilities.createPane("Save your results", true,false);
        gameContinue.setVisible(false);

        // Initializing the textField of the gameContinue, where the user will be able to write his name
        TextField textField = new TextField();
        gameContinue.getChildren().addAll(textField);

        setCentred(textField);


        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
                if (change.isAdded() && change.getControlNewText().length() > MAX_NAME_LETTERS) {
                    return null; // Ignore the change if it exceeds the limit
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
                // Creating the
                ImageView imageView = new ImageView(new Image(Objects.requireNonNull(GameView.class.getResourceAsStream("definitive/ok.png"))));
                imageView.setLayoutX(textField.getLayoutX()-SCALE_FACTOR-5);
                imageView.setLayoutY((double) ViewUtilities.HEIGHT / 2 - (double) SCALE_FACTOR /2);
                imageView.setFitHeight(SCALE_FACTOR);
                imageView.setFitWidth(SCALE_FACTOR);

                if (keyEvent.getCode().equals(KeyCode.ENTER)){
                    nickname = textField.getText();
                    controller.newPlayer(nickname);
                    textField.setStyle("-fx-text-fill: gray;");
                    gameContinue.requestFocus();
                    gameContinue.getChildren().add(imageView);
                    PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
                    pauseTransition.setOnFinished(event->controller.quitMatch());
                    pauseTransition.play();
                }
            });


        //################## GAME OVER #################//
        gameOver = ViewUtilities.createPane("Game Over", true,false);
        gameOver.setVisible(false);

        Label gameOverContinue = ViewUtilities.getButton("Continue", 2, Color.WHITE);

        gameOverContinue.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            ViewUtilities.changePane(gameOver, gameContinue);
        });

        deathPointsLabel = new Label();

        deathPointsLabel.setStyle("-fx-text-fill: white;");
        deathPointsLabel.setFont(ViewUtilities.CUSTOM_FONT_SMALL);

        gameOver.getChildren().addAll(gameOverContinue, deathPointsLabel);


        //################## VICTORY ###################//
        gameVictory = ViewUtilities.createPane("Victory", true, false);
        gameVictory.setVisible(false);


        //################## GAMEBOARD ################//
        gameBoard.getChildren().addAll(pause, options , gameOver, gameVictory, gameContinue );
    }


    //#################### BOTTOM BAR ################//

    private void addBottomBar() {
        // Building the bottomBar
        bottomBar.setLayoutX(0);
        bottomBar.setLayoutY((double)SCALE_FACTOR * (MainController.DY-1));
        bottomBar.setPrefHeight(SCALE_FACTOR);
        bottomBar.setPrefWidth(ViewUtilities.WIDTH);
        bottomBar.setStyle("-fx-background-color: grey");

        // Building the labels
        livesLabel = new Label();
        pointsLabel = new Label();

        livesLabel.setFont(ViewUtilities.CUSTOM_FONT_SMALL);
        livesLabel.setTextFill(Color.BLACK);

        pointsLabel.setFont(ViewUtilities.CUSTOM_FONT_SMALL);
        pointsLabel.setTextFill(Color.BLACK);


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
        // Updating both the in-game points label and the post-game points label
        // to avoid inconsistencies
        String points = "Points: " + totalPoints;
        pointsLabel.setText(points);
        deathPointsLabel.setText(points);

        Label text = ViewUtilities.getFloatingLabel(Integer.toString(currentPoints), coordinate);
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
                                loader(updateInfo.getBlocks(), BlockImage.GRASS.getImage());
                            else
                                loader(updateInfo.getBlocks(), BlockImage.GRASS2.getImage());
                        }

                        case STATIC_BLOCKS -> {
                            if (level == 1)
                                loader(updateInfo.getBlocks(), BlockImage.BEDROCK.getImage());
                            else
                                loader(updateInfo.getBlocks(), BlockImage.BEDROCK2.getImage());
                        }

                        case RANDOM_BLOCKS -> {
                            if (level == 1)
                                updateInfo.getBlocks().forEach(coordinate -> drawImageView(coordinate, BlockImage.STONE.getImage(), randomBlocks));
                            else
                                updateInfo.getBlocks().forEach(coordinate -> drawImageView(coordinate, BlockImage.STONE2.getImage(), randomBlocks));
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + updateInfo.getIndex());
                    }
                }
                case LOAD_POINTS -> {
                    pointsLabel.setText("Points: "+updateInfo.getIndex());
                    deathPointsLabel.setText("Points: "+updateInfo.getIndex());
                }

                case LOAD_LIFE -> livesLabel.setText("Lives: " + updateInfo.getIndex());

                case LOAD_ENEMIES -> updateInfo.getEntities().forEach(coordinate -> drawImageView(coordinate, BlockImage.ENEMY_DOWN.getImage(), enemies));

                case LOAD_COINS -> updateInfo.getEntities().forEach(coordinate -> drawImageView(coordinate, BlockImage.COIN.getImage(), coins));


                case LOAD_PLAYER -> player = loadItems(updateInfo.getCoordinate(), BlockImage.BOMBERMAN.getImage());

                case LOAD_EXIT -> exit = loadItems(updateInfo.getCoordinate(), BlockImage.DOOR.getImage());

                case LOAD_POWER_UP_LIFE -> puLife = loadItems(updateInfo.getCoordinate(), BlockImage.LIFE.getImage());

                case LOAD_POWER_UP_BOMB -> puBomb = loadItems(updateInfo.getCoordinate(), BlockImage.FIRE.getImage());

                case LOAD_POWER_UP_INVINCIBLE -> puInvincible = loadItems(updateInfo.getCoordinate(), BlockImage.INVINCIBLE.getImage());

                case UPDATE_BLOCK_DESTROYED -> runBlockDestructionAnimation(removeImageView(randomBlocks, updateInfo.getIndex()));


                case UPDATE_ENEMY_DEAD -> {
                    removeImageView(enemies, updateInfo.getIndex());
                    BackgroundMusic.playEnemyDeath();
                }

                case UPDATE_COINS -> {
                    removeImageView(coins, updateInfo.getIndex());
                    BackgroundMusic.playCoin();
                }

                case UPDATE_DOOR -> {
                    BackgroundMusic.playDoor();
                    runOpeningDoorAnimation();
                }

                case UPDATE_POSITION -> position(updateInfo.getNewCoord(), updateInfo.getOldCoord(), updateInfo.getIndex(), updateInfo.getKeyCode(), updateInfo.isEnemyLastLife());

                case UPDATE_RESPAWN -> respawn(updateInfo.getIndex());


                case UPDATE_POINTS -> updatePoints(updateInfo.getPoints(), updateInfo.getEarnedPoints(), updateInfo.getCoordinate());

                case UPDATE_PU_LIFE -> doLifePowerUp(updateInfo.getIndex());


                case UPDATE_PU_BOMB -> doBombPowerUp();

                case UPDATE_PU_INVINCIBLE -> doInvinciblePowerUp(updateInfo.isInvincible());

                case UPDATE_BOMB_RELEASED -> {
                    BackgroundMusic.playBomb();
                    drawBomb(updateInfo.getCoordinate());
                }

                case UPDATE_EXPLOSION -> playExplosionAnimation(updateInfo.getTriadList());

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
        BackgroundMusic.playLost();
        PauseTransition pauseGameOver = new PauseTransition(Duration.millis(400));
        pauseGameOver.setOnFinished(event -> {
            setCentred(deathPointsLabel);
            gameOver.toFront();
            gameOver.setVisible(true);
            gameOver.requestFocus();
        });
        pauseGameOver.play();
    }

    private void gameWin() {
        updateVictoryPane();
        controller.endMatch();
        BackgroundMusic.playSuccess();
        PauseTransition pauseGameWin = new PauseTransition(Duration.millis(400));
        pauseGameWin.setOnFinished(event -> {
            setCentred(victoryPointsLabel);
            gameVictory.toFront();
            gameVictory.setVisible(true);
            gameVictory.requestFocus();
        });
        pauseGameWin.play();
    }

    private void updateVictoryPane() {
        victoryPointsLabel = new Label();
        victoryPointsLabel.setStyle("-fx-text-fill: white;");
        victoryPointsLabel.setFont(ViewUtilities.CUSTOM_FONT_SMALL);

        Label victoryNextLevelButton = ViewUtilities.getButton("nextLevel", 1, Color.WHITE);
        Label victoryExitButton = ViewUtilities.getButton("menu", 2, Color.WHITE);
        Label victoryContinueButton = ViewUtilities.getButton("Continue", 2, Color.WHITE);

        victoryNextLevelButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            controller.nextLevel();
        });
        victoryExitButton.setOnMouseClicked(mouseEvent -> {
            BackgroundMusic.playClick();
            ViewUtilities.changePane(gameVictory, gameContinue);
        });
        victoryContinueButton.setOnMouseClicked(mouseEvent ->{
            BackgroundMusic.playClick();
            ViewUtilities.changePane(gameVictory, gameContinue);
        });

        if (level == 1) {
            gameVictory.getChildren().addAll(victoryNextLevelButton, victoryExitButton);
        }
        else {
            gameVictory.getChildren().addAll(victoryPointsLabel, victoryContinueButton);
        }
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
        BackgroundMusic.playDeath();
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
        ViewUtilities.changePane(pause,gameBoard);
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
            BackgroundMusic.playInvincible();
            player.setOpacity(0.5);
            powerUPs(puInvincible);
        }else {
            BackgroundMusic.stopInvincible();
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

    private void playExplosionAnimation(List<Triad> triadArrayList) {
        removeBomb();
        playExplosionAnimation(triadArrayList, 1);
    }

    private void playExplosionAnimation(List<Triad> triadArrayList, int i) {
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

    private void loader(List<Coordinate> array, Image image) {
        array.forEach(coordinate -> gameBoard.getChildren().add(createImageView(coordinate, image)));
    }
}