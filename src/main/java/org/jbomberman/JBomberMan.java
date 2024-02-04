package org.jbomberman;

import org.jbomberman.controller.MainController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JBomberMan extends Application {

    public Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        this.stage = stage;
        Image icon = new Image(JBomberMan.class.getResourceAsStream("view/bomb/bomb_2.png"));
        stage.getIcons().add(icon);

        stage.setResizable(true);
        stage.setTitle("JBomberMan");

        MainController controller = MainController.getInstance();

        controller.setStage(stage);
        controller.initialize();
    }
}