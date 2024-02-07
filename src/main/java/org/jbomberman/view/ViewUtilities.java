package org.jbomberman.view;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jbomberman.controller.MainController;
import org.jbomberman.utils.Coordinate;

/**
 * This class contains some utilities for the views
 */
public class ViewUtilities {

    public static final int SCALE_FACTOR = 35;
    public static final int WIDTH = SCALE_FACTOR* MainController.DX;
    public static final int HEIGHT = SCALE_FACTOR*MainController.DY;
    public static final Font CUSTOM_FONT_SMALL = Font.loadFont(ViewUtilities.class.getResourceAsStream("/org/jbomberman/SfComicScriptBold-YXD2.ttf"), SCALE_FACTOR-5);
    public static final int MAX_NAME_LETTERS = 8;


    /**
     * @param string the name of the main text
     * @param opacity set true if the pane needs to be used in game
     * @param main set true if the pane needs to be used as the main pane for the main menu
     * opacity and main can't be true at the same time
     * if neither opacity nor main is true, the pane is used for the main menu (except for the main screen
     * of the main menu)
     * @return the pane
     */
    public static Pane createPane(String string, boolean opacity, boolean main) {
        if (opacity && main) {
            System.out.println("you can't set opacity and main at the same time!!");
            return new Pane();
        }
        Font customFont;
        Pane pane;
        Text text = new Text(string);

        if (opacity) {
            pane = getPaneBackground(true);
            pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        }else {
            pane = getPaneBackground(false);
        }

        if (main) {
            customFont = Font.loadFont(ViewUtilities.class.getResourceAsStream("/org/jbomberman/SfComicScriptBold-YXD2.ttf"), SCALE_FACTOR + 15);
            text.setFont(customFont);
            text.setStyle("-fx-fill: darkcyan");
        } else {
            customFont = Font.loadFont(ViewUtilities.class.getResourceAsStream("/org/jbomberman/SfComicScriptBold-YXD2.ttf"), SCALE_FACTOR + 5);
            text.setFont(customFont);
            text.setStyle("-fx-fill: white");
        }

        text.setStroke(Color.BLACK);
        text.setStrokeWidth(2);
        text.setLayoutY(118);
        text.setLayoutX((WIDTH - text.getBoundsInLocal().getWidth()) / 2);
        pane.getChildren().add(text);
        return pane;
    }

    /**
     * This method is used to create the pane with or without the background
     * @param opacity to set the opacity of the background
     * @return
     */
    private static Pane getPaneBackground(boolean opacity) {
        ImageView imageView = new ImageView(new Image(ViewUtilities.class.getResourceAsStream("/org/jbomberman/sfondo_small.jpg")));
        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH);
        if (opacity) imageView.setOpacity(0);
        return new Pane(imageView);
    }

    /**
     * This method returns a button that highlights itself when the mouse goes over it
     * @param text text of the button
     * @param i fixed position of the button
     * @param color color of the button
     * @return
     */
    public static Label getButton(String text, int i, Color color) {
        Text textNode = new Text(text);
        textNode.setFont(CUSTOM_FONT_SMALL);
        textNode.setFill(color);

        Label clickableText = new Label();
        clickableText.setGraphic(textNode);

        clickableText.setOnMouseEntered(event -> {
            textNode.setFill(Color.YELLOW);
        });

        clickableText.setOnMouseExited(event -> {
            textNode.setFill(color);
        });


        Platform.runLater(() -> {
            double textWidth = textNode.getLayoutBounds().getWidth();
            double textHeight = textNode.getLayoutBounds().getHeight();
            double centerX = (double) WIDTH / 2;
            double centerY = (double) HEIGHT / 2;
            clickableText.setLayoutX(centerX - textWidth / 2);
            clickableText.setLayoutY(centerY - textHeight / 2 + (i * SCALE_FACTOR));
        });

        return clickableText;

    }

    /**
     * This method is used to switch pane in a view
     * @param toHide
     * @param toShow
     */
    public static void changePane(Pane toHide, Pane toShow) {
        toHide.setVisible(false);
        toShow.setVisible(true);
        toShow.toFront();
        toShow.requestFocus();
    }

    /**
     * this method returns the labels that are going to represent the floating points in the game
     * @param string the points
     * @param coordinate
     * @return
     */
    public static Label getFloatingLabel(String string, Coordinate coordinate){
        Label text = new Label("+" + string);
        text.setFont(CUSTOM_FONT_SMALL);
        text.setLayoutX(((double)coordinate.x() * SCALE_FACTOR)-30);
        text.setLayoutY((((double)coordinate.y()) * SCALE_FACTOR)-10);
        return text;
    }

    /**
     * This method create an ImageView of a fixed dimension
     * @param coordinate
     * @param image
     * @return
     */
    public static ImageView createImageView(Coordinate coordinate, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setLayoutX((double)coordinate.x() * SCALE_FACTOR);
        imageView.setLayoutY((double)coordinate.y() * SCALE_FACTOR);
        imageView.setFitHeight(SCALE_FACTOR);
        imageView.setFitWidth(SCALE_FACTOR);
        return imageView;
    }

    /**
     * This method centers the given node
     * @param node the node to center
     */
    public static void setCentred(Node node){
        Platform.runLater(() -> {
            double textWidth = node.getLayoutBounds().getWidth();
            double textHeight = node.getLayoutBounds().getHeight();

            double centerX = (double) ViewUtilities.WIDTH / 2;
            double centerY = (double) ViewUtilities.HEIGHT / 2;
            node.setLayoutX(centerX - textWidth / 2);
            node.setLayoutY(centerY - textHeight / 2);
        });
    }
}