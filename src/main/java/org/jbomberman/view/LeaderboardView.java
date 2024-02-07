package org.jbomberman.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.jbomberman.controller.MainController;
import org.jbomberman.model.User;

import java.util.Comparator;
import java.util.List;

/**
 * This class is used to represent the leaderboard in the MainView
 */
public class LeaderboardView {

    private final MainController controller = MainController.getInstance();
    private final Pane leaderboardPane = ViewUtilities.createPane("Leaderboard", false, false);
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contentPane = new VBox(2);

    public LeaderboardView() {
        genScrollPane();

        leaderboardPane.getChildren().add(scrollPane);
    }

    /**
     * This method generates the view of the leaderboard
     */
    private void genScrollPane() {
        scrollPane.setPrefSize(400, 200);
        contentPane.setAlignment(Pos.CENTER);
        scrollPane.setTranslateY(20);
        scrollPane.setStyle("-fx-control-inner-background: transparent;");
        contentPane.setStyle("-fx-background-color: black;");
        scrollPane.setId("mainScrollPane");
        scrollPane.getStylesheets().add("org/jbomberman/view/scrollPane.css");
        scrollPane.setContent(contentPane);

        ViewUtilities.setCentred(scrollPane);
    }

    /**
     * This method updates the view of the leaderboard
     */
    public void updateScrollPane() {
        contentPane.getChildren().clear();
        contentPane.setAlignment(Pos.CENTER_LEFT);

        List<User> leaderboard = controller.loadLeaderboard();
        leaderboard.sort(Comparator.comparingInt(User::score).reversed().thenComparing(User::level).thenComparing(User::name));


        leaderboard.forEach(user -> {
            String playerName = user.name();
            int nameLength = playerName.length();
            int paddingLength = ViewUtilities.MAX_NAME_LETTERS - nameLength;
            String username = playerName + " ".repeat(paddingLength);

            Label player = new Label(username + ": " + user.score() + " - " + user.level());
            player.setFont(ViewUtilities.CUSTOM_FONT_SMALL);
            player.setStyle("-fx-text-fill: white;");
            contentPane.getChildren().add(player);
        });
    }

    public Pane getLeaderboardPane() {
        return leaderboardPane;
    }
}
